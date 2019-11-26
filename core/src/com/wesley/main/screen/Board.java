package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;
import com.wesley.main.gameobject.Tiles;

import java.util.Random;
import java.util.Vector;

public class Board {

    private ShapeRenderer _shapeRenderer;
    private SpriteBatch _spriteBatch;
    private Tiles _tiles;
    private int _size;
    private int _firstX;
    private int _firstY;
    private boolean _playerTurn;
    private BitmapFont _font;
    float deltaX=0;
    float deltaY =0;

    public Board(int size) {
        this._tiles = new Tiles(size);
        this._size = size * this._tiles.getSquareSize();
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (this._size / 2);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (this._size / 2);
        this._tiles.setOffset(new Vector2(this._firstX, this._firstY));
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
        this._playerTurn = false;
        this._tiles.createTile();
        this._tiles.createTile();
        this._playerTurn = true;

        this._font = new BitmapFont();
        this._font.setColor(Color.WHITE);
        this._font.getData().setScale(5);
    }

    public void update() {

        if (this._tiles.getState() != Tiles.STATE.STATIC) {
            this._tiles.update();
            this._playerTurn = false;
        } else {
            if (this._playerTurn) {
                this.handleInput();
            } else {
                this._tiles.createTile();
                this._playerTurn = true;
            }
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            deltaX = Gdx.input.getDeltaX();
            deltaY = Gdx.input.getDeltaY();
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    this._tiles.moveTiles(Tiles.DIRECTION.RIGHT);
                } else {
                    this._tiles.moveTiles(Tiles.DIRECTION.LEFT);
                }
            } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                if (deltaY > 0) {
                    this._tiles.moveTiles(Tiles.DIRECTION.DOWN);
                } else {
                    this._tiles.moveTiles(Tiles.DIRECTION.UP);
                }
            }
        }
    }

    public void draw() {
        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._shapeRenderer.setColor(1,1,1,1);
        this._shapeRenderer.rect(this._firstX, this._firstY, this._size, this._size);
        this._shapeRenderer.end();
        for (int i = 0; i < this._tiles.getSize(); i++){
            for (int j = 0; j < this._tiles.getSize(); j++) {
                int x = this._firstX + (this._tiles.getSquareSize() * i);
                int y = this._firstY + (this._tiles.getSquareSize() * j);
                if (this._tiles.hasTile(i, j)) {
                    this._tiles.draw(this._shapeRenderer, this._spriteBatch, i, j);
                }
                this.drawBGRect(x, y);
            }
        }
        String text = deltaX + "," + deltaY;
        this._spriteBatch.begin();
        this._font.draw(this._spriteBatch, text,100,100);
        this._spriteBatch.end();
    }

    private void drawBGRect(int x, int y) {
        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float borderWeight = 5f;
        Vector2 bottomLeft = new Vector2(x, y);
        Vector2 topLeft = new Vector2(x, y + this._tiles.getSquareSize());
        Vector2 topRight = new Vector2(x + this._tiles.getSquareSize(), y + this._tiles.getSquareSize());
        Vector2 bottomRight = new Vector2(x + this._tiles.getSquareSize(), y);

        this._shapeRenderer.setColor(0,0,0,1);

        this._shapeRenderer.rectLine(bottomLeft, topLeft, borderWeight);
        this._shapeRenderer.rectLine(topLeft, topRight, borderWeight);
        this._shapeRenderer.rectLine(topRight, bottomRight, borderWeight);
        this._shapeRenderer.rectLine(bottomRight, bottomLeft, borderWeight);
        this._shapeRenderer.end();
    }

    public void dispose() {
        this._spriteBatch.dispose();
        this._shapeRenderer.dispose();
    }
}
