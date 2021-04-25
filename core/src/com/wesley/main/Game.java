package com.wesley.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wesley.main.screen.MainMenu;
import com.wesley.main.screen.Screen;

public class Game extends ApplicationAdapter{
	private SpriteBatch _batch;
	private BitmapFont _font;
	long _startTime;
	long _frames = 0;
	Screen _currentScreen;
	
	@Override
	public void create () {
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		this._batch = new SpriteBatch();
		this._font = new BitmapFont();
		this._font.setColor(Color.WHITE);
		this._font.getData().setScale(5);
		this._startTime = System.currentTimeMillis();
		this._currentScreen = new MainMenu();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.47f,0.56f,0.61f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Screen nextScreen = this._currentScreen.update();
		this._currentScreen.draw();
		if (nextScreen != this._currentScreen)
		{
			this._currentScreen.dispose();
			this._currentScreen = nextScreen;
		}
		if (false) {
			this.getFrameRate();
		}
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
	
	@Override
	public void dispose () {
		this._batch.dispose();
		this._font.dispose();
		this._currentScreen.dispose();
	}
}
