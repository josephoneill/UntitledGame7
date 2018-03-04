package com.joneill.unnamed7.desktop.objects.world.weapons.Guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joneill.unnamed7.desktop.objects.world.weapons.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 4/24/2016.
 */
public class Gun {
    private Vector2 position;
    private float angle;
    public Bullet.BULLET_TYPE bulletType;
    public float bulletsPerSecond;
    public int magSize;
    public List<Bullet> bullets;
    private float lastShotDur;
    private float tBetweenShots;
    private boolean canShoot;

    public Gun(Vector2 position, float angle) {
        this.position = position;
        this.angle = angle;
        init();
    }

    private void init() {
        bulletType = Bullet.BULLET_TYPE.NORMAL;
        bulletsPerSecond = 3.0f;
        magSize = 60;
        tBetweenShots = 1.0f / bulletsPerSecond;
        bullets = new ArrayList<Bullet>();
    }

    public void update() {
        if(lastShotDur >= tBetweenShots) {
            canShoot = true;
            lastShotDur -= tBetweenShots;
        } else {
            lastShotDur += Gdx.graphics.getDeltaTime();
        }

        checkInput();

        for(Bullet bullet : bullets) {
            bullet.update();
        }
    }

    public void render(SpriteBatch batch) {
        for(Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    //Must set bullet each time before updating
    private void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if(canShoot) {
                fireBullet();
                canShoot = false;
            }
        }
    }

    private void fireBullet() {
        Bullet bullet = null;
        switch(bulletType) {
            case NORMAL:
                bullet = new Bullet(position, angle);
                break;
            default:
                bullet = new Bullet(position, angle);
                break;
        }

        bullets.add(bullet);
    }

    public float getBulletsPerSecond() {
        return bulletsPerSecond;
    }

    public void setBulletsPerSecond(float bulletsPerSecond) {
        this.bulletsPerSecond = bulletsPerSecond;
    }

    public int getMagSize() {
        return magSize;
    }

    public void setMagSize(int magSize) {
        this.magSize = magSize;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
