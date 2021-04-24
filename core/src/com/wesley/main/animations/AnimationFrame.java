package com.wesley.main.animations;

public abstract class AnimationFrame {
    protected float _duration = 0;

    public AnimationFrame(float duration) {
        this._duration = duration;
    }

    float getDuration() {
        return this._duration;
    }
}
