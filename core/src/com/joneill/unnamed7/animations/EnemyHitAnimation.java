package com.joneill.unnamed7.animations;

import com.joneill.unnamed7.helper.Assets;

/**
 * Created by josep_000 on 5/22/2016.
 */
public class EnemyHitAnimation extends ShaderAnimator {

    public EnemyHitAnimation() {
        super();
        setDuration(1.0f);
        createAnim();
    }

    private void createAnim() {
        addAnimationStep(Assets.getInstance().getAssetsShader().geteHitShader(), 0.0f);
        addAnimationStep(null, 0.5f);
    }

}
