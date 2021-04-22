package com.wesley.main.gameobject;

import com.badlogic.gdx.math.Vector2;

public class TileMovement {
    public final Tile Tile;
    public final MatrixPosition MatrixOrigin;
    public final Vector2 Delta;
    public final MatrixPosition MatrixDestination;
    public final Vector2 Destination;

    public TileMovement(Tile tile, MatrixPosition matrixOrigin, Vector2 delta, MatrixPosition matrixDestination, Vector2 destination) {
        this.Tile = tile;
        this.MatrixOrigin = matrixOrigin;
        this.Delta = delta;
        this.MatrixDestination = matrixDestination;
        this.Destination = destination;
    }

    public boolean isMovingHorizontally() {
        return this.MatrixOrigin.x != this.MatrixDestination.x;
    }

    public boolean isMovingVertically() {
        return this.MatrixOrigin.y != this.MatrixDestination.y;
    }

    public boolean isInFinalDestination() {
        if (this.isMovingHorizontally() && this.Tile.getPosition().x == this.Destination.x) {
            return true;
        }
        if (this.isMovingVertically() && this.Tile.getPosition().y == this.Destination.y) {
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