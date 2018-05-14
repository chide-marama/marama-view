package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.View;

public class MainMenuScreen extends Stage implements Screen {
    final private View view;
    private Skin skin;

    public MainMenuScreen(final View view, Viewport viewport, Skin skin) {
        super(viewport);

        this.view = view;
        this.skin = skin;

        addActor(new Table());

        final TextButton button = new TextButton("Play game!", skin, "default");
        final Container<Actor> container = new Container<Actor>(button);

        container.setX((viewport.getScreenWidth() / 2));
        container.setY((viewport.getScreenHeight() / 2));

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.setScreen(new GameScreen());
            }
        });
        this.addActor(container);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        act(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        view.dispose();
        skin.dispose();
    }
}
