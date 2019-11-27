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
    private List<Tile> _tilesToMove;
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
        this._tilesToMove = new ArrayList<>();
        this._isFirstExecution = false;
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
        this._state = STATE.SET_DESTINATION;
        this._isFirstExecution = true;
    }

    public void update() {
        if (this._state == STATE.SET_DESTINATION) {
            setDestination();
        }
        else if (this._state == STATE.MOVE) {
            this.moveTiles();
        }
    }

    private void setDestination() {
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

    private void moveTiles() {
        boolean hasMoves = false;
        for (int i = 0; i < this._tilesToMove.size(); i++ ) {
            Tile tile = this._tilesToMove.get(i);
            float deltaX = 0;
            float deltaY = 0;
            int x = tile.getX();
            int y = tile.getY();
            if (tile.getPosition().y > tile.getDestination().y) { //down
                deltaY = -this.getDelta(tile.getPosition().y, tile.getDestination().y, -tile.getSpeed());
                hasMoves = true;
            } else if (tile.getPosition().y < tile.getDestination().y) { //up
                deltaY = this.getDelta(tile.getPosition().y, tile.getDestination().y, -tile.getSpeed());
                hasMoves = true;
            } else if (tile.getPosition().x < tile.getDestination().x) { //right
                deltaX = this.getDelta(tile.getPosition().x, tile.getDestination().x, tile.getSpeed());
                hasMoves = true;
            } else if (tile.getPosition().x > tile.getDestination().x) { //left
                deltaX = -this.getDelta(tile.getPosition().x, tile.getDestination().x, tile.getSpeed());
                hasMoves = true;
            }

            tile.move(deltaX, deltaY);
        }
        if (!hasMoves) {
            this._tilesToMove.clear();
            this.checkMatches();
        }
    }

    private float getDelta(float position, float destination, float speed) {
        return Math.min(Math.abs(position - destination), Math.abs(speed));
    }

    private void checkMatches() {
        boolean hasMatches = false;
        if (this._isFirstExecution) {
            this._isFirstExecution = false;
            switch (this._direction) {
                case UP:
                    hasMatches = this.checkMatchesUp();
                    break;
                case DOWN:
                    hasMatches = this.checkMatchesDown();
                    break;
                case RIGHT:
                    hasMatches = this.checkMatchesRight();
                    break;
                case LEFT:
                    hasMatches = this.checkMatchesLeft();
                    break;
            }
        }
        if (hasMatches)
            this._state = STATE.SET_DESTINATION;
        else
            this._state = STATE.STATIC;

    }

    private boolean checkMatchesDown () {
        boolean hasMatches = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = 0; y < this._size - 1; y++) {
                if (this._tiles[x][y] != null) {
                    if (this._tiles[x][y].match(this._tiles[x][y+1])) {
                        this.mergeTiles(x, y, x, y+1);
                        hasMatches = true;
                    }
                }
            }
        }
        return hasMatches;
    }

    private boolean checkMatchesUp () {
        boolean hasMatches = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = this._size - 1; y > 0; y--) {
                if (this._tiles[x][y] != null) {
                    if (this._tiles[x][y].match(this._tiles[x][y-1])) {
                        this.mergeTiles(x, y, x, y-1);
                        hasMatches = true;
                    }
                }
            }
        }
        return hasMatches;
    }

    private boolean checkMatchesRight () {
        boolean hasMatches = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = 0; x < this._size - 1; x++) {
                if (this._tiles[x][y] != null) {
                    if (this._tiles[x][y].match(this._tiles[x+1][y])) {
                        this.mergeTiles(x, y, x+1, y);
                        hasMatches = true;
                    }
                }
            }
        }
        return hasMatches;
    }

    private boolean checkMatchesLeft () {
        boolean hasMatches = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = this._size - 1; x > 0; x--) {
                if (this._tiles[x][y] != null) {
                    if (this._tiles[x][y].match(this._tiles[x-1][y])) {
                        this.mergeTiles(x, y,x-1, y);
                        hasMatches = true;
                    }
                }
            }
        }
        return hasMatches;
    }

    private void mergeTiles(int x1, int y1, int x2, int y2) {
        this._tiles[x1][y1] = null;
        this._tiles[x2][y2].doubleValue();
        this._tilesCount--;
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


    public void dispose() {
        for (int i = 0; i < this.getSize(); i++){
            for (int j = 0; j < this.getSize(); j++) {
                if (this._tiles[i][j] != null) {
                    this._tiles[i][j].dispose();
                }
            }
        }
    }
}
