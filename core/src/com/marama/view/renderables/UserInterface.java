package com.marama.view.renderables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.util.BackgroundColor;

public class UserInterface extends Stage implements Renderable {

    /**
     *
     * @param viewport
     * @param skin
     */
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
        float height = (blockSize + padding) * files.length;
        t.setWidth(blockSize + (padding * 2));
        t.setHeight(height);

        t.setPosition(0f, viewport.getScreenHeight() - height);

        BackgroundColor bgColor = new BackgroundColor("white_color_texture.png");
        bgColor.setColor(200, 20, 20, 255);
        t.setBackground(bgColor);
        addActor(t);

    }

    /**
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        act(delta);
        draw();
    }

    /**
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     *
     */
    @Override
    public void pause() {

    }

    /**
     *
     */
    @Override
    public void resume() {

    }
}
