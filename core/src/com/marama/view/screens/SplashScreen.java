package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.View;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Sprite splash;
    private Image splashImage;
    private float elapsedTime;

    private final View view;
    private Viewport viewport;
    private Skin skin;

    private final double SPLASH_DURATION = 6;

    public SplashScreen(final View view, Viewport viewport, Skin skin) {
        this.view = view;
        this.viewport = viewport;
        this.skin = skin;

        this.batch = new SpriteBatch();
        Texture logo = new Texture(Gdx.files.internal("MaramaLogo.png"));
        this.splash = new Sprite(logo, 0, 0, logo.getWidth(), logo.getHeight());
        splashImage = new Image(splash);
        this.elapsedTime = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        final double HALF_DUR = SPLASH_DURATION / 2;
        // TODO optimization: resize img dimensions to powers of two
        // Clear screen and reset active texture (just in case).
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        // Keep track of elapsed time and fade out img after half-time.
        elapsedTime += delta;
        double progressSinceHalf = (elapsedTime - HALF_DUR) / HALF_DUR;
        double newAlpha = Math.max(1 - progressSinceHalf, 0); // Avoid negative values

        // Manually update the camera.
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);

        // Start a drawing task.
        batch.begin();
        if (elapsedTime > SPLASH_DURATION / 2) {
            // Set the alpha to engage a fade out animation.
            batch.setColor( batch.getColor().r,
                            batch.getColor().g,
                            batch.getColor().b,
                            (float) newAlpha);
        }
        splashImage.getDrawable().draw(batch, -viewport.getScreenWidth() / 2, -viewport.getScreenHeight() / 2, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        // Exit after the duration has elapsed.
        if (elapsedTime > SPLASH_DURATION)
            this.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.getCamera().update();
        System.out.println("graphics: (" + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight() + ")\n"
                + "viewport: (" + viewport.getScreenWidth() + ", " + viewport.getScreenHeight() + ")\n"
                + "viewportXY: (" + viewport.getScreenX() + ", " + viewport.getScreenY() + ")\n"
                + "viewportWorld: (" + viewport.getWorldWidth() + ", " + viewport.getWorldHeight() + ")\n");
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
