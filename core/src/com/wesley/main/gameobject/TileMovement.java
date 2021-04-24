package com.wesley.main.gameobject;

import com.badlogic.gdx.math.Vector2;

public class TileMovement {
    private final Tile _tile;
    private final MatrixPosition _matrixOrigin;
    private final MatrixPosition _matrixDestination;
    private final Vector2 _destination;
    private final Vector2 _originalDistance;
    private final float _totalSpeed;

    public TileMovement(Tile tile, MatrixPosition matrixOrigin, MatrixPosition matrixDestination, Vector2 destination, float speed) {
        this._tile = tile;
        this._matrixOrigin = matrixOrigin;
        this._matrixDestination = matrixDestination;
        this._destination = destination;
        this._originalDistance = this.getDistance();
        this._totalSpeed = speed;
    }

    private Vector2 getDistance() {
        return this._destination.cpy().sub(this._tile.getPosition());
    }

    public Vector2 getVelocity(float deltaTime) {
        Vector2 velocity = new Vector2((this._originalDistance.x / this._totalSpeed) * deltaTime, (this._originalDistance.y / this._totalSpeed) * deltaTime);
        Vector2 currentDistance = this.getDistance();
        if (Math.abs(currentDistance.x) < Math.abs(velocity.x) || Math.abs(currentDistance.y) < Math.abs(velocity.y)) {
            return currentDistance;
        }
        return velocity;
    }

    public boolean isMovingHorizontally() {
        return this._matrixOrigin.x != this._matrixDestination.x;
    }

    public boolean isMovingVertically() {
        return this._matrixOrigin.y != this._matrixDestination.y;
    }

    public boolean isInFinalDestination() {
        if (this.isMovingHorizontally() && this._tile.getPosition().x == this._destination.x) {
            return true;
        }
        if (this.isMovingVertically() && this._tile.getPosition().y == this._destination.y) {
            return true;
        }
        return false;
    }
}

class MatrixPosition {
    public final int x;
    public final int y;

    public MatrixPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}