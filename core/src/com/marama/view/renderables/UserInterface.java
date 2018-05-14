package com.marama.view.renderables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.utils.BackgroundColor;

public class UserInterface extends Stage implements Renderable {

    public UserInterface(Viewport viewport, Skin skin) {
        super(viewport);
        Table t = new Table();

        float blockSize = 80f;
        float padding = 10f;
        t.left().top();


        FileHandle[] files = Gdx.files.internal("test_Ms").list();
        for (final FileHandle file : files) {
            Texture texture = new Texture(file.path());
            Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
            ImageButton button = new ImageButton(drawable);
            button.setWidth(blockSize);
            button.setHeight(blockSize);
            t.add(button).padLeft(padding).width(blockSize).height(blockSize).padTop(padding);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(file.name());
                }
            });
            t.row();
        }
//        t.setFillParent(true);
        float height = (blockSize + padding) * files.length;
        t.setWidth(blockSize + (padding * 2));
        t.setHeight(height);

        t.setPosition(0f, viewport.getScreenHeight() - height);

        BackgroundColor bgColor = new BackgroundColor("white_color_texture.png");
        bgColor.setColor(200, 20, 20, 255);
        t.setBackground(bgColor);
        addActor(t);


//
//        button.setWidth(50f);
//        button.setHeight(50f);
//        final Container<Actor> container = new Container<Actor>(button);
//        container.setWidth(300f);
//        container.setHeight(300f);
//        container.setX(100f);
//        container.setY(100f);

//
//        final Dialog dialog = new Dialog("Nee, dat is niet hoe het werkt, vriend!", skin);
//
//        dialog.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                dialog.hide();
//            }
//        });
//        // Used to remember this inside the implicit function definition.
//        final UserInterface self = this;
//        button.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                dialog.show(self);
//
//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        dialog.hide();
//                    }
//                }, 3);
//            }
//        });

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
}
