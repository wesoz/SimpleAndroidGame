package com.wesley.main.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;
import com.wesley.main.gameobject.TileGrid;
import com.wesley.main.screen.Board;

import java.util.ArrayList;

public class DataManager {

    private Preferences _preferences;
    private String _fileID;

    public DataManager(String fileID) {
        this._fileID = fileID;
        String saveFile = DataModel.SAVE_FILE_NAME + this._fileID;
        this._preferences = Gdx.app.getPreferences(saveFile);
    }

    public void saveGame(DataModel dataModel) {
        this._preferences.clear();
        this.saveTiles(dataModel.getTileGrid(), DataModel.OBJECT_TILE_GRID);
        this.saveLastMoves(dataModel.getLastMoves(), DataModel.OBJECT_LAST_MOVES);
        this._preferences.putInteger(DataModel.FIELD_SIZE, dataModel.getSize());
        this._preferences.flush();
    }

    private void saveLastMoves(ArrayList<TileGrid> lastMoves, String objectName) {
        for (int i = 0; i < lastMoves.size(); i++) {
            this.saveTiles(lastMoves.get(i), objectName + "_" + i);
        }
    }

    private void saveTiles(TileGrid tileGrid, String objectName) {
        for (int x = 0; x < tileGrid.getSize(); x++) {
            for (int y = 0; y < tileGrid.getSize(); y++) {
                String tileObjectName = objectName + ".tile_" + x + "_" + y + ".";
                this.saveTile(tileGrid.getTile(x, y), tileObjectName);
            }
        }

        int tileSize = tileGrid.getTileSize();
        int size = tileGrid.getSize();
        int maxTiles = tileGrid.getMaxTiles();
        int tilesCount = tileGrid.getTilesCount();
        Vector2 offset = tileGrid.getOffset();

        boolean isFirstExecution = tileGrid.isFirstExecution();

        this._preferences.putInteger(objectName + DataModel.FIELD_TILE_SIZE, tileSize);
        this._preferences.putInteger(objectName + DataModel.FIELD_SIZE, size);
        this._preferences.putInteger(objectName + DataModel.FIELD_MAX_TILES, maxTiles);
        this._preferences.putInteger(objectName + DataModel.FIELD_TILES_COUNT, tilesCount);
        this._preferences.putFloat(objectName + DataModel.FIELD_OFFSET_X, offset.x);
        this._preferences.putFloat(objectName + DataModel.FIELD_OFFSET_Y, offset.y);
        this._preferences.putBoolean(objectName + DataModel.FIELD_IS_FIRST_EXEC, isFirstExecution);
    }

    private void saveTile(Tile tile, String objectName) {
        if (tile == null) {
            this.deleteTile(objectName);
        } else {
            int value = tile.getValue();
            int tileSize = tile.getTileSize();
            float offset = tile.getOffset();
            Vector2 position = tile.getPosition();
            float speed = tile.getSpeed();
            int x = tile.getX();
            int y = tile.getY();

            this._preferences.putInteger(objectName + DataModel.FIELD_TILE_VALUE, value);
            this._preferences.putInteger(objectName + DataModel.FIELD_TILE_SIZE, tileSize);
            this._preferences.putFloat(objectName + DataModel.FIELD_TILE_OFFSET, offset);
            this._preferences.putFloat(objectName + DataModel.FIELD_TILE_POSITION_X, position.x);
            this._preferences.putFloat(objectName + DataModel.FIELD_TILE_POSITION_Y, position.y);
            this._preferences.putFloat(objectName + DataModel.FIELD_TILE_SPEED, speed);
            this._preferences.putInteger(objectName + DataModel.FIELD_TILE_X, x);
            this._preferences.putInteger(objectName + DataModel.FIELD_TILE_Y, y);
        }
    }

    private void deleteTile(String tileID) {

        this._preferences.remove(tileID + DataModel.FIELD_TILE_VALUE);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_SIZE);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_OFFSET);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_POSITION_X);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_POSITION_Y);
        // this._preferences.remove(tileID + "destination.x");
        // this._preferences.remove(tileID + "destination.y");
        this._preferences.remove(tileID + DataModel.FIELD_TILE_SPEED);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_X);
        this._preferences.remove(tileID + DataModel.FIELD_TILE_Y);
    }

    public DataModel loadGame() {
        int size = this._preferences.getInteger(DataModel.FIELD_SIZE, -1);
        if (size == -1) return null;

        TileGrid tileGrid = this.loadTiles(DataModel.OBJECT_TILE_GRID);
        ArrayList<TileGrid> lastMoves = this.loadLastMoves(DataModel.OBJECT_LAST_MOVES);

        DataModel dataModel = new DataModel(tileGrid, lastMoves, size);

        return dataModel;
    }

    private ArrayList<TileGrid> loadLastMoves(String objectName) {
        ArrayList<TileGrid> lastMoves = new ArrayList<>();
        for (int i = 0; i < Board.LAST_MOVES_LIMIT; i++) {
            TileGrid tileGrid = this.loadTiles(objectName + "_" + i);
            if (tileGrid == null) break;
            lastMoves.add(tileGrid);
        }
        return lastMoves;
    }

    private TileGrid loadTiles(String objectName) {
        int size;
        Tile[][] tiles;
        int tileSize;
        int maxTiles;
        int tilesCount;
        Vector2 offset;
        boolean isFirstExecution;

        size = this._preferences.getInteger(objectName + DataModel.FIELD_SIZE, -1);
        if (size == -1) return null;

        tiles = new Tile[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                String tileObjectName = objectName + ".tile_" + x + "_" + y + ".";
                tiles[x][y] = this.loadTile(tileObjectName);
            }
        }

        tileSize = this._preferences.getInteger(objectName + DataModel.FIELD_TILE_SIZE, 0);
        maxTiles = this._preferences.getInteger(objectName + DataModel.FIELD_MAX_TILES, 0);
        tilesCount = this._preferences.getInteger(objectName + DataModel.FIELD_TILES_COUNT, 0);
        offset = new Vector2(this._preferences.getFloat(objectName + DataModel.FIELD_OFFSET_X, 0),
                this._preferences.getFloat(objectName + DataModel.FIELD_OFFSET_Y, 0));
        isFirstExecution = this._preferences.getBoolean(objectName + DataModel.FIELD_IS_FIRST_EXEC, false);

        return new TileGrid(tiles, TileGrid.DIRECTION.DOWN, TileGrid.STATE.PLAYER_TURN,
                tileSize, size, maxTiles, tilesCount, offset, isFirstExecution);
    }

    private Tile loadTile(String objectName) {
        int value = this._preferences.getInteger(objectName + DataModel.FIELD_TILE_VALUE, -1);
        ;
        if (value == -1) return null;

        int tileSize = this._preferences.getInteger(objectName + DataModel.FIELD_TILE_SIZE, 0);
        float offset = this._preferences.getFloat(objectName + DataModel.FIELD_TILE_OFFSET, 0);
        Vector2 position = new Vector2(this._preferences.getFloat(objectName + DataModel.FIELD_TILE_POSITION_X, 0),
                this._preferences.getFloat(objectName + DataModel.FIELD_TILE_POSITION_Y, 0));
        float speed = this._preferences.getFloat(objectName + DataModel.FIELD_TILE_SPEED, 0);
        int x = this._preferences.getInteger(objectName + DataModel.FIELD_TILE_X, 0);
        int y = this._preferences.getInteger(objectName + DataModel.FIELD_TILE_Y, 0);

        return new Tile(value, tileSize, offset, position, speed, x, y);
    }
}
