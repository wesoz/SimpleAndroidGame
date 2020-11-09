package com.wesley.main.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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
        PLAYER_TURN,
        CREATE_TILE
    }

    private final Tile[][] _tiles;
    private DIRECTION _direction;
    private STATE _state;
    private final int _tileSize;
    private final int _size;
    private final int _maxTiles;
    private int _tilesCount;
    private Vector2 _offset;
    private final Random _random;
    private boolean _isFirstExecution;

    public Tiles(int size) {
        this._size = size;
        this._tileSize = (Gdx.graphics.getWidth() - 80) / size;
        this._tiles = new Tile[size][size];
        this._maxTiles = this._size * this._size;
        this._tilesCount = 0;
        this._state = STATE.PLAYER_TURN;
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
        int x, y;
        do {
            x = _random.nextInt(this._tiles.length);
            y = _random.nextInt(this._tiles[0].length);
        } while (this._tiles[x][y] != null);
        this.setTile(new Tile(this._tileSize, 2, this.getInBoardPosition(x, y)), x, y);
        this._tilesCount++;
        this._state = STATE.PLAYER_TURN;
        return true;
    }

    public boolean hasTile(int x, int y) {
        return this._tiles[x][y] != null;
    }

    private Vector2 getInBoardPosition(int x, int y) {
        int xPos = (int) this._offset.x + (this.getTileSize() * x);
        int yPos = (int) this._offset.y + (this.getTileSize() * y);
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

    public void update() {
        if (this._state == STATE.MOVE) {
            this.moveTiles(false);
        }
    }

    // TILES MOVEMENT
    public void startMovingTiles(DIRECTION direction) {
        this._direction = direction;
        this._state = STATE.MOVE;
        this._isFirstExecution = true;
        this.resetMergeTiles();
    }

    private void resetMergeTiles() {
        for (int x = 0; x < this._size; x++) {
            for (int y = 0; y < this._size; y++) {
                if (this._tiles[x][y] != null) {
                    this._tiles[x][y].setMerged(false);
                }
            }
        }
    }

    private void moveTiles(boolean mergeTiles) {
        boolean hasMoves = false;
        switch (this._direction) {
            case UP:
                hasMoves = this.moveUp(mergeTiles);
                break;
            case DOWN:
                hasMoves = this.moveDown(mergeTiles);
                break;
            case RIGHT:
                hasMoves = this.moveRight(mergeTiles);
                break;
            case LEFT:
                hasMoves = this.moveLeft(mergeTiles);
                break;
        }
        if (hasMoves) {
            this._isFirstExecution = false;
            //if (!mergeTiles) {
            //this.moveTiles(true);
            //}
        } else {
            if (this._isFirstExecution) {
                this._state = STATE.PLAYER_TURN;
            } else {
                this._state = STATE.CREATE_TILE;
            }
        }
    }

    private float getDelta(float position, float destination, float speed) {
        return Math.min(Math.abs(position - destination), Math.abs(speed));
    }

    private void mergeTiles(int x1, int y1, int x2, int y2) {
        this._tiles[x1][y1] = null;
        this._tiles[x2][y2].doubleValue();
        this._tiles[x2][y2].setMerged(true);
        this._tilesCount--;
    }

    private boolean moveLeft(boolean mergeTiles) {
        boolean hasMoves = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = 1; x < this._size; x++) {
                hasMoves = this.checkAndMoveTile(x, y, x - 1, y, -1, 0) || hasMoves;
            }
        }
        return hasMoves;
    }

    private boolean moveRight(boolean mergeTiles) {
        boolean hasMoves = false;
        for (int y = 0; y < this._size; y++) {
            for (int x = this._size - 2; x >= 0; x--) {
                hasMoves = this.checkAndMoveTile(x, y, x + 1, y, 1, 0) || hasMoves;
            }
        }
        return hasMoves;
    }

    private boolean moveUp(boolean mergeTiles) {
        boolean hasMoves = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = this._size - 2; y >= 0; y--) {
                hasMoves = this.checkAndMoveTile(x, y, x, y + 1, 0, 1) || hasMoves;
            }
        }
        return hasMoves;
    }

    private boolean moveDown(boolean mergeTiles) {
        boolean hasMoves = false;
        for (int x = 0; x < this._size; x++) {
            for (int y = 1; y < this._size; y++) {
                hasMoves = this.checkAndMoveTile(x, y, x, y-1, 0, -1) || hasMoves;
            }
        }
        return hasMoves;
    }

    private boolean checkAndMoveTile(int x, int y, int targetX, int targetY, int deltaXMultiplier, int deltaYMultiplier) {
        TileMovement tileMovement = getTileMovement(x, y, targetX, targetY, deltaXMultiplier, deltaYMultiplier);
        if (tileMovement != null) {
            moveTile(tileMovement);
            return true;
        }
        return false;
    }

    private TileMovement getTileMovement(int x, int y, int targetX, int targetY,
                                         int deltaXMultiplier, int deltaYMultiplier) {
        if (this._tiles[x][y] != null) {
            Vector2 destination = this.getInBoardPosition(targetX, targetY);
            if (this._tiles[targetX][targetY] == null) {
                Tile tileToMove = this._tiles[x][y];
                float deltaX = this.getDelta(tileToMove.getPosition().x, destination.x, tileToMove.getSpeed()) * deltaXMultiplier;
                float deltaY = this.getDelta(tileToMove.getPosition().y, destination.y, tileToMove.getSpeed()) * deltaYMultiplier;
                return new TileMovement(tileToMove,
                        new MatrixPosition(x, y),
                        new Vector2(deltaX, deltaY),
                        new MatrixPosition(targetX, targetY), destination);
            }
        }
        return null;
    }

    private boolean moveTile(TileMovement tileMovement) {
        tileMovement.Tile.move(tileMovement.Delta.x, tileMovement.Delta.y);
        if (tileMovement.isInFinalDestination()) {
            this.exchangePosition(tileMovement.MatrixOrigin.x, tileMovement.MatrixOrigin.y, tileMovement.MatrixDestination.x, tileMovement.MatrixDestination.y);
            return false;
        } else {
            return true;
        }
    }

    private void exchangePosition(int x1, int y1, int x2, int y2) {
        Tile element1 = this._tiles[x1][y1];
        Tile element2 = this._tiles[x2][y2];
        this.setTile(element2, x1, y1);
        this.setTile(element1, x2, y2);
    }

    // END TILES MOVEMENT

    public void setOffset(Vector2 offset) {
        this._offset = offset;
    }

    private void setTile(Tile tile, int x, int y) {
        if (tile != null) tile.set(x, y);
        this._tiles[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        return this._tiles[x][y];
    }

    public int getMaxTiles() {
        return this._maxTiles;
    }

    public int getTilesCount() {
        return this._tilesCount;
    }

    public Vector2 getOffset() {
        return this._offset;
    }

    public boolean isFirstExecution() {
        return this._isFirstExecution;
    }

    public void dispose() {
        for (int i = 0; i < this.getSize(); i++) {
            for (int j = 0; j < this.getSize(); j++) {
                if (this._tiles[i][j] != null) {
                    this._tiles[i][j].dispose();
                }
            }
        }
    }

    public Tiles clone() {
        Tile[][] tiles = new Tile[this._size][this._size];

        for (int x = 0; x < this.getSize(); x++) {
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
