package com.joneill.unnamed7.desktop.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josep_000 on 3/18/2016.
 */
public class GameObject {
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 acceleration;
    public Vector2 terminalVelocity;
    public Vector2 friction;
    public Vector2 origin;
    public Vector2 dimension;
    public List<Rectangle> bounds;
    public float rotation;
    //We can either use our own "physics"
    //or use a box2d body
    public Body body;

    public GameObject() {
        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2();
        friction = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        origin = new Vector2();
        dimension = new Vector2(1,1);
        bounds = new ArrayList<Rectangle>();
        rotation = 0;
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (body == null) {
            updateX(deltaTime);
            updateY(deltaTime);
            updateBounds();
        } else {
            position.set(body.getPosition());
        }
    }

    public void updateX(float deltaTime) {
        if (velocity.x != 0) {
            if (velocity.x > 0) {
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
            }
        }
        velocity.x += acceleration.x * deltaTime;
        //MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);

        if(Math.abs(velocity.x) > terminalVelocity.x) {
            if(velocity.x > 0) {
                velocity.x = terminalVelocity.x;
            }
            else {
                velocity.x = -terminalVelocity.x;
            }
        }

        position.x += velocity.x;
    }

    public void updateY(float deltaTime) {
        if (velocity.y != 0) {
            if (velocity.y > 0) {
                velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
            }
        }
        velocity.y += acceleration.y * deltaTime;

        //MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);

        if(Math.abs(velocity.y) > terminalVelocity.y) {
            if(velocity.y > 0) {
                velocity.y = terminalVelocity.y;
            }
            else {
                velocity.y = -terminalVelocity.y;
            }
        }
        position.y += velocity.y;
    }

    public void updateBounds() {
        for (Rectangle bound : bounds) {
            bound.updatePosition(position.x, position.y, rotation);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getTerminalVelocity() {
        return terminalVelocity;
    }

    public void setTerminalVelocity(Vector2 terminalVelocity) {
        this.terminalVelocity = terminalVelocity;
    }

    public Vector2 getFriction() {
        return friction;
    }

    public void setFriction(Vector2 friction) {
        this.friction = friction;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Vector2 getDimension() {
        return dimension;
    }

    public void setDimension(Vector2 dimension) {
        this.dimension = dimension;
    }

    public List<Rectangle> getBounds() {
        return bounds;
    }

    public void setBounds(ArrayList<Rectangle> bounds) {
        this.bounds = bounds;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
