package com.marama.view.renderables.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.marama.view.View;
import com.marama.view.renderables.Renderable;
import com.marama.view.screens.GameScreen;

public class MainMenuUserInterface extends Stage implements Renderable {
    private View view;
    private Skin skin;

    public MainMenuUserInterface(final View view, Skin skin) {
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
    public void resize(int width, int height) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        act(delta);
        draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        view.dispose();
        skin.dispose();
    }
}
