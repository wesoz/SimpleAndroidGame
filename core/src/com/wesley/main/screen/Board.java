package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;

public class Board {

    Tile[][] _tiles;
    boolean _isMoving;
    int _size;
    int _firstX;
    int _firstY;

    public Board(Tile[][] tiles) {
        this._tiles = tiles;
        this._isMoving = false;
        this._size = tiles.length * 200;
        this._firstX = Gdx.app.getGraphics().getWidth() - (this._size / 2);
        this._firstY = Gdx.app.getGraphics().getHeight() - (this._size / 2);
    }

    public void update() {

        if (!this._isMoving) {
            if (Gdx.input.isTouched()) {

            }
        } else {

        }
    }

    public void draw() {
        for (int i = 0; i < this._tiles.length; i++){
            for (int j = 0; j < this._tiles[i].length; j++) {
                if (this._tiles[i][j] == null) {
                    Tile emptyTile = new Tile(new Vector2(this._firstX * i, this._firstY * j));
                    emptyTile.draw();
                }
            }
        }
    }
}
