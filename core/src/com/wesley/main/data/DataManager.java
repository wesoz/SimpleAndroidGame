package com.wesley.main.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.gameobject.Tile;
import com.wesley.main.gameobject.Tiles;
import com.wesley.main.screen.Board;

import java.util.ArrayList;

public class DataManager {

    private Preferences _preferences;
    private String _fileID;

    public DataManager(String fileID) {
        this._fileID = fileID;
        String saveFile = "saveData_2048_" + this._fileID;
        this._preferences = Gdx.app.getPreferences(saveFile);
    }

    public void saveGame(DataModel dataModel) {
        this._preferences.clear();
        this.saveTiles(dataModel.getTiles(), "tiles");
        this.saveLastMoves(dataModel.getLastMoves(), "lastMoves");
        this._preferences.putInteger("size", dataModel.getSize());
        this._preferences.flush();
    }

    private void saveLastMoves(ArrayList<Tiles> lastMoves, String objectName) {
        for (int i = 0; i < lastMoves.size(); i++) {
            this.saveTiles(lastMoves.get(i), objectName + "_" + i);
        }
    }

    private void saveTiles(Tiles tiles, String objectName) {
        for (int x = 0; x < tiles.getSize(); x++) {
            for (int y = 0; y < tiles.getSize(); y++) {
                String tileObjectName = objectName + ".tile_" + x + "_" + y + ".";
                this.saveTile(tiles.getTile(x, y), tileObjectName);
            }
        }

        /*for (int i = 0; i < tiles.getTilesToMove().size(); i++) {
            String tileID = this._fileID + "_tileToMove_" + i;
            this.saveTile(tiles.getTilesToMove().get(i), tileID);
        }*/

        //List<Tile> tilesToMove,
        //Tiles.DIRECTION direction,
        //Tiles.STATE state = tiles.getState();

        int tileSize = tiles.getTileSize();
        int size = tiles.getSize();
        int maxTiles = tiles.getMaxTiles();
        int tilesCount = tiles.getTilesCount();
        Vector2 offset = tiles.getOffset();
        //Random random;
        boolean isFirstExecution = tiles.isFirstExecution();

        this._preferences.putInteger(objectName + "tileSize", tileSize);
        this._preferences.putInteger(objectName + "size", size);
        this._preferences.putInteger(objectName + "maxTiles", maxTiles);
        this._preferences.putInteger(objectName + "tilesCount", tilesCount);
        this._preferences.putFloat(objectName + "offset.x", offset.x);
        this._preferences.putFloat(objectName + "offset.y", offset.y);
        this._preferences.putBoolean(objectName + "isFirstExecution", isFirstExecution);
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

            this._preferences.putInteger(objectName + "value", value);
            this._preferences.putInteger(objectName + "tileSize", tileSize);
            this._preferences.putFloat(objectName + "offset", offset);
            this._preferences.putFloat(objectName + "position.x", position.x);
            this._preferences.putFloat(objectName + "position.y", position.y);
            this._preferences.putFloat(objectName + "speed", speed);
            this._preferences.putInteger(objectName + "x", x);
            this._preferences.putInteger(objectName + "y", y);
        }
    }

    private void deleteTile(String tileID) {

        this._preferences.remove(tileID + "value");
        this._preferences.remove(tileID + "tileSize");
        this._preferences.remove(tileID + "offset");
        this._preferences.remove(tileID + "position.x");
        this._preferences.remove(tileID + "position.y");
        this._preferences.remove(tileID + "destination.x");
        this._preferences.remove(tileID + "destination.y");
        this._preferences.remove(tileID + "speed");
        this._preferences.remove(tileID + "x");
        this._preferences.remove(tileID + "y");
    }

    public DataModel loadGame() {
        int size = this._preferences.getInteger("size", -1);
        if (size == -1) return null;

        Tiles tiles = this.loadTiles("tiles");
        ArrayList<Tiles> lastMoves = this.loadLastMoves("lastMoves");
        boolean isPlayerTurn = this._preferences.getBoolean("isPlayerTurn", false);

        DataModel dataModel = new DataModel(tiles, lastMoves, size);

        return dataModel;
    }

    private ArrayList<Tiles> loadLastMoves(String objectName) {
        ArrayList<Tiles> lastMoves = new ArrayList<>();
        for (int i = 0; i < Board.LAST_MOVES_LIMIT;i++) {
            Tiles tiles = this.loadTiles(objectName + "_" + i);
            if (tiles == null) break;
            lastMoves.add(tiles);
        }
        return lastMoves;
    }

    private Tiles loadTiles(String objectName) {
        int size;
        Tile[][] tiles;
        int tileSize;
        int maxTiles;
        int tilesCount;
        Vector2 offset;
        boolean isFirstExecution;

        size = this._preferences.getInteger(objectName + "size", -1);
        if (size == -1) return null;

        tiles = new Tile[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                String tileObjectName = objectName + ".tile_" + x + "_" + y + ".";
                 tiles[x][y] = this.loadTile(tileObjectName);
            }
        }

        tileSize = this._preferences.getInteger(objectName + "tileSize", 0);
        maxTiles = this._preferences.getInteger(objectName + "maxTiles", 0);
        tilesCount = this._preferences.getInteger(objectName + "tilesCount", 0);
        offset = new Vector2(this._preferences.getFloat(objectName + "offset.x", 0),
                             this._preferences.getFloat(objectName + "offset.y", 0));
        isFirstExecution = this._preferences.getBoolean(objectName + "isFirstExecution", false);

        return new Tiles(tiles, Tiles.DIRECTION.DOWN, Tiles.STATE.PLAYER_TURN,
                         tileSize, size, maxTiles, tilesCount, offset, isFirstExecution);
    }

    private Tile loadTile(String objectName) {
        int value = this._preferences.getInteger(objectName + "value", -1);;
        if (value == -1) return null;

        int tileSize = this._preferences.getInteger(objectName + "tileSize", 0);
        float offset = this._preferences.getFloat(objectName + "offset", 0);
        Vector2 position = new Vector2(this._preferences.getFloat(objectName + "position.x", 0),
                                       this._preferences.getFloat(objectName + "position.y", 0));
        float speed = this._preferences.getFloat(objectName + "speed", 0);
        int x = this._preferences.getInteger(objectName + "x", 0);
        int y = this._preferences.getInteger(objectName + "y", 0);

        return new Tile(value, tileSize, offset, position, speed, x, y);
    }
}
