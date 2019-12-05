package com.wesley.main.gameobject;

import com.badlogic.gdx.Gdx;
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
    private DIRECTION _direction;
    private STATE _state;
    private int _tileSize;
    private int _size;
    private int _maxTiles;
    private int _tilesCount;
    private Vector2 _offset;
    private Random _random;
    private boolean _isFirstExecution;

    public Tiles(int size) {
        this._size = size;
        this._tileSize = (Gdx.graphics.getWidth() - 80) / size;
        this._tiles = new Tile[size][size];
        this._maxTiles = this._size * this._size;
        this._tilesCount = 0;
        this._state = STATE.STATIC;
        this._random = new Random();
        this._isFirstExecution = false;
    }

    public Tiles(Tile[][] tiles, DIRECTION direction, STATE state, int tileSize,
                int size, int maxTiles, int tilesCount, Vector2 offset, boolean isFirstExecution) {
        this._tiles = tiles;
        this._direction = direction;
        this._state = state;
        this._tileSize = tileSize;
        this._size = size;
        this._maxTiles = maxTiles;
        this._tilesCount = tilesCount;
        this._offset = offset;
        this._random = new Random();
        this._isFirstExecution = isFirstExecution;
    }

    public int getSize() {
        return _size;
    }

    public int getTileSize() {
        return _tileSize;
    }

    public boolean createTile() {
        if (this._tilesCount >= this._maxTiles)
            return false;
        int x , y;
        do {
            x = _random.nextInt(this._tiles.length);
            y = _random.nextInt(this._tiles[0].length);
        } while (this._tiles[x][y] != null);
        this.setTile(new Tile(this._tileSize, 2, this.getInBoardPosition(x, y)), x, y);
        this._tilesCount++;
        return true;
    }

    public boolean hasTile(int x, int y) {
        return this._tiles[x][y] != null;
    }

    private Vector2 getInBoardPosition (int x, int y) {
        int xPos = (int)this._offset.x + (this.getTileSize() * x);
        int yPos = (int)this._offset.y + (this.getTileSize() * y);
        return new Vector2(xPos, yPos);
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, int x, int y) {
        Tile tile = this._tiles[x][y];

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        tile.drawSquare(shapeRenderer, tile.getPosition());
        shapeRenderer.end();
        spriteBatch.begin();
        tile.writeValue(spriteBatch, tile.getPosition());
        spriteBatch.end();
    }

    public STATE getState() {
        return this._state;
    }

    public void moveTiles(DIRECTION direction) {
        this._direction = direction;
        this._state = STATE.MOVE;//STATE.SET_DESTINATION;
        this._isFirstExecution = true;
    }

    public void update() {
        if (this._state == STATE.MOVE) {
            this.moveTiles();
        }
    }

    private void moveTiles() {
        switch (this._direction) {
            case UP:
                this.moveUp();
                break;
            case DOWN:
                this.moveDown();
                break;
            case RIGHT:
                this.moveRight();
                break;
            case LEFT:
                this.moveLeft();
                break;
        }
    }

    private float getDelta(float position, float destination, float speed) {
        return Math.min(Math.abs(position - destination), Math.abs(speed));
    }

    private void mergeTiles(int x1, int y1, int x2, int y2) {
        this._tiles[x1][y1] = null;
        this._tiles[x2][y2].doubleValue();
        this._tilesCount--;
    }

    private void moveLeft() {
        boolean hasMoves = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = 1; x < this._size; x++) {
                hasMoves = moveTile(x, y, x-1, y, -1, 0) || hasMoves;
            }
        }
        if (!hasMoves) this._state = STATE.STATIC;
    }

    private void moveRight() {
        boolean hasMoves = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = this._size - 2; x >= 0; x--) {
                hasMoves = moveTile(x, y, x+1, y, 1, 0) || hasMoves;
            }
        }
        if (!hasMoves) this._state = STATE.STATIC;
    }

    private void moveUp() {
        boolean hasMoves = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = this._size-2; y >= 0; y--) {
                hasMoves = moveTile(x, y, x, y+1, 0,1) || hasMoves;
                /*if (this._tiles[x][y] != null) {
                    Vector2 destination = this.getInBoardPosition(x, y+1);
                    Vector2 position = this._tiles[x][y].getPosition();
                    if (position.y == destination.y) {
                        this.exchangePosition(x, y, x, y+1);
                        hasMoves = true;
                        continue;
                    }
                    if (this._tiles[x][y+1] == null) {
                        Tile tileToMove = this._tiles[x][y];
                        float deltaY = this.getDelta(tileToMove.getPosition().y, destination.y, tileToMove.getSpeed());
                        tileToMove.move(0, deltaY);
                        hasMoves = true;
                    } else {
                        if (this._tiles[x][y].match(this._tiles[x][y-1])) {
                            this._tiles[x][y] = null;
                            this._tiles[x][y-1].doubleValue();
                            hasMoves = true;
                        }
                    }
                }*/
            }
        }
        if (!hasMoves) this._state = STATE.STATIC;
    }

    private void moveDown() {
        boolean hasMoves = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = 1; y < this._size; y++) {
                hasMoves = moveTile(x, y, x, y-1, 0, -1) || hasMoves;
            }
        }
        if (!hasMoves) this._state = STATE.STATIC;
    }

    private boolean moveTile(int x, int y, int targetX, int targetY, int deltaXMultiplier, int deltaYMultiplier) {
        boolean hasMoves = false;
        if (this._tiles[x][y] != null) {
            Vector2 destination = this.getInBoardPosition(targetX, targetY);
            Vector2 position = this._tiles[x][y].getPosition();
            if ((x != targetX && position.x == destination.x)
                    || (y != targetY && position.y == destination.y)) {
                this.exchangePosition(x, y, targetX, targetY);
                return true;
            }
            if (this._tiles[targetX][targetY] == null) {
                Tile tileToMove = this._tiles[x][y];
                float deltaX = this.getDelta(tileToMove.getPosition().x, destination.x, tileToMove.getSpeed()) * deltaXMultiplier;
                float deltaY = this.getDelta(tileToMove.getPosition().y, destination.y, tileToMove.getSpeed()) * deltaYMultiplier;
                tileToMove.move(deltaX, deltaY);
                hasMoves = true;
            } else {
                if (this._tiles[x][y].match(this._tiles[targetX][targetY])) {
                    this.mergeTiles(x, y, targetX, targetY);
                    hasMoves = true;
                }
            }
        }
        return hasMoves;
    }

    private void exchangePosition(int x1, int y1, int x2, int y2) {
        Tile element1 = this._tiles[x1][y1];
        Tile element2 = this._tiles[x2][y2];
        this.setTile(element2, x1, y1);
        this.setTile(element1, x2, y2);
    }

    public void setOffset(Vector2 offset) {
        this._offset = offset;
    }

    private void setTile(Tile tile, int x, int y) {
        if (tile != null) tile.set(x, y);
        this._tiles[x][y] = tile;
    }

    public Tile getTile(int x, int y) { return this._tiles[x][y]; }

    public int getMaxTiles() { return this._maxTiles; }

    public int getTilesCount() { return this._tilesCount; }

    public Vector2 getOffset() { return this._offset; }

    public boolean isFirstExecution() { return  this._isFirstExecution; }

    public void dispose() {
        for (int i = 0; i < this.getSize(); i++){
            for (int j = 0; j < this.getSize(); j++) {
                if (this._tiles[i][j] != null) {
                    this._tiles[i][j].dispose();
                }
            }
        }
    }

    public Tiles clone() {
        Tile[][] tiles = new Tile[this._size][this._size];

        for (int x = 0; x < this.getSize(); x++){
            for (int y = 0; y < this.getSize(); y++) {
                if (this._tiles[x][y] != null) {
                    tiles[x][y] = this._tiles[x][y].clone();
                } else
                    tiles[x][y] = null;
            }
        }

        return new Tiles(tiles, this._direction, this._state,
                         this._tileSize, this._size, this._maxTiles, this._tilesCount,
                         new Vector2(this._offset), this._isFirstExecution);
    }
}
