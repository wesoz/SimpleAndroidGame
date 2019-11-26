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

import java.util.Date;

public class Game extends ApplicationAdapter {
	private SpriteBatch _batch;
	private ShapeRenderer _renderer;
	private Texture _img;
	private Vector2 _mousePosition;
	private BitmapFont _font;
	private float _mouseX = 0;
	private float _mouseY = 0;
	private Board _board;
	long _startTime;
	long _frames = 0;
	
	@Override
	public void create () {
		this._renderer = new ShapeRenderer();
		this._batch = new SpriteBatch();
		this._img = new Texture("badlogic.jpg");
		this._mousePosition = new Vector2(Gdx.app.getGraphics().getWidth()/2, Gdx.app.getGraphics().getHeight() / 2);
		this._font = new BitmapFont();
		this._font.setColor(Color.WHITE);
		this._font.getData().setScale(5);
		this._board = new Board(4);
		this._startTime = System.currentTimeMillis();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.4f, 0.6f, 0.8f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this._board.update();
		this._board.draw();
		this.getFrameRate();
	}

	private void getFrameRate() {
		long elapsedTime = (System.currentTimeMillis() - this._startTime) / 1000;
		_frames++;

		long FPS = elapsedTime == 0 ? 0 : _frames / elapsedTime;
		String text = FPS + " FPS";

		this._batch.begin();
		this._font.draw(this._batch,text,500,100);
		this._batch.end();
	}

	private void getTouchMove() {
		if (Gdx.input.isTouched()) {
			this._mouseX = Gdx.input.getDeltaX();
			this._mouseY = Gdx.input.getDeltaY();
			this._mousePosition = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
		}
		String text = _mouseX + "," + _mouseY;

		this._batch.begin();
		this._font.draw(this._batch,text,100,100);
		this._batch.end();
	}
	
	@Override
	public void dispose () {
		this._batch.dispose();
		this._img.dispose();
		this._font.dispose();
		this._board.dispose();
	}
}
