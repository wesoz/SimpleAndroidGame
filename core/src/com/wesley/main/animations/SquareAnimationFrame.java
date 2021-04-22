package com.wesley.main.animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SquareAnimationFrame extends AnimationFrame {
    private final Color _color;
    private final Vector2 _offset;
    private final float _size;

    public SquareAnimationFrame(Vector2 offset, Color color, float size, float duration) {
        super(duration);
        this._offset = offset;
        this._color = color;
        this._size = size;
    }

    public Color getColor() {
        return this._color;
    }

    public float getSize() {
        return this._size;
    }

    public Vector2 getOffset() {
        return this._offset;
    }
}