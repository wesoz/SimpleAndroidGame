package com.wesley.main.gameobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tiles {

    public enum DIRECTION {
        UP,
        DOWN,
        RIGHT,
        LEFT,
    }
    public enum STATE {
        MOVE,
        SET_DESTINATION,
        STATIC
    }

    private Tile[][] _tiles;
    private List<Tile> _tilesToMove;
    private DIRECTION _direction;
    private STATE _state;
    private int _squareSize;
    private int _size;
    private int _maxTiles;
    private int _tilesCount;
    private Vector2 _offset;
    private Random _random;

    public Tiles(int size) {
        this._size = size;
        this._squareSize = 200;
        this._tiles = new Tile[size][size];
        this._maxTiles = this._size * this._size;
        this._tilesCount = 0;
        this._state = STATE.STATIC;
        this._random = new Random();
        this._tilesToMove = new ArrayList<>();
    }

    public int getSize() {
        return _size;
    }

    public int getSquareSize() {
        return _squareSize;
    }

    public boolean createTile() {
        if (this._tilesCount >= this._maxTiles)
            return false;
        int x , y;
        do {
            x = _random.nextInt(this._tiles.length);
            y = _random.nextInt(this._tiles[0].length);
        } while (this._tiles[x][y] != null);
        this._tiles[x][y] = new Tile(2, new Vector2(x, y));
        this._tilesCount++;
        return true;
    }

    public boolean hasTile(int x, int y) {
        return this._tiles[x][y] != null;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, int x, int y) {
        Tile tile = this._tiles[x][y];
        int xPos = (int)this._offset.x + (this.getSquareSize() * (int)tile.getPosition().x);
        int yPos = (int)this._offset.y + (this.getSquareSize() * (int)tile.getPosition().y);

        Vector2 position = new Vector2(xPos, yPos);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        tile.drawSquare(shapeRenderer, position);
        shapeRenderer.end();
        spriteBatch.begin();
        tile.writeValue(spriteBatch, position);
        spriteBatch.end();
    }

    public STATE getState() {
        return this._state;
    }

    public void moveTiles(DIRECTION direction) {
        this._direction = direction;

        this._state = STATE.SET_DESTINATION;
    }

    public void update() {
        if (this._state == STATE.SET_DESTINATION) {
            switch (this._direction) {
                case UP:
                    break;
                case DOWN:
                    this.setDestinationDown();
                    break;
                case RIGHT:
                    break;
                case LEFT:
                    break;
            }
            this._state = STATE.MOVE;
        }
        else if (this._state == STATE.MOVE) {
            boolean hasMoves = false;
            for (int i = 0; i < this._tilesToMove.size(); i++ ) {
                Tile tile = this._tilesToMove.get(i);
                if (tile.getPosition().y > tile.getDestination().y) {
                    tile.move(0, -tile.getSpeed());
                    hasMoves = true;
                }
            }
            if (!hasMoves) {
                this._tilesToMove.clear();
                this._state = STATE.STATIC;
            }
        }
    }

    private void setDestinationDown() {
        for (int x = 0; x < this._size; x++) {
            Vector2 destination = null;
            for (int y = 0; y < this._size; y++) {
                if (this._tiles[x][y] == null) {
                    if (destination == null) {
                        destination = new Vector2(x, y);
                    }
                } else {
                    if (destination != null) {
                        this._tiles[x][y].setDestination(destination);
                        this._tilesToMove.add(this._tiles[x][y]);
                        this.exchangePosition(x, y, (int)destination.x, (int)destination.y);
                        break;
                    }
                }
            }
        }
    }

    private void exchangePosition(int x1, int y1, int x2, int y2) {
        Tile element1 = this._tiles[x1][y1];
        Tile element2 = this._tiles[x2][y2];
        this._tiles[x1][y1] = element2;
        this._tiles[x2][y2] = element1;
    }

    public Vector2 getOffset() {
        return _offset;
    }

    public void setOffset(Vector2 offset) {
        this._offset = offset;
    }
}
