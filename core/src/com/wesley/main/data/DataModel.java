package com.wesley.main.data;

import com.wesley.main.gameobject.Tiles;

import java.util.ArrayList;

public class DataModel {
    private Tiles _tiles;
    private ArrayList<Tiles> _lastMoves;
    private int _size;

    public DataModel(Tiles tiles, ArrayList<Tiles> lastMoves, int size) {
        this._tiles = tiles;
        this._lastMoves = lastMoves;
        this._size = size;
    }

    public Tiles getTiles() {
        return _tiles;
    }

    public ArrayList<Tiles> getLastMoves() {
        return _lastMoves;
    }

    public int getSize() {
        return _size;
    }
}
