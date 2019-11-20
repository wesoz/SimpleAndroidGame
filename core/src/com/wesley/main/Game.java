package com.wesley.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.logging.Logger;

public class Game extends ApplicationAdapter {
	SpriteBatch _batch;
	Texture _img;
	Vector2 _mousePosition;
	ShapeRenderer _shapeRenderer;
	
	@Override
	public void create () {
		this._batch = new SpriteBatch();
		this._img = new Texture("badlogic.jpg");
		this._mousePosition = new Vector2(Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight() / 2);
		this._shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isTouched()) {
			this._mousePosition = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
		}
		this._batch.begin();
		float imgOffsetX = _img.getWidth() / 2;
		float imgOffsetY = _img.getHeight() / 2;
		this._batch.draw(_img, this._mousePosition.x - imgOffsetX, this._mousePosition.y - imgOffsetY);
		this._batch.end();
		this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		this._shapeRenderer.setColor(1, 0, 0, 1);
		this._shapeRenderer.circle(this._mousePosition.x, this._mousePosition.y, 30);
		this._shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		this._batch.dispose();
		this._img.dispose();
		this._shapeRenderer.dispose();
	}
}
