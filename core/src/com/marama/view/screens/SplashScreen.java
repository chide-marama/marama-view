package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.View;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private float ellapsedTime;

    private final View view;
    private Viewport viewport;
    private Skin skin;

    private final float SPLASH_DURATION = 3;

    public SplashScreen(final View view, Viewport viewport, Skin skin) {
        this.view = view;
        this.viewport = viewport;
        this.skin = skin;

        this.batch = new SpriteBatch();
        this.texture = new Texture(Gdx.files.internal("MaramaLogo.png"));
        this.sprite = new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight());
        this.ellapsedTime = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // TODO optimization: resize img dimensions to powers of two
        // Clear screen and reset active texture (just in case).
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        // Keep track of ellapsed time and fade out img after half-time.
        ellapsedTime += delta;
        if (ellapsedTime > SPLASH_DURATION / 2) {
            sprite.setAlpha(sprite.getColor().a - 255 / (delta / (SPLASH_DURATION / 2)));
        }

        // Start a drawing task.
        batch.begin();
        batch.draw(sprite, 0, 0);
        batch.end();

        if (ellapsedTime > SPLASH_DURATION)
            this.dispose();
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
        view.setScreen(new MainMenuScreen(view, viewport, skin));
    }
}
