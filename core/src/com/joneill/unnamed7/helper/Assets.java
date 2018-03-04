package com.joneill.unnamed7.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by josep_000 on 4/9/2016.
 */

//Top-down shooter assets from: http://kenney.nl/assets/topdown-shooter
public class Assets {
    private static final Assets instance = new Assets();
    private Assets() {}

    public AssetsTerrain assetsTerrain;
    public AssetsEnemy assetsEnemy;
    public AssetsWeapon assetsWeapon;
    public AssetsBullet assetsBullet;

    public AssetsShader assetsShader;

    public class AssetsTerrain {
        public Texture sand;
        public Texture tallGrass;
        public Texture dirt;
        public Texture grass;
        public Texture ice;
        public Texture rock;

        public AssetsTerrain() {
            sand = new Texture("images/tiles/tile_15.png");
            tallGrass = new Texture("images/tiles/tile_17.png");
            dirt = new Texture("images/tiles/tile_05.png");
            grass = new Texture("images/tiles/tile_01.png");
            ice = new Texture("images/tex_ice.jpg");
            rock = new Texture("images/tiles/tile_09.png");
        }

        public Texture getSand() {
            return sand;
        }

        public Texture getTallGrass() {
            return tallGrass;
        }

        public Texture getDirt() {
            return dirt;
        }

        public Texture getGrass() {
            return grass;
        }

        public Texture getIce() {
            return ice;
        }

        public Texture getRock() {
            return rock;
        }
    }

    public class AssetsEnemy {
        public Texture normEnemy;

        public AssetsEnemy() {
            normEnemy = new Texture("images/zombie1/zoimbie1_hold.png");
        }

        public Texture getNormEnemy() {
            return normEnemy;
        }

        public void setNormEnemy(Texture normEnemy) {
            this.normEnemy = normEnemy;
        }
    }

    public class AssetsWeapon {
        public Texture normalGun;

        public AssetsWeapon() {
           // normalGun = new Texture("");
        }
    }

    public class AssetsBullet {
        public Texture normalBullet;

        public AssetsBullet() {
            normalBullet = new Texture("images/weapon_gun.png");
        }

        public Texture getNormalBullet() {
            return normalBullet;
        }

        public void setNormalBullet(Texture normalBullet) {
            this.normalBullet = normalBullet;
        }
    }

    public class AssetsShader {
        public ShaderProgram eHitShader;

        public AssetsShader() {
            eHitShader = new ShaderProgram(Gdx.files.internal("shaders/e_hit_vshader.glsl").readString(),
                    Gdx.files.internal("shaders/e_hit_fshader.glsl").readString());
            eHitShader.pedantic = false;
        }

        public ShaderProgram geteHitShader() {
            return eHitShader;
        }
    }

    public void loadAssets() {
        assetsTerrain = new AssetsTerrain();
        assetsEnemy = new AssetsEnemy();
        assetsBullet = new AssetsBullet();
        assetsWeapon = new AssetsWeapon();
        assetsShader = new AssetsShader();
    }

    public AssetsEnemy getAssetsEnemy() {
        return assetsEnemy;
    }

    public AssetsTerrain getAssetsTerrain() {
        return assetsTerrain;
    }

    public AssetsShader getAssetsShader() {
        return assetsShader;
    }

    public AssetsWeapon getAssetsWeapon() {
        return assetsWeapon;
    }


    public AssetsBullet getAssetsBullet() {
        return assetsBullet;
    }


    public static Assets getInstance() {
        return instance;
    }
}
