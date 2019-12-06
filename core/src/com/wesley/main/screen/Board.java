package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.wesley.main.data.DataManager;
import com.wesley.main.data.DataModel;
import com.wesley.main.gameobject.Tiles;
import java.util.ArrayList;

public class Board extends Screen {

    public static final int LAST_MOVES_LIMIT = 5;
    private Tiles _tiles;
    private ArrayList<Tiles> _lastMoves;
    private int _gridSize;
    private int _size;
    private int _firstX;
    private int _firstY;
    private BitmapFont _font;
    TextButton _btnUndo;
    Stage _stage;
    Table _restartDialogTable;
    Table _gameButtonsTable;
    DataManager _dataManager;

    private Board() {
        super();
        this._restartDialogTable = null;
        this._font = new BitmapFont();
        this._font.setColor(Color.WHITE);
        this._font.getData().setScale(5);
        this._lastMoves = new ArrayList<>();
    }

    public Board(Tiles tiles, ArrayList<Tiles> lastMoves, int size) {
        this();
        this.initialize(size);
        this._tiles = tiles;
        this._lastMoves = lastMoves;
    }

    public Board(int size) {
        this();
        this.initialize(size);
        this.startGame();
    }

    private void initialize(int size) {
        this._size = size;
        this._tiles = new Tiles(this._size);
        this.calculateBoundaries();
        _dataManager = new DataManager(Board.getSaveFileID(size));
        this.setupScreenControls();
    }

    private void setupScreenControls() {
        this._stage = new Stage();
        Gdx.input.setInputProcessor(_stage);
        buildGameButtons();
    }

    public static String getSaveFileID(int boardSize) {
        return boardSize + "x" + boardSize;
    }

    private void saveState() {
        DataModel dataModel = new DataModel(this._tiles, this._lastMoves, this._size);
        this._dataManager.saveGame(dataModel);
    }

    private void buildGameButtons() {
        this._gameButtonsTable = new Table();
        Texture upTexture = new Texture("buttons/restart.png");
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        Button btnRestart;
        btnRestart = new Button(upDrawable);
        btnRestart.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Board.this.buildRestartDialog();
            }
        });

        upTexture = new Texture("buttons/undo.png");
        upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        BitmapFont font = new BitmapFont();
        font.getData().setScale(4f);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = upDrawable;
        this._btnUndo = new TextButton("", textButtonStyle);
        this._btnUndo.getLabel().setAlignment(Align.topRight);
        this._btnUndo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Board.this.popMove();
            }
        });

        this._gameButtonsTable.add(this._btnUndo).pad(100);
        this._gameButtonsTable.add(btnRestart);
        this._gameButtonsTable.setPosition(500, (Gdx.graphics.getHeight() - upTexture.getHeight()) - 100);
        this._stage.addActor(this._gameButtonsTable);
    }

    private void buildRestartDialog() {
        this._restartDialogTable = new Table();

        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(5);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label("Restart Game?", labelStyle);
        this._restartDialogTable.add(label).width(400);
        this._restartDialogTable.row();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        TextButton btnNo = new TextButton("No", textButtonStyle);
        btnNo.getLabel().setFontScale(5f);
        btnNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Board.this._restartDialogTable = null;
            }
        });
        this._restartDialogTable.add(btnNo).width(200);

        TextButton btnYes = new TextButton("Yes", textButtonStyle);
        btnYes.getLabel().setFontScale(5f);
        btnYes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Board.this.restartGame();
            }
        });
        this._restartDialogTable.add(btnYes).width(200);

        this._restartDialogTable.setFillParent(true);
        this._stage.addActor(this._restartDialogTable);
    }

    private void startGame() {
        this._tiles.createTile();
        this._tiles.createTile();
    }

    private void restartGame() {
        this.initialize(this._size);
        this._restartDialogTable = null;
        this.startGame();
        this.saveState();
    }

    private void calculateBoundaries() {
        this._gridSize = this._size * this._tiles.getTileSize();
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (this._gridSize / 2);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (this._gridSize / 2);
        this._tiles.setOffset(new Vector2(this._firstX, this._firstY));
    }

    public Screen update() {

        if (this._restartDialogTable != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                this._restartDialogTable = null;
            }
            return this;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            return new MainMenu();
        }

        if (this._tiles.getState() == Tiles.STATE.MOVE) {
            this._tiles.update();
        } else if (this._tiles.getState() == Tiles.STATE.PLAYERTURN){
                this.handleInput();
        } else if (this._tiles.getState() == Tiles.STATE.CREATETILE) {
            this._tiles.createTile();
            this.saveState();
        }

        return this;
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            float deltaX = Gdx.input.getDeltaX();
            float deltaY = Gdx.input.getDeltaY();
            if (Math.abs(deltaX) < 30 && Math.abs(deltaY) < 30)
                return;

            this.addMove();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    this._tiles.moveTiles(Tiles.DIRECTION.RIGHT);
                } else {
                    this._tiles.moveTiles(Tiles.DIRECTION.LEFT);
                }
            } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
                if (deltaY > 0) {
                    this._tiles.moveTiles(Tiles.DIRECTION.DOWN);
                } else {
                    this._tiles.moveTiles(Tiles.DIRECTION.UP);
                }
            }
        }
    }

    private void addMove() {
        if (this._lastMoves.size() == Board.LAST_MOVES_LIMIT)
            this._lastMoves.remove(0);
        this._lastMoves.add(this._tiles.clone());
    }

    private void popMove() {
        if (this._lastMoves.size() > 0) {
            Tiles tiles = this._lastMoves.get(this._lastMoves.size() - 1);
            this._tiles = tiles;
            this._lastMoves.remove(tiles);
            this.saveState();
        }
    }

    public void draw() {
        super._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        super._shapeRenderer.setColor(0.949f,0.9686f,0.9882f,1);
        super._shapeRenderer.rect(this._firstX, this._firstY, this._gridSize, this._gridSize);
        super._shapeRenderer.end();
        for (int x = 0; x < this._tiles.getSize(); x++){
            for (int y = 0; y < this._tiles.getSize(); y++) {
                int xPos = this._firstX + (this._tiles.getTileSize() * x);
                int yPos = this._firstY + (this._tiles.getTileSize() * y);
                if (this._tiles.hasTile(x, y)) {
                    this._tiles.draw(super._shapeRenderer, super._spriteBatch, x, y);
                }
                this.drawBGRect(xPos, yPos);
            }
        }
        this.drawGameButtons();
        this.drawRestartDialog();
    }

    private void drawGameButtons() {
        if (_gameButtonsTable != null) {
            super._spriteBatch.begin();
            this._btnUndo.setText(String.valueOf(this._lastMoves.size()));
            this._gameButtonsTable.draw(super._spriteBatch, 1);
            super._spriteBatch.end();
        }
    }

    private void drawRestartDialog() {

        if (this._restartDialogTable != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            super._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            super._shapeRenderer.setColor(1f, 0.7f, 0.2f, 0.8f);
            super._shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            super._shapeRenderer.end();
            super._spriteBatch.begin();
            this._restartDialogTable.draw(super._spriteBatch, 1);
            super._spriteBatch.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void drawBGRect(int x, int y) {
        super._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float borderWeight = 20f;
        Vector2 bottomLeft = new Vector2(x, y);
        Vector2 topLeft = new Vector2(x, y + this._tiles.getTileSize());
        Vector2 topRight = new Vector2(x + this._tiles.getTileSize(), y + this._tiles.getTileSize());
        Vector2 bottomRight = new Vector2(x + this._tiles.getTileSize(), y);

        super._shapeRenderer.setColor(0.7f,0.7f,0.74f,1);
        super._shapeRenderer.rectLine(bottomLeft, topLeft, borderWeight);
        super._shapeRenderer.rectLine(topLeft, topRight, borderWeight);
        super._shapeRenderer.rectLine(topRight, bottomRight, borderWeight);
        super._shapeRenderer.rectLine(bottomRight, bottomLeft, borderWeight);
        super._shapeRenderer.end();
    }

    public void dispose() {
        super.dispose();

        this._tiles.dispose();
    }
}
