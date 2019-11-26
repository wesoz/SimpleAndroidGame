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
        this._tiles[x][y] = new Tile(2, this.getInBoardPosition(x, y));
        this._tilesCount++;
        return true;
    }

    public boolean hasTile(int x, int y) {
        return this._tiles[x][y] != null;
    }

    private Vector2 getInBoardPosition (int x, int y) {
        int xPos = (int)this._offset.x + (this.getSquareSize() * x);
        int yPos = (int)this._offset.y + (this.getSquareSize() * y);
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

        this._state = STATE.SET_DESTINATION;
    }

    public void update() {
        if (this._state == STATE.SET_DESTINATION) {
            switch (this._direction) {
                case UP:
                    this.setDestinationUp();
                    break;
                case DOWN:
                    this.setDestinationDown();
                    break;
                case RIGHT:
                    this.setDestinationRight();
                    break;
                case LEFT:
                    this.setDestinationLeft();
                    break;
            }
            this._state = STATE.MOVE;
        }
        else if (this._state == STATE.MOVE) {
            this.moveTiles();
        }
    }

    private void moveTiles() {
        boolean hasMoves = false;
        for (int i = 0; i < this._tilesToMove.size(); i++ ) {
            Tile tile = this._tilesToMove.get(i);
            float deltaX = 0;
            float deltaY = 0;
            if (tile.getPosition().y > tile.getDestination().y) {
                deltaY =-tile.getSpeed();
                hasMoves = true;
            } else if (tile.getPosition().y < tile.getDestination().y) {
                deltaY = tile.getSpeed();
                hasMoves = true;
            } else if (tile.getPosition().x < tile.getDestination().x) {
                deltaX = tile.getSpeed();
                hasMoves = true;
            } else if (tile.getPosition().x > tile.getDestination().x) {
                deltaX = -tile.getSpeed();
                hasMoves = true;
            }

            tile.move(deltaX, deltaY);
        }
        if (!hasMoves) {
            this._tilesToMove.clear();
            this._state = STATE.STATIC;
        }
    }

    private void setDestinationLeft() {
        for (int y = 0; y < this._size; y++) {
            Vector2 destination = null;
            for (int x = 0; x < this._size; x++) {
                if (this._tiles[x][y] == null) {
                    if (destination == null) {
                        destination = new Vector2(x, y);
                    }
                } else {
                    if (destination != null) {
                        this._tiles[x][y].setDestination(this.getInBoardPosition((int)destination.x, (int)destination.y));
                        this._tilesToMove.add(this._tiles[x][y]);
                        this.exchangePosition(x, y, (int)destination.x, (int)destination.y);
                        destination.set(destination.x + 1, destination.y);
                    }
                }
            }
        }
    }

    private void setDestinationRight() {
        for (int y = 0; y < this._size; y++) {
            Vector2 destination = null;
            for (int x = this._size - 1; x >= 0; x--) {
                if (this._tiles[x][y] == null) {
                    if (destination == null) {
                        destination = new Vector2(x, y);
                    }
                } else {
                    if (destination != null) {
                        this._tiles[x][y].setDestination(this.getInBoardPosition((int)destination.x, (int)destination.y));
                        this._tilesToMove.add(this._tiles[x][y]);
                        this.exchangePosition(x, y, (int)destination.x, (int)destination.y);
                        destination.set(destination.x - 1, destination.y);
                    }
                }
            }
        }
    }

    private void setDestinationUp() {
        for (int x = 0; x < this._size; x++) {
            Vector2 destination = null;
            for (int y = this._size - 1; y >= 0; y--) {
                if (this._tiles[x][y] == null) {
                    if (destination == null) {
                        destination = new Vector2(x, y);
                    }
                } else {
                    if (destination != null) {
                        this._tiles[x][y].setDestination(this.getInBoardPosition((int)destination.x, (int)destination.y));
                        this._tilesToMove.add(this._tiles[x][y]);
                        this.exchangePosition(x, y, (int)destination.x, (int)destination.y);
                        destination.set(destination.x, destination.y - 1);
                    }
                }
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
                        this._tiles[x][y].setDestination(this.getInBoardPosition((int)destination.x, (int)destination.y));
                        this._tilesToMove.add(this._tiles[x][y]);
                        this.exchangePosition(x, y, (int)destination.x, (int)destination.y);
                        destination.set(destination.x, destination.y + 1);
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
