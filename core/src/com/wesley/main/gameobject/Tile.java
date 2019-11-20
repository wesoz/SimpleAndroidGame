package com.wesley.main.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    SpriteBatch _spriteBatch;
    ShapeRenderer _shapeRenderer;
    BitmapFont _bitmapFont;
    Color _bgColor;
    int _value;
    Vector2 _position;
    int _tileSize = 200;
    int _borderWeight;
    int _innerTileSize;
    float _offset;
    float _innerOffset;

    public Tile(Vector2 position) {
        this();
        this.setPosition(position);
        this.setBorderWeight(10);
    }

    public Tile(int value, Vector2 position) {
        this();
        this.setValue(value);
        this.setPosition(position);
        this.setBorderWeight(30);
    }

    public Tile() {
        this._spriteBatch = new SpriteBatch();
        this._shapeRenderer = new ShapeRenderer();
        this._bitmapFont = new BitmapFont();
        this._bitmapFont.setColor(Color.BLACK);
        this._bitmapFont.getData().setScale(5);
        this._bgColor = new Color(Color.WHITE);
    }

    public void update() {}

    public boolean match(Tile tile) { return this._value == tile._value; }

    public void setBorderWeight(int _borderWeight) {
        this._borderWeight = _borderWeight;
        this._innerTileSize = this._tileSize - this._borderWeight;
        this._offset = this._tileSize / 2;
        this._innerOffset = this._innerTileSize / 2;
    }
    public void setPosition(Vector2 position) {
        this._position = position;
    }
    public void setValue(int value) {
        this._value = value;
        switch (this._value) {
            case 0:
                this._bgColor.set(1, 1, 1, 1);
                break;
            case 2:
                this._bgColor.set(250 / 255.0f, 242 / 255.0f, 180 / 255.0f, 1);
                break;
            case 4:
                this._bgColor.set(245 / 255.0f, 227 / 255.0f, 86 / 255.0f, 1);
                break;
            case 8:
                this._bgColor.set(245 / 255.0f, 171 / 255.0f, 73 / 255.0f, 1);
                break;
            case 16:
                this._bgColor.set(240 / 255.0f, 126 / 255.0f, 19 / 255.0f, 1);
                break;
            case 32:
                this._bgColor.set(247 / 255.0f, 119 / 255.0f, 54 / 255.0f, 1);
                break;
            case 64:
                this._bgColor.set(224 / 255.0f, 57 / 255.0f, 31 / 255.0f, 1);
                break;
            case 128:
            case 256:
            case 512:
                this._bgColor.set(219 / 255.0f, 87 / 255.0f, 35 / 255.0f, 1);
                break;
            case 1024:
                this._bgColor.set(150 / 255.0f, 219 / 255.0f, 37 / 255.0f, 1);
                break;
            case 2048:
                this._bgColor.set(109 / 255.0f, 212 / 255.0f, 36 / 255.0f, 1);
                break;
            default:
                this._bgColor.set(30 / 255.0f, 179 / 255.0f, 30 / 255.0f, 1);
        }
    }

    public void draw() {
        this._shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._shapeRenderer.setColor(Color.BLACK);
        this._shapeRenderer.rect(this._position.x - this._offset, this._position.y - this._offset, this._tileSize, this._tileSize);
        this._shapeRenderer.setColor(this._bgColor);
        this._shapeRenderer.rect(this._position.x, this._position.y - this._innerOffset, this._innerTileSize, this._innerTileSize);
        this._shapeRenderer.end();
        this._spriteBatch.begin();

        this._bitmapFont.draw(this._spriteBatch, String.valueOf(_value) ,this._position.x, this._position.y+30, 0, 1, false);
        this._spriteBatch.end();

    }

    public void dispose() {
        this._spriteBatch.dispose();
        this._shapeRenderer.dispose();
        this._bitmapFont.dispose();
    }
}
