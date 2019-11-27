package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
                Board.this.startGame();
            }
        });

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

        this._font = new BitmapFont();
        this._font.setColor(Color.WHITE);
        this._font.getData().setScale(5);
    }

    public Screen update() {

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
