package com.joneill.unnamed7.desktop.objects.world.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.animations.EnemyHitAnimation;
import com.joneill.unnamed7.desktop.objects.GameObject;
import com.joneill.unnamed7.desktop.objects.Rectangle;
import com.joneill.unnamed7.helper.Assets;

/**
 * Created by josep_000 on 4/25/2016.
 */
public class Enemy extends GameObject {
    public static final float ENEMY_WIDTH = 3.0f;
    public static final float ENEMY_HEIGHT = 3.0f;
    private static final float NO_ROT_ZONE_RADIUS = 2.5f;
    public Texture texture;
    public float speed;
    public float startingHealth, health;
    public Vector2 destination;
    public boolean isHit;
    public boolean isDead;

    private EnemyHitAnimation animation;

    public Enemy() {
        super();
        init();
    }

    private void init() {
        texture = Assets.getInstance().getAssetsEnemy().getNormEnemy();
        speed = 0.10f;
        startingHealth = 5.0f;
        health = 5.0f;
        setDimension(new Vector2(ENEMY_WIDTH, ENEMY_HEIGHT));
        setTerminalVelocity(new Vector2(speed, speed));
        setOrigin(new Vector2(dimension.x / 2, dimension.y / 2));
        bounds.add(new Rectangle(position.x, position.y, dimension.x, dimension.y, position, true, true));
        animation = new EnemyHitAnimation();
        animation.adjustDuration(0.5f);
    }

    //Set destination before updating
    @Override
    public void update() {
        super.update();
        //bounds.updatePosition(position.x, position.y, rotation);
        moveEnemy();
        if(health <= 0) {
            isDead = true;
        }
    }

    private void moveEnemy() {
        Vector2 tempPos = new Vector2(position.x, position.y);
        Vector2 slope = destination.sub(tempPos).nor();
        velocity.set(slope.x * speed, slope.y * speed);
        System.out.println(velocity.x * 100);
        rotation = slope.angle();
    }

    public void render(SpriteBatch batch) {
        animation.update(Gdx.graphics.getDeltaTime());
        batch.setShader(animation.getShader());
        if(animation.getShader() != null) {
            float percentage = health / startingHealth;
            animation.getShader().setUniformf("u_color", 1.0f, 1.0f, 0.0f);
        }
        batch.draw(texture, position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, 1, 1, rotation, 0, 0, texture.getHeight(), texture.getHeight(), false,
                false);
        batch.setShader(null);
    }

    public void hitByBullet(float damage) {
        health -= damage;
        if(animation.isActive()) {
            animation.reset(false);
        }
        animation.start();
    }

    public boolean isDead() { return isDead; }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getDestination() {
        return destination;
    }

    public void setDestination(Vector2 destination) {
        this.destination = new Vector2(destination.x, destination.y);
    }
}
