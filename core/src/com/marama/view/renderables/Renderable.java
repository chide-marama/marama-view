package com.marama.view.renderables;

public interface Renderable {
    /**
     * @param width
     * @param height
     */
    void resize(int width, int height);

    /**
     * @param delta
     */
    void render(float delta);

    /**
     *
     */
    void pause();

    /**
     *
     */
    void resume();

    /**
     *
     */
    void dispose();
}
