package com.wesley.main.data;

import com.wesley.main.gameobject.TileGrid;

import java.util.ArrayList;

public class DataModel {

    public static final String OBJECT_TILE_GRID = "tileGrid";
    public static final String OBJECT_LAST_MOVES = "lastMoves";
    public static final String SAVE_FILE_NAME = "saveData_2048_";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_TILE_SIZE = "tileSize";
    public static final String FIELD_MAX_TILES = "maxTiles";
    public static final String FIELD_TILES_COUNT = "tilesCount";
    public static final String FIELD_OFFSET_X = "offset.x";
    public static final String FIELD_OFFSET_Y = "offset.y";
    public static final String FIELD_IS_FIRST_EXEC = "isFirstExecution";
    public static final String FIELD_TILE_VALUE = "value";
    public static final String FIELD_TILE_OFFSET = "offset";
    public static final String FIELD_TILE_POSITION_X = "position.x";
    public static final String FIELD_TILE_POSITION_Y = "position.y";
    public static final String FIELD_TILE_SPEED = "speed";
    public static final String FIELD_TILE_X = "x";
    public static final String FIELD_TILE_Y = "y";

    private TileGrid _tileGrid;
    private ArrayList<TileGrid> _lastMoves;
    private int _size;

    public DataModel(TileGrid tileGrid, ArrayList<TileGrid> lastMoves, int size) {
        this._tileGrid = tileGrid;
        this._lastMoves = lastMoves;
        this._size = size;
    }

    public TileGrid getTileGrid() {
        return _tileGrid;
    }

    public ArrayList<TileGrid> getLastMoves() {
        return _lastMoves;
    }

    public int getSize() {
        return _size;
    }
}
