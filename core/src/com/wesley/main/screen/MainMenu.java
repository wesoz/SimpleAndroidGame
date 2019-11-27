package com.wesley.main.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenu extends Screen {

    Stage _stage;
    ImageButton _btn4x4;
    Screen _screen;
    Table _table;

    public MainMenu() {
        super();
        this._screen = this;
        this._stage = new Stage();
        Gdx.input.setInputProcessor(this._stage);
        this._table = new Table();
        createMenuOption(3);
        this._table.row();
        createMenuOption(4);
        this._table.row();
        createMenuOption(5);
        this._table.row();
        createMenuOption(6);
        this._table.row();
        createMenuOption(8);
    }

    private void createMenuOption(final int size) {
        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(5f);
        Texture upTexture = new Texture("buttons/start_game.png");
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(size + "x" + size, labelStyle);
        ImageButton button = new ImageButton(upDrawable);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenu.this._screen = new Board(size);
            }
        });
        this._table.add(label).width(250).height(250);
        this._table.add(button);
        this._table.setFillParent(true);
        this._stage.addActor(this._table);
    }

    @Override
    public Screen update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
        return this._screen;
    }

    @Override
    public void draw() {
        super._spriteBatch.begin();
        this._table.draw(super._spriteBatch, 1);
        super._spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
