package com.marama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UserInterface extends Table {
    public Skin Skin;
    public Stage Stage;

    public void create(){
        Skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
        Stage = new Stage(new ScreenViewport());


        final TextButton button =  new TextButton("Click me", Skin, "default");
        button.setWidth(50f);
        button.setHeight(50f);
        final Container<Actor> container = new Container<Actor>(button);
        container.setWidth(300f);
        container.setHeight(300f);
        container.setX(100f);
        container.setY(100f);


        final Dialog dialog = new Dialog("Nee, dat is niet hoe het werkt, vriend!", Skin);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.show(Stage);

                dialog.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        dialog.hide();
                    }
                });
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run(){
                        dialog.hide();
                    }
                }, 3);
            }
        });

        Stage.addActor(container);
    }

    public void render(){
        Stage.act(Gdx.graphics.getDeltaTime());
        Stage.draw();
    }
}
