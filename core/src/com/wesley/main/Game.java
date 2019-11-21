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
import com.wesley.main.gameobject.Tile;
import com.wesley.main.screen.Board;

public class Game extends ApplicationAdapter {
	SpriteBatch _batch;
	ShapeRenderer _renderer;
	Texture _img;
	Vector2 _mousePosition;
	BitmapFont _font;
	float _mouseX = 0;
	float _mouseY = 0;
	Tile _tile;
	Board _board;
	
	@Override
	public void create () {
		this._renderer = new ShapeRenderer();
		this._batch = new SpriteBatch();
		this._img = new Texture("badlogic.jpg");
		this._mousePosition = new Vector2(Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight() / 2);
		this._font = new BitmapFont();
		this._font.setColor(Color.WHITE);
		this._font.getData().setScale(5);
		this._tile = new Tile(2, new Vector2(Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight() / 2));
		this._board = new Board(new Tile[4][4]);
	}

	@Override
	public void render () {
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
		this._tile.setPosition(this._mousePosition);
		this._board.draw();

		this._renderer.begin(ShapeRenderer.ShapeType.Filled);
		this._tile.drawSquare(this._renderer);
		this._renderer.end();

		this._batch.begin();
		this._tile.writeValue(this._batch);
		this._font.draw(this._batch,text,100,100);
		this._batch.end();


	}
	
	@Override
	public void dispose () {
		this._batch.dispose();
		this._img.dispose();
		this._font.dispose();
		this._tile.dispose();
		this._board.dispose();
	}
}
