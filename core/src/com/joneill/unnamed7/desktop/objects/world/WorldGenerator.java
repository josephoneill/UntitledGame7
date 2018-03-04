package com.joneill.unnamed7.desktop.objects.world;

import com.badlogic.gdx.math.MathUtils;
import com.joneill.unnamed7.desktop.objects.world.terrain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by josep_000 on 4/12/2016.
 */
public class WorldGenerator {
    private static final float NOISE_SCALE_TOTAL = 2;
    private static final float NOISE_SCALE_FACTOR = 1;
    //Default percentages
    private float B_GRASS_P = 65;
    private float B_ROCKSAND_P = 10;
    private float B_ROCKDIRT_P = 15;
    private float B_DIRTSAND_P = 10;
    //B_GRASS
    private static final float T_TALLGRASS_P = 40;
    private static final float T_NORMGRASS_P = 60;
    //B_ROCKSAND
    private static final float T_RS_ROCK_P = 0;
    private static final float T_RS_SAND_P = 100;
    //B_ROCKDIRT
    private static final float T_RD_ROCK_P = 0;
    private static final float T_RD_DIRT_P = 100;
    //B_DIRTSAND
    private static final float T_DS_DIRT_P = 20;
    private static final float T_DS_SAND_P = 80;

    private float bGrassP;
    private float bRockSandP;
    private float bRockDirtP;
    private float bDirtSandP;

    private float tTallGrassP;
    private float tNormGrassP;
    private float tRSSandP;
    private float tRSRockP;
    private float tRDRockP;
    private float tRDDirtP;
    private float tDSDirtP;
    private float tDSSandP;

    public WorldGenerator() {

    }

    public Terrain getTerrainFromNoise(float noise, float xPos, float yPos) {
        //If we put 0% chance for one of the biomes, 1.0 and -1.0 are still possible
        //to be selected and then used for the 0% biome. To make sure this doesn't happen,
        //set the value to slightly above or below this
        noise = noise >= 1.0f ? 0.99f : noise;
        noise = noise <= -1.0f ? -0.99f : noise;
        populateTerrainPercentages(true);
        if (noise >= bGrassP) {
            Float[] bGrassTPs = {tTallGrassP, tNormGrassP};
            Arrays.sort(bGrassTPs, Collections.<Float>reverseOrder());
            if (noise > bGrassTPs[0]) {
                return getGrassType(bGrassTPs[0], xPos, yPos);
            } else if (noise > bGrassTPs[1]) {
                return getGrassType(bGrassTPs[1], xPos, yPos);
            }
        } else if (noise > bRockSandP) {
            Float[] bRockSandTPs = {tRSRockP, tRSSandP};
            Arrays.sort(bRockSandTPs, Collections.<Float>reverseOrder());
            if (noise > bRockSandTPs[0]) {
                return getRSType(bRockSandTPs[0], xPos, yPos);
            } else if (noise > bRockSandTPs[1]) {
                return getRSType(bRockSandTPs[1], xPos, yPos);
            }
        } else if (noise > bRockDirtP) {
            Float[] bRockDirtTPs = {tRDDirtP, tRDRockP};
            Arrays.sort(bRockDirtTPs, Collections.<Float>reverseOrder());
            if (noise > bRockDirtTPs[0]) {
                return getRDType(bRockDirtTPs[0], xPos, yPos);
            } else if (noise > bRockDirtTPs[1]) {
                return getRDType(bRockDirtTPs[1], xPos, yPos);
            }
        } else if (noise > bDirtSandP) {
            Float[] bDirtSandTPs = {tDSDirtP, tDSSandP};
            Arrays.sort(bDirtSandTPs, Collections.<Float>reverseOrder());
            if (noise > bDirtSandTPs[0]) {
                return getDSType(bDirtSandTPs[0], xPos, yPos);
            } else if (noise > bDirtSandTPs[1]) {
                return getDSType(bDirtSandTPs[1], xPos, yPos);
            }
        }
        return new Terrain(xPos, yPos);

    }

    //Converts a percentage value (from 0-100) from a B_ into
    //an equivalent float in the noise scale used (-1 to 1)
    private float convertBPercentToNoiseScale(float remainder, float percent) {
        float subValue = (percent / 100.0f) * NOISE_SCALE_TOTAL;
        //We subtract by the noise scale factor because the
        //remainder is on a 0-2 scale but we need a -1 to 1 scale
        float value = remainder - subValue;
        return value;
    }

    //Converts a percentage value (from 0-100) from a T_ into
    //an equivalent float in the noise scale used (-1 to 1)
    //realPercent is the bPercent that was already converted into noise scale
    //bPercent is the B_ percent that has not yet been converted into noise scale
    //tPercent is the T_ percent that has not yet been converted into noise scale
    private float convertTPercentToNoiseScale(float realBPercent, float bPercent, float tPercent, float previous) {
        //-2 means there is no previous in the given biome
        if(previous == -2) {
            float baseRange = (bPercent / 100.0f) * NOISE_SCALE_TOTAL;
            float normalizedTPercent = tPercent / 100.0f;
            float valuePercent = baseRange * normalizedTPercent;
            float value = round(realBPercent + baseRange - valuePercent, 2);
            return value;
        } else {
            previous = round(previous, 2);
            float baseRange = (bPercent / 100.0f) * NOISE_SCALE_TOTAL;
            float normalizedTPercent = tPercent / 100.0f;
            float subValue = round(baseRange * normalizedTPercent, 2);
            float value = previous - subValue;
            return value;
        }
    }

    //Sets the values of the floats that hold the
    //percentage of a certain terrain or biome being
    //selected
    private void populateTerrainPercentages(boolean randomize) {
        float remainder = 1.0f;
        bGrassP = convertBPercentToNoiseScale(remainder, B_GRASS_P);
        remainder = bGrassP;
        bRockSandP = convertBPercentToNoiseScale(remainder, B_ROCKSAND_P);
        remainder = bRockSandP;
        bRockDirtP = convertBPercentToNoiseScale(remainder, B_ROCKDIRT_P);
        remainder = bRockDirtP;
        bDirtSandP = convertBPercentToNoiseScale(remainder, B_DIRTSAND_P);
        remainder = bDirtSandP;

        //This must be done in order of greatest chance to least chance between terrains of biomes
        tNormGrassP = convertTPercentToNoiseScale(bGrassP, B_GRASS_P, T_NORMGRASS_P, -2);
        tTallGrassP = convertTPercentToNoiseScale(bGrassP, B_GRASS_P, T_TALLGRASS_P, tNormGrassP);
        tRSSandP = convertTPercentToNoiseScale(bRockSandP, B_ROCKSAND_P, T_RS_SAND_P, -2);
        tRSRockP = convertTPercentToNoiseScale(bRockSandP, B_ROCKSAND_P, T_RS_ROCK_P, tRSSandP);
        tRDDirtP = convertTPercentToNoiseScale(bRockDirtP, B_ROCKDIRT_P, T_RD_DIRT_P, -2);
        tRDRockP = convertTPercentToNoiseScale(bRockDirtP, B_ROCKDIRT_P, T_RD_ROCK_P, tRDDirtP);
        tDSSandP = convertTPercentToNoiseScale(bDirtSandP, B_DIRTSAND_P, T_DS_SAND_P, -2);
        tDSDirtP = convertTPercentToNoiseScale(bDirtSandP, B_DIRTSAND_P, T_DS_DIRT_P, tDSSandP);
    }

    private Terrain getGrassType(float value, float xPos, float yPos) {
        if(value == tTallGrassP) {
            return new TallGrass(xPos, yPos);
        } else if (value == tNormGrassP) {
            return new Grass(xPos, yPos);
        } else {
            return new Terrain(xPos, yPos);
        }
    }

    private Terrain getRSType(float value, float xPos, float yPos) {
        if(value == tRSSandP) {
            return new Sand(xPos, yPos);
        } else if (value == tRSRockP) {
            return new Rock(xPos, yPos);
        } else {
            return new Terrain(xPos, yPos);
        }
    }

    private Terrain getRDType(float value, float xPos, float yPos) {
        if(value == tRDDirtP) {
            return new Dirt(xPos, yPos);
        } else if (value == tRDRockP) {
            return new Rock(xPos, yPos);
        } else {
            return new Terrain(xPos, yPos);
        }
    }

    private Terrain getDSType(float value, float xPos, float yPos) {
        if(value == tDSDirtP) {
            return new Dirt(xPos, yPos);
        } else if (value == tDSSandP) {
            return new Sand(xPos, yPos);
        } else {
            return new Terrain(xPos, yPos);
        }
    }

    private float round(float value, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


}
