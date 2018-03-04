package com.joneill.unnamed7.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.joneill.unnamed7.helper.Assets;
import com.joneill.unnamed7.helper.MainController;
import com.joneill.unnamed7.helper.Renderer;

/**
 * Created by josep_000 on 4/7/2016.
 */
public class GameScreen implements Screen {
    private Game game;
    private MainController controller;
    private Renderer renderer;

    public GameScreen(Game game) {
        this.game = game;
        init();
    }

    @Override
    public void show() {

    }

    private void init() {
        Assets.getInstance().loadAssets();
        controller = new MainController();
        renderer = new Renderer();
        controller.setCamera(renderer.getCamera());
        controller.init();
        renderer.init(controller);
    }

    @Override
    public void render(float delta) {
        controller.update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
