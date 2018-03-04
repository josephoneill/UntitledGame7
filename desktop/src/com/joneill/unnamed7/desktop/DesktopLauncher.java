package com.joneill.unnamed7.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joneill.unnamed7.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Unnamed #7";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MainGame(), config);
	}
}
