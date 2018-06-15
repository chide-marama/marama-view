package com.marama.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.marama.view.View;

/**
 * This class acts as a splash screen that shows the Marama logo on application startup.
 */
public class SplashScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final Sprite splash;
    private float elapsedTime;

    private final View view;
    private final Viewport viewport;

    private final double SPLASH_DURATION = 2.0; // Total duration for the splash screen. From start of the app to appearance of the main menu.
    private final double FADE_OUT_START = SPLASH_DURATION * 0.6; // The duration is tweak-able. Try to keep it between 0.1 and 0.9 for best effect.

    public SplashScreen(final View view, Viewport viewport) {
        this.view = view;
        this.viewport = viewport;

        this.batch = new SpriteBatch();
        Texture logo = new Texture(Gdx.files.internal("MaramaLogo.png"));
        this.splash = new Sprite(logo, 0, 0, logo.getWidth(), logo.getHeight());
        this.elapsedTime = 0;
    }

    @Override
    public void render(float delta) {
        // Clear screen and reset active texture (just in case).
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        // Keep track of elapsed time.
        elapsedTime += delta;

        // Manually update the camera. Otherwise the resize difference is applied two times to the image.
        // Once by the viewport (and its camera) and once by the SpriteBatch.
        // To avoid this tell the batch that it needs to use the camera its mv matrix.
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);

        // Start a drawing task.
        batch.begin();
        if (elapsedTime > FADE_OUT_START) {
            // Set the alpha to engage a fade out animation.
            batch.setColor(batch.getColor().r,
                    batch.getColor().g,
                    batch.getColor().b,
                    newAlpha(elapsedTime));
        }
        batch.draw(splash, -viewport.getScreenWidth() / 2, -viewport.getScreenHeight() / 2, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        // Exit after the duration has elapsed.
        if (elapsedTime > SPLASH_DURATION)
            this.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        // Advance to the main menu.
        view.setScreen(new MainMenuScreen(view));
    }

    /**
     * Helper function for Render().
     * Gives the alpha value for a linear fade-out as a function of time.
     *
     * @param currentTime The elapsed time of the animation.
     * @return alpha value between zero and one.
     */
    private float newAlpha(double currentTime) {
        double progressSinceBreakPoint = (currentTime - FADE_OUT_START) / (SPLASH_DURATION - FADE_OUT_START);
        return (float) Math.max(1 - progressSinceBreakPoint, 0); // Avoid negative values
    }
}
