package com.wesley.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen {

    protected ShapeRenderer _shapeRenderer;
    protected SpriteBatch _spriteBatch;

    public Screen() {
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
    }

    public abstract Screen update();
    public abstract void draw();
    public void dispose() {
        this._shapeRenderer.dispose();
        this._spriteBatch.dispose();
    }
}
