package com.joneill.unnamed7.desktop.objects.world.spawner;

import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.GameObject;

import java.util.List;

/**
 * Created by josep_000 on 5/23/2016.
 */
public class Spawner {
    public Vector2 position;
    public float timer;
    public float spawnsPerSecond = 1;
    public float timeTilSpawn;

    public Spawner(Vector2 position) {
        this.position = position;
        timeTilSpawn = 1 / spawnsPerSecond;
    }

    public void update(float deltaTime) {
        timer += deltaTime;
        if(timer > timeTilSpawn) {
            timer = 0;
            spawn();
        }
    }

    public void spawn() {

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public float getSpawnsPerSecond() {
        return spawnsPerSecond;
    }

    public void setSpawnsPerSecond(float spawnsPerSecond) {
        this.spawnsPerSecond = spawnsPerSecond;
    }

    public float getTimeTilSpawn() {
        return timeTilSpawn;
    }

    public void setTimeTilSpawn(float timeTilSpawn) {
        this.timeTilSpawn = timeTilSpawn;
    }
}
