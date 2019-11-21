package com.wesley.main.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tile {
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
        this._innerOffset = this._borderWeight / 2;
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

    public void drawSquare(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(this._position.x, this._position.y, this._tileSize, this._tileSize);
        shapeRenderer.setColor(this._bgColor);
        shapeRenderer.rect(this._position.x + _innerOffset, this._position.y + _innerOffset, this._innerTileSize, this._innerTileSize);
    }

    public void writeValue(SpriteBatch spriteBatch) {
        if (_value != 0) {
            this._bitmapFont.draw(spriteBatch,
                                  String.valueOf(_value),
                                  this._position.x + (_offset * 0.75f),
                                  this._position.y + (_offset * 1.25f ));
        }
    }

    public void dispose() {
        this._bitmapFont.dispose();
    }
}
