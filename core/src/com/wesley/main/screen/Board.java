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
import com.wesley.main.gameobject.Tiles;

public class Board extends Screen {

    private Tiles _tiles;
    private int _gridSize;
    private int _size;
    private int _firstX;
    private int _firstY;
    private boolean _playerTurn;
    private BitmapFont _font;
    float deltaX = 0;
    float deltaY = 0;
    Stage _stage;
    Button _btnRestart;
    Table _restartDialogTable;

    public Board(int size) {
        super();
        this._size = size;
        this.startGame();

        this._stage = new Stage();
        Gdx.input.setInputProcessor(_stage);
        Texture upTexture = new Texture("buttons/restart.png");
        Drawable up = new TextureRegionDrawable(new TextureRegion(upTexture));
        this._btnRestart = new Button(up);
        this._btnRestart.setPosition(500, (Gdx.graphics.getHeight() - upTexture.getHeight()) - 100);
        this._stage.addActor(this._btnRestart);
        this._btnRestart.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Board.this.buildRestartDialog();
            }
        });

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
                Board.this.startGame();
            }
        });
        this._restartDialogTable.add(btnYes).width(200);

        this._restartDialogTable.setFillParent(true);
        this._stage.addActor(this._restartDialogTable);
    }

    private void startGame() {
        this._tiles = new Tiles(this._size);
        this._gridSize = this._size * this._tiles.getTileSize();
        this._firstX = (Gdx.app.getGraphics().getWidth() / 2) - (this._gridSize / 2);
        this._firstY = (Gdx.app.getGraphics().getHeight() / 2) - (this._gridSize / 2);
        this._tiles.setOffset(new Vector2(this._firstX, this._firstY));
        this._playerTurn = false;
        this._tiles.createTile();
        this._tiles.createTile();
        this._playerTurn = true;
        this._restartDialogTable = null;
        this._font = new BitmapFont();
        this._font.setColor(Color.WHITE);
        this._font.getData().setScale(5);
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

        if (this._tiles.getState() != Tiles.STATE.STATIC) {
            this._tiles.update();
            this._playerTurn = false;
        } else {
            if (this._playerTurn) {
                this.handleInput();
            } else {
                this._tiles.createTile();
                this._playerTurn = true;
            }
        }

        return this;
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            deltaX = Gdx.input.getDeltaX();
            deltaY = Gdx.input.getDeltaY();
            if (Math.abs(deltaX) < 30 && Math.abs(deltaY) < 30)
                return;
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

    public void draw() {
        super._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        super._shapeRenderer.setColor(0.949f,0.9686f,0.9882f,1);
        super._shapeRenderer.rect(this._firstX, this._firstY, this._gridSize, this._gridSize);
        super._shapeRenderer.end();
        for (int i = 0; i < this._tiles.getSize(); i++){
            for (int j = 0; j < this._tiles.getSize(); j++) {
                int x = this._firstX + (this._tiles.getTileSize() * i);
                int y = this._firstY + (this._tiles.getTileSize() * j);
                if (this._tiles.hasTile(i, j)) {
                    this._tiles.draw(super._shapeRenderer, super._spriteBatch, i, j);
                }
                this.drawBGRect(x, y);
            }
        }
        String text = deltaX + "," + deltaY;
        super._spriteBatch.begin();
        this._font.draw(super._spriteBatch, text,100,100);
        this._btnRestart.draw(super._spriteBatch, 1);
        super._spriteBatch.end();
        this.drawRestartDialog();
    }

    private void drawRestartDialog() {

        if (this._restartDialogTable == null) return;
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

    private void drawBGRect(int x, int y) {
        super._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float borderWeight = 5f;
        Vector2 bottomLeft = new Vector2(x, y);
        Vector2 topLeft = new Vector2(x, y + this._tiles.getTileSize());
        Vector2 topRight = new Vector2(x + this._tiles.getTileSize(), y + this._tiles.getTileSize());
        Vector2 bottomRight = new Vector2(x + this._tiles.getTileSize(), y);

        super._shapeRenderer.setColor(0,0,0,1);

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
