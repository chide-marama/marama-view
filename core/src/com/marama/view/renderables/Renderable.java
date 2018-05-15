package com.marama.view.renderables;

public interface Renderable {
    void resize(int width, int height);

    void render(float delta);

    void pause();

    void resume();

    void dispose();
}
