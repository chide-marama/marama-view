package com.marama.view.renderables;

import com.marama.view.View;

public interface Renderable {
    /**
     * Called when the {@link View} is resized.
     *
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    void resize(int width, int height);

    /**
     * Called when the {@link View} renders itself.
     * @param delta The time in seconds since the last render.
     */
    void render(float delta);

    /**
     *	Called when the {@link View} is paused, usually when it's not active or visible on screen. The {@link View} is also
     * 	paused before it is destroyed.
     */
    void pause();

    /**
     * Called when the {@link View} is resumed from a paused state, usually when it regains focus.
     */
    void resume();

    /**
     * Called when the {@link View} is destroyed. Preceded by a call to {@link #pause()}.
     */
    void dispose();
}
