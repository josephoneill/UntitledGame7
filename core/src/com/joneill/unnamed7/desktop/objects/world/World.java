package com.joneill.unnamed7.desktop.objects.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.joneill.unnamed7.desktop.objects.MainCharacter;
import com.joneill.unnamed7.desktop.objects.Rectangle;
import com.joneill.unnamed7.desktop.objects.world.enemies.Enemy;
import com.joneill.unnamed7.desktop.objects.world.spawner.SpawnerHelper;
import com.joneill.unnamed7.desktop.objects.world.terrain.*;
import com.joneill.unnamed7.desktop.objects.world.weapons.bullets.Bullet;
import com.joneill.unnamed7.helper.CameraHelper;
import com.joneill.unnamed7.helper.Constants;
import com.joneill.unnamed7.helper.SimplexNoise;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by josep_000 on 4/7/2016.
 */
public class World {
    private OrthographicCamera camera;
    private CameraHelper cameraHelper;
    private MainCharacter mainCharacter;
    private SpawnerHelper spawner;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private SimplexNoise simplexNoise;
    private WorldGenerator worldGen;
    private Terrain map[][];
    //Goes left, top, right, bottom
    private int[] cameraBounds;

    public World() {
        simplexNoise = new SimplexNoise();
        worldGen = new WorldGenerator();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();
        map = new Terrain[Constants.WORLD_WIDTH][Constants.WORLD_HEIGHT];
        cameraBounds = new int[4];
    }

    public void initWorld() {
        spawnCharacter();
        spawnEnemies();
        initCamera();
        genWorld();
        spawner = new SpawnerHelper(this);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        updateCamera();
        System.out.println(enemies.size());
        mainCharacter.update();
        for (Enemy enemy : enemies) {
            enemy.setDestination(mainCharacter.getPosition());
            enemy.update();
        }

        spawner.update(deltaTime, mainCharacter.getPosition());

        checkCollisions();
    }

    private void checkCollisions() {
        checkBulletCol();
    }

    private void checkBulletCol() {
        List<Bullet> bulletsRemoveList = new ArrayList<Bullet>();
        List<Enemy> enemiesRemoveList = new ArrayList<Enemy>();
        for(Bullet bullet : mainCharacter.getGun().getBullets()) {
            if(bullet.isDead()) {
                bulletsRemoveList.add(bullet);
            }
            for(Enemy enemy : enemies) {
                for(Rectangle bound : enemy.bounds) {
                    for(Rectangle bulletBound : bullet.bounds) {
                        if (bulletBound.collidesWithRect(bound)) {
                            enemy.hitByBullet(bullet.getDamage());
                            bulletsRemoveList.add(bullet);
                            if(enemy.isDead()) {
                                enemiesRemoveList.add(enemy);
                            }
                        }
                    }
                }
            }
        }

        mainCharacter.getGun().getBullets().removeAll(bulletsRemoveList);
        enemies.removeAll(enemiesRemoveList);
    }

    private void spawnCharacter() {
        mainCharacter = new MainCharacter(camera);
    }

    private void spawnEnemies() {
        /*enemies = new ArrayList<Enemy>();
        for (int i = 0; i < 100; i++) {
            Enemy enemy = new Enemy();
            enemies.add(enemy);
        }*/
        for(int i = 0; i < 5; i++) {
            Enemy enemy = new Enemy();
            enemy.getPosition().set(mainCharacter.getPosition().x, mainCharacter.getPosition().y);
            enemies.add(enemy);
        }
    }

    private void initCamera() {
        cameraHelper = new CameraHelper();
        cameraHelper.setPosition(mainCharacter.getPosition());
        cameraHelper.setTarget(mainCharacter);
        cameraBounds[0] = getTerIndexByPos(Constants.VIEWPORT_WIDTH) / 2;
        cameraBounds[1] = Constants.WORLD_HEIGHT - getTerIndexByPos(Constants.VIEWPORT_HEIGHT) / 2;
        cameraBounds[2] = Constants.WORLD_WIDTH - getTerIndexByPos(Constants.VIEWPORT_WIDTH) / 2;
        cameraBounds[3] = getTerIndexByPos(Constants.VIEWPORT_HEIGHT) / 2;
    }

    private void genWorld() {
        float[][] noise = simplexNoise.generateSimplexNoise(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, 3, 0.4f, 0.03f,
                (int) MathUtils.random(1, 100000000));
        for (int x = 0; x < Constants.WORLD_WIDTH; x++) {
            for (int y = 0; y < Constants.WORLD_HEIGHT; y++) {
                float xPos = x * Terrain.TERRAIN_SIZE;
                float yPos = y * Terrain.TERRAIN_SIZE;
                float noiseVal = noise[x][y];
                noiseVal = noiseVal < -1.0f ? -1.0f : noiseVal;
                map[x][y] = worldGen.getTerrainFromNoise(noiseVal, xPos, yPos);
            }
        }
    }

    //Updates camera and keeps the camera within the bounds of the world
    private void updateCamera() {
        cameraHelper.update(Gdx.graphics.getDeltaTime());
        int indexTerX = getTerIndexByPos(mainCharacter.position.x);
        int indexTerY = getTerIndexByPos(mainCharacter.position.y);

        if(indexTerX <= cameraBounds[0]
                || indexTerX >= cameraBounds[2]) {
            if(cameraHelper.getFollowX()) {
                cameraHelper.setFollowX(false);
            }
        } else {
            if(!cameraHelper.getFollowX()) {
                cameraHelper.setFollowX(true);
            }
        }

        if(indexTerY >= cameraBounds[1]
                || indexTerY <= cameraBounds[3]) {
            if(cameraHelper.getFollowY()) {
                cameraHelper.setFollowY(false);
            }
        } else {
            if(!cameraHelper.getFollowY()) {
                cameraHelper.setFollowY(true);
            }
        }
    }

    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Terrain[][] clippedMap = clipTerrain(map, mainCharacter, 4, 4);
        for (int x = 0; x < clippedMap.length; x++) {
            for (int y = 0; y < clippedMap[0].length; y++) {
                Terrain terrain = clippedMap[x][y];
                if(terrain != null) {
                    terrain.render(batch);
                }
            }
        }

        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }

        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }

        mainCharacter.render(batch);
    }

    public void renderDebugging(ShapeRenderer renderer) {
        /*for (Enemy enemy : enemies) {
            for(Rectangle bound : enemy.bounds) {
                bound.renderBounds(renderer);
            }
        }

        for(Rectangle bound : mainCharacter.bounds) {
            bound.renderBounds(renderer);
        }*/
    }

    //Method to clip the terrain so we only render the terrain on the screen at the time
    //Padding is defined in tiles, not "meters" or pixels
    private Terrain[][] clipTerrain(Terrain[][] map, MainCharacter character, int paddingX, int paddingY) {
        paddingX *= Terrain.TERRAIN_SIZE;
        paddingY *= Terrain.TERRAIN_SIZE;
        //How many tiles of terrain do we need to render
        int clippingRangeWidth = (int) Math.ceil((Constants.VIEWPORT_WIDTH / Terrain.TERRAIN_SIZE) + paddingX);
        int clippingRangeHeight = (int) Math.ceil((Constants.VIEWPORT_HEIGHT / Terrain.TERRAIN_SIZE) + paddingY);
        //Make sure that we don't start below 0 or start above the last tile
        int clippingStartX = (int) MathUtils.clamp(Math.ceil(getTerIndexByPos(mainCharacter.getPosition().x) - (clippingRangeWidth / 2)),
                0, (Constants.WORLD_WIDTH) - 1);
        int clippingStartY = (int) MathUtils.clamp(Math.ceil(getTerIndexByPos(mainCharacter.getPosition().y) - (clippingRangeHeight / 2)),
                0, (Constants.WORLD_HEIGHT) - 1);
        //Clamp the clipping edge to the first or last tile in the world size
        int clippingEndX = MathUtils.clamp(clippingStartX + clippingRangeWidth, 0, Constants.WORLD_WIDTH - 1);
        int clippingEndY = MathUtils.clamp(clippingStartY + clippingRangeHeight, 0, Constants.WORLD_HEIGHT - 1);
        Terrain[][] clippedMap = new Terrain[clippingRangeWidth][clippingRangeHeight];
        int clipX = -1, clipY = -1;
        for (int x = clippingStartX; x < clippingEndX; x++) {
            clipX++;
            if (clipY != -1) {
                clipY = -1;
            }
            for (int y = clippingStartY; y < clippingEndY; y++) {
                clipY++;
                clippedMap[clipX][clipY] = map[x][y];
            }
        }
        return clippedMap;
    }

    private int getTerIndexByPos(float pos) {
        int index = 0;
        index = (int) Math.ceil(pos / Terrain.TERRAIN_SIZE);
        return index;
    }

    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public void setMainCharacter(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    public void setCameraHelper(CameraHelper cameraHelper) {
        this.cameraHelper = cameraHelper;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

}
