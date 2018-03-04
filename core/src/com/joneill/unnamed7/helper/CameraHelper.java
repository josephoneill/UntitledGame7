package com.joneill.unnamed7.helper;

/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Modifications copyright(C) 2016 Joseph O'Neill
 ******************************************************************************/

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.joneill.unnamed7.desktop.objects.GameObject;

public class CameraHelper {
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private final float FOLLOW_SPEED_Y_AXIS = 4.0f;
    private final float FOLLOW_SPEED_X_AXIS = 4.0f;
    private boolean followObjectXAxis;
    private boolean followObjectYAxis;

    private Vector2 position;
    private float zoom;
    private GameObject target;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
        followObjectXAxis = true;
        followObjectYAxis = true;
    }

    public void update(float deltaTime) {
        if (!hasTarget())
            return;

        if (followObjectXAxis)
            position.lerp(new Vector2(target.position.x, position.y),
                    FOLLOW_SPEED_X_AXIS * deltaTime);

        if (followObjectYAxis)
            position.lerp(new Vector2(position.x, target.position.y),
                    FOLLOW_SPEED_Y_AXIS * deltaTime);

        // Prevent camera from moving down too far
        //position.y = Math.max((Constants.BLOCK_SIZE / Constants.METER_FACTOR * 2), position.y);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setPosition(Vector2 position) {
        this.position.set(position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom() {
        return zoom;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public GameObject getTarget() {
        return target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(GameObject target) {
        return hasTarget() && this.target.equals(target);
    }

    public boolean getFollowX() {
        return followObjectXAxis;
    }

    public boolean getFollowY() {
        return followObjectYAxis;
    }

    public void setFollowX(boolean followX) {
        this.followObjectXAxis = followX;
    }

    public void setFollowY(boolean followY) {
        this.followObjectYAxis = followY;
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}
