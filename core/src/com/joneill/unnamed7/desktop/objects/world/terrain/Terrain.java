package com.joneill.unnamed7.desktop.objects.world.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.MainCharacter;

/**
 * Created by josep_000 on 4/9/2016.
 */
public class Terrain {
    public static final float TERRAIN_SIZE = 4.0f;
    public Texture texture;
    public Vector2 friction;
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public boolean hasEntered;

    public Terrain(float x, float y) {
        friction = new Vector2();
        position = new Vector2(x, y);
        dimension = new Vector2(TERRAIN_SIZE, TERRAIN_SIZE);
        origin = new Vector2(dimension.x/2, dimension.y/2);
        hasEntered = false;
    }

    public void onTouch(MainCharacter character) {
        if(!hasEntered) {
            character.setFriction(friction);
            hasEntered = true;
        }
    }

    public void setFriction(Vector2 friction) {
        this.friction = friction;
    }

    public void render(SpriteBatch batch) {
        if(texture == null) {
            //System.out.println("Terrain texture is null");
            return;
        }
        batch.draw(texture, position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, 1, 1, 0, 0, 0, texture.getHeight(), texture.getHeight(), false,
                false);
    }
}
