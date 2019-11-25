package com.wesley.main.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Shape;
import java.util.Random;

public class Tiles {

    Tile[][] _tiles;

    public int getSize() {
        return _size;
    }

    int _size;
    int _maxTiles;
    int _tilesCount;
    Random _random;

    public int getSquareSize() {
        return _squareSize;
    }

    int _squareSize;
    public Tiles(int size) {
        this._size = size;
        this._squareSize = 200;
        this._tiles = new Tile[size][size];
        this._maxTiles = this._size * this._size;
        this._tilesCount = 0;
        this._random = new Random();
    }

    public boolean createTile() {
        if (this._tilesCount >= this._maxTiles)
            return false;
        int x , y;
        do {
            x = _random.nextInt(this._tiles.length);
            y = _random.nextInt(this._tiles[0].length);
        } while (this._tiles[x][y] != null);
        this._tiles[x][y] = new Tile(2);
        this._tilesCount++;
        return true;
    }

    public boolean hasTile(int x, int y) {
        return this._tiles[x][y] != null;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, int x, int y, Vector2 position) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._tiles[x][y].drawSquare(shapeRenderer, position);
        shapeRenderer.end();
        spriteBatch.begin();
        this._tiles[x][y].writeValue(spriteBatch, position);
        spriteBatch.end();
    }
}
