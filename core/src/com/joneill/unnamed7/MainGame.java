package com.joneill.unnamed7;

import com.badlogic.gdx.Game;
import com.joneill.unnamed7.screens.MainMenuScreen;

public class MainGame extends Game {

    @Override
	public void create () {
        this.setScreen(new MainMenuScreen(this));
        System.out.println("Runder");
    }

	@Override
	public void render () {
        super.render();
	}

}
