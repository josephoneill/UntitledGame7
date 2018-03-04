package com.joneill.unnamed7.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by josep_000 on 3/29/2016.
 */

//This class will hold all of the rendering done in the game
public class Renderer {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Texture image;
    private MainController controller;

    public Renderer() {
        initCamera();
    }

    private void initCamera() {
        //Set the viewport width and height of the camera
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);
        //set the initial position of the camera to the x-coord 5
        camera.position.set(5, 0, 0);
        camera.update();
    }

    public void init(MainController controller) {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        image = new Texture("images/badlogic.jpg");
        this.controller = controller;
    }

    public void render() {
        controller.getWorld().getCameraHelper().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        controller.getWorld().render(batch);
        font.draw(batch, String.valueOf(controller.getWorld().getMainCharacter().getVelocity().x), 0, 480);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        controller.getWorld().renderDebugging(shapeRenderer);
        shapeRenderer.end();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
