package com.joneill.unnamed7.desktop.objects.world.spawner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.world.enemies.Enemy;

import java.util.List;

/**
 * Created by josep_000 on 5/23/2016.
 */
public class EnemySpawner extends Spawner {
    public List<Enemy> objectList;
    public float minRadius;
    public float maxRadius;

    public EnemySpawner(Vector2 position, float minRadius, float maxRadius) {
        super(position);
        spawnsPerSecond = 0.4f;
        timeTilSpawn = 1 / spawnsPerSecond;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }

    @Override
    public void update(float deltaTime) {
        if(objectList == null) {
            System.out.println("Must set objectList");
            return;
        }
        super.update(deltaTime);
    }

    @Override
    public void spawn() {
        super.spawn();
        float angle = MathUtils.random() * (float) Math.PI * 2;
        float start = (maxRadius - minRadius) / maxRadius;
        float radius = (float) Math.sqrt(MathUtils.random(start, 1.0f)) * maxRadius;
        float xPos = position.x + radius * MathUtils.cos(angle);
        float yPos = position.y + radius * MathUtils.sin(angle);

        Enemy object = new Enemy();
        object.setPosition(new Vector2(xPos, yPos));
        objectList.add(object);
    }


    public List<Enemy> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Enemy> objectList) {
        this.objectList = objectList;
    }
}
