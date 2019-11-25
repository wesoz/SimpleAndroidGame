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
    int _tileSize = 200;
    float _offset;

    public Tile(int value) {
        this();
        this.setValue(value);
    }

    public Tile() {
        this._bitmapFont = new BitmapFont();
        this._bitmapFont.setColor(Color.BLACK);
        this._bitmapFont.getData().setScale(5);
        this._bgColor = new Color(Color.WHITE);
        this._offset = this._tileSize / 2;
    }

    public void update() {}

    public boolean match(Tile tile) { return this._value == tile._value; }

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

    public void drawSquare(ShapeRenderer shapeRenderer, Vector2 position) {
        shapeRenderer.setColor(this._bgColor);
        shapeRenderer.rect(position.x, position.y, this._tileSize, this._tileSize);
    }

    public void writeValue(SpriteBatch spriteBatch, Vector2 position) {
        if (_value != 0) {
            this._bitmapFont.draw(spriteBatch,
                                  String.valueOf(_value),
                                position.x + (_offset * 0.75f),
                                position.y + (_offset * 1.25f ));
        }
    }

    public void dispose() {
        this._bitmapFont.dispose();
    }
}
