package com.wesley.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Test extends ApplicationAdapter {
    SpriteBatch _batch;
    Texture _img;
    Vector2 _mousePosition;
    ShapeRenderer _shapeRenderer;
    BitmapFont _font;
    float _mouseX = 0;
    float _mouseY = 0;

    @Override
    public void create() {
        this._batch = new SpriteBatch();
        this._img = new Texture("badlogic.jpg");
        this._mousePosition = new Vector2(Gdx.app.getGraphics().getWidth() / 2, Gdx.app.getGraphics().getHeight() / 2);
        this._shapeRenderer = new ShapeRenderer();
        this._font = new BitmapFont();
        this._font.setColor(Color.WHITE);
        this._font.getData().setScale(5);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            this._mouseX = Gdx.input.getDeltaX();
            this._mouseY = Gdx.input.getDeltaY();
            this._mousePosition = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
        }
        float imgOffsetX = _img.getWidth() / 2;
        float imgOffsetY = _img.getHeight() / 2;
        String text = _mouseX + "," + _mouseY;
        this._batch.begin();

        this._batch.draw(_img, this._mousePosition.x - imgOffsetX, this._mousePosition.y - imgOffsetY);
        //this._font.draw(this._batch,text,100,100);
        this._batch.end();
        //this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		/*this._shapeRenderer.setColor(1, 0, 0, 1);
		this._shapeRenderer.circle(this._mousePosition.x, this._mousePosition.y, 60);
		this._shapeRenderer.setColor(0, 1, 0, 1);
		this._shapeRenderer.circle(this._mousePosition.x, this._mousePosition.y, 30);*/
        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._shapeRenderer.setColor(Color.BLACK);
        this._shapeRenderer.rect(this._mousePosition.x - imgOffsetX, this._mousePosition.y - imgOffsetY, 200, 200);

        this._shapeRenderer.setColor(Color.YELLOW);
        this._shapeRenderer.rect(this._mousePosition.x - 85, this._mousePosition.y - 85, 170, 170);
        this._shapeRenderer.end();

    }

    @Override
    public void dispose() {
        this._batch.dispose();
        this._img.dispose();
        this._shapeRenderer.dispose();
        this._font.dispose();
    }
}