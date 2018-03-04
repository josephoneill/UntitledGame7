package com.joneill.unnamed7.desktop.objects.world.spawner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.world.World;
import com.joneill.unnamed7.desktop.objects.world.terrain.Terrain;
import com.joneill.unnamed7.helper.Constants;

/**
 * Created by josep_000 on 5/23/2016.
 */
public class SpawnerHelper {
    public World world;
    public EnemySpawner enemySpawner;

    public SpawnerHelper(World world) {
        Vector2 pos = new Vector2(world.getMainCharacter().getPosition().x + MathUtils.random(-1, 1),
                world.getMainCharacter().getPosition().y +  MathUtils.random(-1, 1));
        float minRadius = Constants.VIEWPORT_WIDTH / 2 + Terrain.TERRAIN_SIZE;
        enemySpawner = new EnemySpawner(pos, minRadius, minRadius + Terrain.TERRAIN_SIZE * 4);
        enemySpawner.setObjectList(world.getEnemies());
    }

    public void update(float deltaTime, Vector2 charPos) {
        enemySpawner.getPosition().set(charPos);
        enemySpawner.update(deltaTime);
    }
}
