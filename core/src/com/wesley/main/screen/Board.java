package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;

import java.util.Random;

public class Board {

    ShapeRenderer _shapeRenderer;
    SpriteBatch _spriteBatch;
    Texture _background;
    Tile[][] _tiles;
    boolean _isMoving;
    int _size;
    int _firstX;
    int _firstY;
    int _squareSize = 200;
    int _squareBorderSize = 20;
    int _innerSquareSize;
    boolean _playerTurn;
    Random _random;

    public Board(int size) {
        this._tiles = new Tile[size][size];
        this._isMoving = false;
        this._size = size * 200;
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (400);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (400);
        this._background = new Texture("board_4x4.bmp");
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
        this._innerSquareSize = this._squareSize - this._squareBorderSize;
        this._playerTurn = false;
        this._random = new Random();
    }

    public void update() {

        if (!this._isMoving) {
            if (this._playerTurn) {
                if (Gdx.input.isTouched()) {

                }
            } else {
                int x;
                int y;
                do {
                    x = _random.nextInt(this._tiles.length);
                    y = _random.nextInt(this._tiles[0].length);
                } while (this._tiles[x][y] != null);
                this._tiles[x][y] = new Tile(2);
                this._playerTurn = true;
            }
        } else {

        }
    }

    public void draw() {
        this._spriteBatch.begin();
        //this._spriteBatch.draw(this._background, this._firstX, this._firstY);

        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._shapeRenderer.setColor(1,1,1,1);
        this._shapeRenderer.rect(this._firstX, this._firstY, this._size, this._size);
        this._shapeRenderer.end();
        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < this._tiles.length; i++){
            for (int j = 0; j < this._tiles[i].length; j++) {
                int x = this._firstX + (_squareSize * i);
                int y = this._firstY + (_squareSize * j);
                if (this._tiles[i][j] == null) {
                    //this._shapeRenderer.setColor(0,0,0,1);
                    //this._shapeRenderer.rect(x, y, _squareSize, _squareSize);
                } else {
                    this._tiles[i][j].drawSquare(this._shapeRenderer, new Vector2(x, y));
                    this._tiles[i][j].writeValue(this._spriteBatch, new Vector2(x, y));
                }
            }
        }
        this._shapeRenderer.end();
        this._spriteBatch.end();
    }

    public void dispose() {
        this._spriteBatch.dispose();
        this._shapeRenderer.dispose();
    }
}
