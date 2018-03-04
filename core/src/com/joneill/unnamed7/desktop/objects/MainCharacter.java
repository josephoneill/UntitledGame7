package com.joneill.unnamed7.desktop.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joneill.unnamed7.desktop.objects.world.terrain.Terrain;
import com.joneill.unnamed7.desktop.objects.world.weapons.Guns.Gun;
import com.joneill.unnamed7.desktop.objects.world.weapons.bullets.Bullet;
import com.joneill.unnamed7.helper.Constants;

/**
 * Created by josep_000 on 3/20/2016.
 */
public class MainCharacter extends GameObject {
    public static final float CHARACTER_WIDTH = 3;
    public static final float CHARACTER_HEIGHT = 3;
    private static final float PIVOT_RADIUS = 1.5f;
    //The gun starts below the pivot point
    //this defines the offset angle of the gun from the pivot
    private static final float GUN_ANGLE_START = 25.0f;
    private static final float NO_ROT_ZONE_RADIUS = 2.5f;
    private OrthographicCamera camera;
    private Vector2 pivot;
    private Vector2 pivotOffset;
    //How many blocks away from edge of map
    //can the character spawn
    private static int S_PADDING = 10;
    private Texture image;
    private float speed;
    private Gun gun;

    public MainCharacter(OrthographicCamera camera) {
        super();
        this.camera = camera;
        initVar();
        initData();
    }

    private void initVar() {
        image = new Texture("images/manblue/manBlue_gun.png");
        rotation = 0.0f;
        speed = 0.15f;
        pivot = new Vector2();
        pivotOffset = new Vector2(-0.2f + CHARACTER_WIDTH / 2.0f, -0.3f + CHARACTER_HEIGHT / 2.0f);
        gun = new Gun(position, rotation);
    }

    private void initData() {
        setDimension(new Vector2(CHARACTER_WIDTH, CHARACTER_HEIGHT));
        setVelocity(new Vector2(0, 0));
        setTerminalVelocity(new Vector2(speed, speed));
        setOrigin(new Vector2(dimension.x / 2, dimension.y / 2));
        float xPos = MathUtils.random(S_PADDING * Terrain.TERRAIN_SIZE, (Constants.WORLD_WIDTH - S_PADDING) * Terrain.TERRAIN_SIZE);
        float yPos = MathUtils.random(S_PADDING * Terrain.TERRAIN_SIZE, (Constants.WORLD_HEIGHT - S_PADDING) * Terrain.TERRAIN_SIZE);
        setPosition(new Vector2(xPos, yPos));
        gun.setPosition(new Vector2(position.x + CHARACTER_WIDTH, position.y));
        setCollisionBounds();
    }

    private void setCollisionBounds() {
        Rectangle bound1 = new Rectangle(position.x, position.y, dimension.x / 2, dimension.y, position, true, false);
        bound1.setCenter(new Vector2(position.x + dimension.x / 2, position.y + dimension.y / 2), position, false);
        Rectangle bound2 = new Rectangle(position.x + dimension.x / 2, position.y, dimension.x / 2, dimension.y / 2, position, true, false);
        bound2.setCenter(new Vector2(position.x, position.y + dimension.y / 2), position, false);
        bounds.add(bound1);
        bounds.add(bound2);
    }

    @Override
    public void update() {
        updateRotation();
        checkInput();
        gun.update();
        super.update();
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, 1, 1, rotation, 0, 0, image.getHeight(), image.getHeight(), false,
                false);

        gun.render(batch);

        /*batch.draw(image, gunPosition.x, gunPosition.y, 0.25f,
                0.25f, 0.5f, 0.5f, 1, 1, rotAngle, 0, 0, image.getHeight(), image.getHeight(), false,
                false);*/
    }

    private void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y = speed - friction.y;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y = -speed + friction.y;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -speed + friction.x;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = speed - friction.x;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(!Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocity.y = 0;
            }
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x = 0;
            }
        }

    }

    private void updateRotation() {
        Vector3 m = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mousePos = new Vector2(m.x, m.y);
        Vector2 slope = mousePos.sub(gun.getPosition());
        if(slope.len() >= NO_ROT_ZONE_RADIUS) {
            rotation = slope.angle();
        }

        pivot.set(position.x + pivotOffset.x, position.y + pivotOffset.y);
        gun.setPosition(rotateOnPivot(pivot, rotation, PIVOT_RADIUS));
        gun.setAngle(rotation);
    }

    private Vector2 rotateOnPivot(Vector2 pivot, float theta, float radius) {
        theta = theta - GUN_ANGLE_START < 0 ? (theta - GUN_ANGLE_START) + 360 : theta - GUN_ANGLE_START;
        float sine = MathUtils.sinDeg(theta) * radius;
        float cos =  MathUtils.cosDeg(theta) * radius;

        float x = pivot.x + cos;
        float y = pivot.y + sine;

        return new Vector2(x, y);
    }

    public Gun getGun() { return gun; }
}
