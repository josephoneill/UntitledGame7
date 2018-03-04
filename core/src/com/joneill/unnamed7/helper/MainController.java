package com.joneill.unnamed7.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.joneill.unnamed7.desktop.objects.world.World;

/**
 * Created by josep_000 on 3/29/2016.
 */

//The MainController class holds all the objects of the game and
//their updates
public class MainController {
    private OrthographicCamera camera;
    private World world;

    //Instantiate the variables
    public void init() {
        world = new World();
        if(camera != null) {
            world.setCamera(camera);
        } else {
        }
        world.initWorld();
    }

    public void update() {
        world.update();
        checkAdminControls();
    }

    private void checkAdminControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            Constants.VIEWPORT_WIDTH += 20 * Gdx.graphics.getDeltaTime();
            Constants.VIEWPORT_HEIGHT += 20 * Gdx.graphics.getDeltaTime();
            camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            Constants.VIEWPORT_WIDTH -= 20 * Gdx.graphics.getDeltaTime();
            Constants.VIEWPORT_HEIGHT -= 20 * Gdx.graphics.getDeltaTime();
            camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
