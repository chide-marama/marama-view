package com.marama.game;

public interface Renderable {
    void resize(int width, int height);
    void render();
    void pause();
    void resume();
    void dispose();
}
