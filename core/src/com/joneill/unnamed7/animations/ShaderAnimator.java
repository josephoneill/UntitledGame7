package com.joneill.unnamed7.animations;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.ArrayList;

/**
 * Created by josep_000 on 5/22/2016.
 * Use: Switch between an array of shaders to create
 *      an animation effect
 */

public class ShaderAnimator {
    public float timer;
    public float duration;
    public boolean isActive;
    public ShaderProgram currShaderProgram;
    public ArrayList<AnimationStep> animationsList;
    private int currAnimIndex;

    public ShaderAnimator() {
        animationsList = new ArrayList<AnimationStep>();
        duration = 0;
        currAnimIndex = -1;
        currShaderProgram = null;
    }

    public void update(float deltaTime) {
        if(isActive) {
            timer += deltaTime;

            if(currAnimIndex != animationsList.size() - 1
                    && timer > animationsList.get(currAnimIndex + 1).getKeystone()
                    && timer <= duration) {
                currShaderProgram = animationsList.get(currAnimIndex + 1).getProgram();
                currAnimIndex += 1;
            }

            if(timer > duration) {
                if(currAnimIndex != animationsList.size() - 1) {
                    currShaderProgram = animationsList.get(currAnimIndex + 1).getProgram();
                }
                reset(false);
            }
        }
    }

    public void start() {
        isActive = true;
    }

    public void stop() {
        isActive = false;
    }

    public void reset(boolean run) {
        timer = 0;
        currAnimIndex = -1;
        isActive = run;
    }

    public void addAnimationStep(ShaderProgram program, float keystone) {
        AnimationStep animation = new AnimationStep(program, keystone);
        animationsList.add(animation);
        sortAnimations(animationsList);
    }

    private void sortAnimations(ArrayList<AnimationStep> array) {
        AnimationStep temp;

        //Bubblesort the animations in ascending order
        //based on their keyframe time
        for(int i = 0; i < array.size() - 1; i++) {
            for(int j = 1; j < array.size() - i; j++) {
                if(array.get(j - 1).getKeystone() > array.get(j).getKeystone()) {
                    temp = array.get(j - 1);
                    array.set(j - 1, array.get(j));
                    array.set(j, temp);
                }
            }
        }

        /*for(AnimationStep step : array) {
            System.out.print(step.getKeystone() + ", ");
        }
        System.out.println("");*/
    }

    public ShaderProgram getShader() {
        return currShaderProgram;
    }

    public class AnimationStep {
        public ShaderProgram program;
        public float keystone;
        public AnimationStep(ShaderProgram program, float keystone) {
            this.program = program;
            this.keystone = keystone;
        }

        public ShaderProgram getProgram() {
            return program;
        }

        public void setProgram(ShaderProgram program) {
            this.program = program;
        }

        public float getKeystone() {
            return keystone;
        }

        public void setKeystone(float keystone) {
            this.keystone = keystone;
        }
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    //This will take any static animation
    //and set it to whatever duration passed in
    public void adjustDuration(float newDuration) {
        float fTimeAdjust = newDuration / duration;
        for(AnimationStep anim : animationsList) {
            anim.setKeystone(anim.getKeystone() * fTimeAdjust);
        }
        setDuration(newDuration);
    }

    public boolean isActive() {
        return isActive;
    }
}
