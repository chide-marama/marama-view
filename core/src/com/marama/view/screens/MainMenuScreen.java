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
import com.marama.view.View;

/**
 * This class is a {@link Stage} that defines the 2d context and acts as a {@link Screen} for rendering the main menu.
 */
public class MainMenuScreen extends Stage implements Screen {
    final private View view;
    private Skin skin;

    /**
     * Instantiates a {@link Stage} that acts as a main menu.
     *
     * @param view Is used for updating the screen from the main menu.
     * @param skin
     */
    public MainMenuScreen(final View view, Skin skin) {
        super();

        this.view = view;
        this.skin = skin;

        addActor(new Table());

        final TextButton button = new TextButton("Play game!", skin, "default");
        final Container<Actor> container = new Container<Actor>(button);

        container.setX(getViewport().getScreenWidth() / 2);
        container.setY(getViewport().getScreenHeight() / 2);

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
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
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
