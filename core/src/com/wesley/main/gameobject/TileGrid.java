package com.wesley.main.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileGrid {
    public enum DIRECTION {
        UP,
        DOWN,
        RIGHT,
        LEFT,
    }

    public enum STATE {
        VALIDATE_MOVE,
        MOVE,
        PLAYER_TURN,
        CREATE_TILE
    }

    private Tile[][] _tiles;
    private DIRECTION _direction;
    private STATE _state;
    private int _tileSize;
    private int _size;
    private int _maxTiles;
    private int _tilesCount;
    private Vector2 _offset;
    private final Random _random;
    private boolean _isFirstExecution;
    private final List<Tile> _tilesToMove;

    public TileGrid() {
        this._random = new Random();
        this._tilesToMove = new ArrayList<>();
    }

    public TileGrid(int size) {
        this();
        this._size = size;
        this._tileSize = (Gdx.graphics.getWidth() - 80) / size;
        this._tiles = new Tile[size][size];
        this._maxTiles = this._size * this._size;
        this._tilesCount = 0;
        this._state = STATE.PLAYER_TURN;
        this._isFirstExecution = false;
    }

    public TileGrid(Tile[][] tiles, DIRECTION direction, STATE state, int tileSize,
                    int size, int maxTiles, int tilesCount, Vector2 offset, boolean isFirstExecution) {
        this();
        this._tiles = tiles;
        this._direction = direction;
        this._state = state;
        this._tileSize = tileSize;
        this._size = size;
        this._maxTiles = maxTiles;
        this._tilesCount = tilesCount;
        this._offset = offset;
        this._isFirstExecution = isFirstExecution;
    }

    public float getSpeed() {
        return 0.04f * this._size;
    }

    public int getSize() {
        return this._size;
    }

    public int getTileSize() {
        return this._tileSize;
    }

    public void createTile() {
        if (this._tilesCount >= this._maxTiles)
            return;
        int x, y;
        do {
            x = _random.nextInt(this._tiles.length);
            y = _random.nextInt(this._tiles[0].length);
        } while (this._tiles[x][y] != null);
        this.setTile(new Tile(this._tileSize, 2, this.getInBoardPosition(x, y)), x, y);
        this._tilesCount++;
        this._state = STATE.PLAYER_TURN;
    }

    private Vector2 getInBoardPosition(int x, int y) {
        int xPos = (int) this._offset.x + (this.getTileSize() * x);
        int yPos = (int) this._offset.y + (this.getTileSize() * y);
        return new Vector2(xPos, yPos);
    }

    public void draw(Tile tile, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        if (tile != null) {
            tile.draw(shapeRenderer, spriteBatch);

            if (tile.getMergeTile() != null) {
                this.draw(tile.getMergeTile(), shapeRenderer, spriteBatch);
            }
        }
    }

    public STATE getState() {
        return this._state;
    }

    public void update() {
        if (this._state == STATE.VALIDATE_MOVE) {
            switch (this._direction) {
                case UP:
                    this.setTilesToMoveUp();
                    break;
                case DOWN:
                    this.setTilesToMoveDown();
                    break;
                case RIGHT:
                    this.setTilesToMoveRight();
                    break;
                case LEFT:
                    this.setTilesToMoveLeft();
                    break;
            }
            this._state = STATE.MOVE;
        } else if (this._state == STATE.MOVE) {
            this.updateTiles();
        }
    }

    // TILES MOVEMENT
    public void startMovingTiles(DIRECTION direction) {
        this._direction = direction;
        this._state = STATE.VALIDATE_MOVE;
        this._isFirstExecution = true;
        this.resetMergeTiles();
    }

    private void resetMergeTiles() {
        for (int x = 0; x < this._size; x++) {
            for (int y = 0; y < this._size; y++) {
                if (this._tiles[x][y] != null) {
                    this._tiles[x][y].setMergeTile(null);
                }
            }
        }
    }

    private void updateTiles() {
        if (this._tilesToMove.size() > 0) {
            this._isFirstExecution = false;
            this.moveTiles();
        } else {
            if (this._isFirstExecution) {
                this._state = STATE.PLAYER_TURN;
            } else {
                this._state = STATE.CREATE_TILE;
            }
        }
    }

    private boolean nextFreeTileTest(MatrixPosition tilePosition, MatrixPosition targetStart, int count) {
        if (tilePosition.y < targetStart.y) {
            return count <= this._size - 1;
        } else if (tilePosition.y > targetStart.y) {
            return count >= 0;
        } else if (tilePosition.x < targetStart.x) {
            return count <= this._size - 1;
        } else if (tilePosition.x > targetStart.x) {
            return count >= 0;
        }

        return false;
    }

    private MatrixPosition getNextFreeTile(MatrixPosition tilePosition, MatrixPosition targetStart, int increment) {
        MatrixPosition matrixPosition = null;
        Tile current = this._tiles[tilePosition.x][tilePosition.y];
        if (tilePosition.y != targetStart.y) {
            for (int targetY = targetStart.y; this.nextFreeTileTest(tilePosition, targetStart, targetY); targetY += increment) {
                Tile target = this._tiles[tilePosition.x][targetY];
                if (target == null) {
                    matrixPosition = new MatrixPosition(tilePosition.x, targetY);
                } else if (target.getMergeTile() == null && current.match(target)) {
                    matrixPosition = new MatrixPosition(tilePosition.x, targetY);
                    current.setMergeTile(target);
                } else {
                    break;
                }
            }
        } else if (tilePosition.x != targetStart.x) {
            for (int targetX = targetStart.x; this.nextFreeTileTest(tilePosition, targetStart, targetX); targetX += increment) {
                Tile target = this._tiles[targetX][tilePosition.y];
                if (target == null) {
                    matrixPosition = new MatrixPosition(targetX, tilePosition.y);
                } else if (target.getMergeTile() == null && current.match(target)) {
                    matrixPosition = new MatrixPosition(targetX, tilePosition.y);
                    current.setMergeTile(target);
                } else {
                    break;
                }
            }
        }
        return matrixPosition;
    }

    private void addToMovementList(Tile tileToMove, MatrixPosition currentPosition, MatrixPosition targetPosition, int increment) {
        if (tileToMove != null) {
            if (tileToMove.getTileMovement() == null) {
                MatrixPosition targetMatrixPosition = this.getNextFreeTile(currentPosition, targetPosition, increment);
                if (targetMatrixPosition != null) {
                    this.exchangePosition(currentPosition.x, currentPosition.y, targetMatrixPosition.x, targetMatrixPosition.y);
                    tileToMove.setTileMovement(this.getTileMovement(tileToMove, currentPosition, targetMatrixPosition));
                    this._tilesToMove.add(tileToMove);
                }
            }
        }
    }

    private void setTilesToMoveUp() {
        for (int x = 0; x < this._size; x++) {
            for (int y = this._size - 1; y >= 0; y--) {
                this.addToMovementList(this._tiles[x][y], new MatrixPosition(x, y), new MatrixPosition(x, y + 1), 1);
            }
        }
    }

    private void setTilesToMoveDown() {
        for (int x = 0; x < this._size; x++) {
            for (int y = 1; y < this._size; y++) {
                this.addToMovementList(this._tiles[x][y], new MatrixPosition(x, y), new MatrixPosition(x, y - 1), -1);
            }
        }
    }

    private void setTilesToMoveRight() {
        for (int y = 0; y < this._size; y++) {
            for (int x = this._size - 2; x >= 0; x--) {
                this.addToMovementList(this._tiles[x][y], new MatrixPosition(x, y), new MatrixPosition(x + 1, y), 1);
            }
        }
    }

    private void setTilesToMoveLeft() {
        for (int y = 0; y < this._size; y++) {
            for (int x = 1; x < this._size; x++) {
                this.addToMovementList(this._tiles[x][y], new MatrixPosition(x, y), new MatrixPosition(x - 1, y), -1);
            }
        }
    }

    private void moveTiles() {
        for (int i = 0; i < this._tilesToMove.size(); i++) {
            Tile tileToMove = this._tilesToMove.get(i);
            TileMovement movement = tileToMove.getTileMovement();
            Vector2 velocity = movement.getVelocity(Gdx.graphics.getDeltaTime());
            tileToMove.move(velocity);
            if (movement.isInFinalDestination()) {
                tileToMove.setTileMovement(null);
                if (tileToMove.getMergeTile() != null) {
                    tileToMove.setMergeTile(null);
                    tileToMove.doubleValue();
                }
                this._tilesToMove.remove(i);
                i--;
            }
        }
    }

    private TileMovement getTileMovement(Tile tileToMove, MatrixPosition currentPosition, MatrixPosition target) {
        Vector2 destination = this.getInBoardPosition(target.x, target.y);

        return new TileMovement(tileToMove,
                new MatrixPosition(currentPosition.x, currentPosition.y),
                new MatrixPosition(target.x, target.y), destination, this.getSpeed());
    }

    private void exchangePosition(int x1, int y1, int x2, int y2) {
        this.setTile(this._tiles[x1][y1], x2, y2);
        this.setTile(null, x1, y1);
        this._tilesCount--;
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

    public TileGrid clone() {
        Tile[][] tiles = new Tile[this._size][this._size];

        for (int x = 0; x < this.getSize(); x++) {
            for (int y = 0; y < this.getSize(); y++) {
                if (this._tiles[x][y] != null) {
                    tiles[x][y] = this._tiles[x][y].clone();
                } else
                    tiles[x][y] = null;
            }
        }

        return new TileGrid(tiles, this._direction, this._state,
                this._tileSize, this._size, this._maxTiles, this._tilesCount,
                new Vector2(this._offset), this._isFirstExecution);
    }
}
