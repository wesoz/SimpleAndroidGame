package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;
import com.wesley.main.gameobject.Tiles;

import java.util.Random;
import java.util.Vector;

public class Board {

    ShapeRenderer _shapeRenderer;
    SpriteBatch _spriteBatch;
    Tiles _tiles;
    boolean _isMoving;
    int _size;
    int _firstX;
    int _firstY;
    boolean _playerTurn;

    public Board(int size) {
        this._tiles = new Tiles(size);
        this._isMoving = false;
        this._size = size * this._tiles.getSquareSize();
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (this._size / 2);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (this._size / 2);
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
        this._playerTurn = false;
        this._tiles.createTile();
        this._tiles.createTile();
        this._playerTurn = true;
    }

    public void update() {

        if (!this._isMoving) {
            if (this._playerTurn) {
                if (Gdx.input.isTouched()) {

                }
            } else {
                this._tiles.createTile();
                this._playerTurn = true;
            }
        } else {

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
                    this._tiles.draw(this._shapeRenderer, this._spriteBatch, i, j, new Vector2(x, y));
                }
                this.drawBGRect(x, y);
            }
        }
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
