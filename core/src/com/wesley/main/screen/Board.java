package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.wesley.main.gameobject.Tile;

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

    public Board(Tile[][] tiles) {
        this._tiles = tiles;
        this._isMoving = false;
        this._size = tiles.length * 200;
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (400);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (400);
        this._background = new Texture("board_4x4.bmp");
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
        this._innerSquareSize = this._squareSize - this._squareBorderSize;
    }

    public void update() {

        if (!this._isMoving) {
            if (Gdx.input.isTouched()) {

            }
        } else {

        }
    }

    public void draw() {
        //this._spriteBatch.begin();
        //this._spriteBatch.draw(this._background, this._firstX, this._firstY);

        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < this._tiles.length; i++){
            for (int j = 0; j < this._tiles[i].length; j++) {
                if (this._tiles[i][j] == null) {
                    int x = this._firstX + (_squareSize * i);
                    int y = this._firstY + (_squareSize * j);
                    this._shapeRenderer.setColor(0,0,0,1);
                    this._shapeRenderer.rect(x, y, _squareSize, _squareSize);
                    this._shapeRenderer.setColor(1,1,1,1);
                    this._shapeRenderer.rect((this._firstX + (_innerSquareSize * i)) + (_squareBorderSize / 2), (this._firstY + (_innerSquareSize * i)) + (_squareBorderSize / 2), _innerSquareSize, _innerSquareSize);
                }
            }
        }
        this._shapeRenderer.end();
        //this._spriteBatch.end();
    }

    public void dispose() {
        this._spriteBatch.dispose();
        this._shapeRenderer.dispose();
    }
}
