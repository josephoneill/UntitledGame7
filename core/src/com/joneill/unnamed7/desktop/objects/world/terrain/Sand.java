package com.joneill.unnamed7.desktop.objects.world.terrain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.MainCharacter;
import com.joneill.unnamed7.helper.Assets;

/**
 * Created by josep_000 on 4/9/2016.
 */
public class Sand extends Terrain {

    public Sand(float x, float y) {
        super(x, y);
        texture = Assets.getInstance().getAssetsTerrain().getSand();
        friction = new Vector2(1.2f, 1.2f);
    }

    @Override
    public void onTouch(MainCharacter mainCharacter) {
        super.onTouch(mainCharacter);
    }
}
