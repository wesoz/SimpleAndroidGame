package com.wesley.main.animations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animation {

    public enum REPEAT {
        NONE,
        ONCE,
        LOOP,
        GO_BACK
    }

    private List<AnimationFrame> _frames;
    private REPEAT _repeat;
    private int _currentFrame;
    private int _increment;
    private float _lastElapsedTime;

    public Animation() {
        this._frames = new ArrayList<>();
        this.initialize();
    }

    public Animation(AnimationFrame[] frames, REPEAT repeat) {
        this();
        this.addFrames(frames, repeat);
    }

    private void initialize() {
        this._currentFrame = 0;
        this._lastElapsedTime = -1;
        this._increment = 1;
    }

    private void clearValues () {
        this._frames.clear();
        this.initialize();
    }

    public void replaceFrames(AnimationFrame[] frames, REPEAT repeat) {
        this.clearValues();
        this.addFrames(frames, repeat);
    }

    public void addFrames(AnimationFrame[] frames, REPEAT repeat) {
        this._frames.addAll(Arrays.asList(frames));
        this._repeat = repeat;
    }

    public AnimationFrame getNextFrame(float elapsedTime) {
        if (this._frames.size() == 0) {
            return null;
        }
        boolean isOver = (this._increment > 0 && this._currentFrame == this._frames.size() - 1) || (this._increment < 0 && this._currentFrame == 0);
        if (this._repeat == REPEAT.ONCE && isOver) {
            return null;
        }

        AnimationFrame frame = this._frames.get(_currentFrame);
        if (this._lastElapsedTime == -1) {
            this._lastElapsedTime = elapsedTime;
        }

        if (elapsedTime - this._lastElapsedTime >= frame.getDuration()) {
            this._lastElapsedTime = elapsedTime;
            if (!isOver) {
                this._currentFrame += this._increment;
            } else if (this._repeat == REPEAT.LOOP) {
                this._currentFrame = 0;
            } else if (this._repeat == REPEAT.GO_BACK) {
                this._increment *= -1;
            }
        }

        return frame;
    }
}