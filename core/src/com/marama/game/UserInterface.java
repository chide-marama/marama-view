package com.marama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UserInterface extends Stage implements Renderable {
    public Skin Skin;
    public Table Table;

    public UserInterface(Viewport viewport, String skinPath) {
        super(viewport);
        Skin = new Skin(Gdx.files.internal(skinPath));
        Table = new Table();
        addActor(Table);

        final TextButton button = new TextButton("Click me", Skin, "default");
        button.setWidth(50f);
        button.setHeight(50f);
        final Container<Actor> container = new Container<Actor>(button);
        container.setWidth(300f);
        container.setHeight(300f);
        container.setX(100f);
        container.setY(100f);


        final Dialog dialog = new Dialog("Nee, dat is niet hoe het werkt, vriend!", Skin);

        dialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        // Used to remember this inside the implicit function definition.
        final UserInterface self = this;
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.show(self);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                }, 3);
            }
        });

        addActor(container);
    }

    @Override
    public void render() {
        act(Gdx.graphics.getDeltaTime());
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
}
