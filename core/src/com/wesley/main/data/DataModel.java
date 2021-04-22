package com.wesley.main.data;

import com.wesley.main.gameobject.TileGrid;

import java.util.ArrayList;

public class DataModel {
    private TileGrid _tileGrid;
    private ArrayList<TileGrid> _lastMoves;
    private int _size;

    public DataModel(TileGrid tileGrid, ArrayList<TileGrid> lastMoves, int size) {
        this._tileGrid = tileGrid;
        this._lastMoves = lastMoves;
        this._size = size;
    }

    public TileGrid getTiles() {
        return _tileGrid;
    }

    public ArrayList<TileGrid> getLastMoves() {
        return _lastMoves;
    }

    public int getSize() {
        return _size;
    }
}
