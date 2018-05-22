package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.marama.view.View;
import com.marama.view.renderables.stages.MainMenuUserInterface;

/**
 * This class is a {@link Stage} that defines the 2d context and acts as a {@link Screen} for rendering the main menu.
 */
public class MainMenuScreen extends Stage implements Screen {
    private MainMenuUserInterface mainMenuUserInterface;

    /**
     * Instantiates a {@link Stage} that acts as a main menu.
     *
     * @param view Is used for updating the screen from the main menu.
     */
    public MainMenuScreen(final View view) {
        super();
        mainMenuUserInterface = new MainMenuUserInterface(view, new Skin(Gdx.files.internal("skin/uiskin.json")));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuUserInterface);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        mainMenuUserInterface.render(delta);
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

    }
}
