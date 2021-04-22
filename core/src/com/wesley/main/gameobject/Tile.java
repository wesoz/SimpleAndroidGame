package com.wesley.main.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wesley.main.animations.Animation;
import com.wesley.main.animations.SquareAnimationFrame;

public class Tile {
    private BitmapFont _bitmapFont;
    private Color _bgColor;
    private int _value;
    private int _tileSize;
    private float _offset;
    private Vector2 _position;
    private float _speed;
    private int _x;
    private int _y;
    private Tile _mergeTile;
    private TileMovement _tileMovement;
    private float _totalElapsedTime;
    private Animation _mergeAnimation;
    private boolean _shouldWriteValue;

    public float getSpeed() {
        return _speed;
    }

    public Tile(int tileSize, int value, Vector2 position) {
        this();
        this._tileSize = tileSize;
        this._offset = this._tileSize / 2;
        this.setValue(value);
        this.setPosition(position);
    }

    public Tile(int value, int tileSize, float offset,
                Vector2 position, float speed, int x, int y) {
        this(tileSize, value, position);
        this._offset = offset;
        this._speed = speed;
        this._x = x;
        this._y = y;
    }

    public void move(float deltaX, float deltaY) {
        float x = this._position.x + deltaX;
        float y = this._position.y + deltaY;
        this._position.set(new Vector2(x, y));
    }

    public Tile() {
        this._bitmapFont = new BitmapFont();
        this._bitmapFont.setColor(Color.BLACK);
        this._bgColor = new Color(Color.WHITE);
        this._speed = 50f;
        this._mergeTile = null;
        this._tileMovement = null;
        this._totalElapsedTime = 0;
        this._mergeAnimation = new Animation();
        this._shouldWriteValue = true;
    }

    public void setTileMovement(TileMovement tileMovement) {
        this._tileMovement = tileMovement;
    }

    public TileMovement getTileMovement() {
        return this._tileMovement;
    }

    public void doubleValue() {
        this.setValue(this._value * 2);
        this.setMergeAnimationFrames();
    }

    public boolean match(Tile tile) {
        if (tile == null) return false;

        return this._value == tile._value;
    }

    public int getValue() {
        return this._value;
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

    private Vector2 getCentralizedVector2(float outerSquare, float innerSquare) {
        return new Vector2((outerSquare - innerSquare) / 2f, (outerSquare - innerSquare) / 2f);
    }

    private void setMergeAnimationFrames() {
        SquareAnimationFrame[] animationFrames = new SquareAnimationFrame[5];

        float duration = 2f / 60;

        Color color = new Color(this._bgColor.r, this._bgColor.g, this._bgColor.b, 0.3f);
        float squareSize = this._tileSize * 1.5f;
        ;
        Vector2 offset = this.getCentralizedVector2(this._tileSize, squareSize); // new Vector2(squareOffset, squareOffset); //new Vector2(this._innerSquareOffset, this._innerSquareOffset);
        animationFrames[0] = new SquareAnimationFrame(offset, color, squareSize, duration);

        squareSize = this._tileSize * 1.4f;
        offset = this.getCentralizedVector2(this._tileSize, squareSize);
        color = new Color(this._bgColor.r, this._bgColor.g, this._bgColor.b, 0.4f);
        animationFrames[1] = new SquareAnimationFrame(offset, color, squareSize, duration);

        squareSize = this._tileSize * 1.3f;
        offset = this.getCentralizedVector2(this._tileSize, squareSize);
        color = new Color(this._bgColor.r, this._bgColor.g, this._bgColor.b, 0.5f);
        animationFrames[2] = new SquareAnimationFrame(offset, color, squareSize, duration);

        squareSize = this._tileSize * 1.2f;
        color = new Color(this._bgColor.r, this._bgColor.g, this._bgColor.b, 0.6f);
        offset = this.getCentralizedVector2(this._tileSize, squareSize);
        animationFrames[3] = new SquareAnimationFrame(offset, color, squareSize, duration);

        squareSize = this._tileSize * 1.1f;
        color = new Color(this._bgColor.r, this._bgColor.g, this._bgColor.b, 0.7f);
        offset = this.getCentralizedVector2(this._tileSize, squareSize);
        animationFrames[4] = new SquareAnimationFrame(offset, color, squareSize, duration);

        this._mergeAnimation.replaceFrames(animationFrames, Animation.REPEAT.ONCE);
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        this.drawSquare(shapeRenderer);
        this.writeValue(spriteBatch);
    }

    public void drawSquare(ShapeRenderer shapeRenderer) {
        Vector2 position = this.getPosition();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this._totalElapsedTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        SquareAnimationFrame mergeFrame = (SquareAnimationFrame) this._mergeAnimation.getNextFrame(this._totalElapsedTime);
        if (mergeFrame != null) {
            this._shouldWriteValue = false;
            shapeRenderer.setColor(mergeFrame.getColor());
            shapeRenderer.rect(position.x + mergeFrame.getOffset().x, position.y + mergeFrame.getOffset().y, mergeFrame.getSize(), mergeFrame.getSize());
        } else {
            this._shouldWriteValue = true;
            shapeRenderer.setColor(this._bgColor);
            shapeRenderer.rect(position.x, position.y, this._tileSize, this._tileSize);

            float innerSquareSize = this._tileSize * 0.8f;
            Vector2 offset = this.getCentralizedVector2(this._tileSize, innerSquareSize);

            shapeRenderer.setColor(new Color(this._bgColor.r + 0.3f, this._bgColor.g + 0.3f, this._bgColor.b + 0.3f, 1f));
            shapeRenderer.rect(position.x + offset.x, position.y + offset.y, innerSquareSize, innerSquareSize);
        }
        shapeRenderer.end();
    }

    public void writeValue(SpriteBatch spriteBatch) {
        if (this._shouldWriteValue) {
            spriteBatch.begin();
            Vector2 position = this.getPosition();
            String value = String.valueOf(this._value);

            //Adjust the font size according to the size of the number so it fits the tile;
            //The bigger the number, the smaller the font;
            this._bitmapFont.getData().setScale(5f - ((float) value.length() * 0.4f));
            this._bitmapFont.draw(spriteBatch,
                    value,
                    //Adjust the text offset according to the size of the font;
                    //The smaller the font, more to the left;
                    position.x + (this._offset * (0.85f - ((value.length() - 1) * 0.12f))),
                    position.y + (this._offset * 1.2f));
            spriteBatch.end();
        }
    }

    public void dispose() {
        this._bitmapFont.dispose();
    }

    public Vector2 getPosition() {
        return _position;
    }

    public void setPosition(Vector2 position) {
        this._position = position;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public void set(int x, int y) {
        this._x = x;
        this._y = y;
    }

    public int getTileSize() {
        return this._tileSize;
    }

    public float getOffset() {
        return this._offset;
    }

    public Tile getMergeTile() {
        return this._mergeTile;
    }

    public void setMergeTile(Tile tileToMerge) {
        this._mergeTile = tileToMerge;
    }

    public Tile clone() {
        Vector2 position = new Vector2(this._position);

        return new Tile(this._value, this._tileSize, this._offset, position,
                this._speed, this._x, this._y);
    }
}
