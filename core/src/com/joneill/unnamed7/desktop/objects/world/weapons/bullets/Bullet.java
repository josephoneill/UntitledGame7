package com.joneill.unnamed7.desktop.objects.world.weapons.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.GameObject;
import com.joneill.unnamed7.desktop.objects.Rectangle;
import com.joneill.unnamed7.helper.Assets;

/**
 * Created by josep_000 on 4/16/2016.
 */
public class Bullet extends GameObject {
    public enum BULLET_TYPE {NORMAL, HYPER};
    public float speed;
    public Texture texture;
    public float damage;
    public float duration;
    public float lifespan;
    public boolean isDead;

    public Bullet(Vector2 startPosition, float angle) {
        super();
        speed = 3.2f;
        duration = 4.0f;
        init(startPosition, angle);
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = Gdx.graphics.getDeltaTime();
        lifespan += deltaTime;
        if(lifespan > duration) {
            isDead = true;
        }
    }

    private void init(Vector2 startPosition, float angle) {
        texture = Assets.getInstance().getAssetsBullet().getNormalBullet();
        damage = 1.0f;
        position.set(startPosition);
        dimension.set(0.5f, 0.25f);
        Vector2 hypo = getVelocityFromAngle(angle).nor();
        velocity.set(hypo.x, hypo.y);
        rotation = angle;
        bounds.add(new Rectangle(position.x, position.y, dimension.x, dimension.y, position, true, true));
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, dimension.x,
                dimension.y, dimension.x, dimension.y, 1, 1, rotation, 0, 0, texture.getHeight(), texture.getHeight(), false,
                false);
    }

    private Vector2 getVelocityFromAngle(float angle) {
        Vector2 velo = new Vector2();
        float x = MathUtils.cosDeg(angle);
        float y = MathUtils.sinDeg(angle);

        velo.set(x * speed, y * speed);
        return velo;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isDead() {
        return isDead;
    }
}