package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.screens.GameScreen;

public class DeleteObjectInputController extends InputAdapter {
    private final GameScreen gameScreen;

    public DeleteObjectInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) gameScreen.world.getClosestModelInstance(ray);

        if (instance != null) {
            gameScreen.world.deleteObject(instance);
        }

        return false; // Continue to the next 'touchDragged' listener.
    }
}
