package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.screens.GameScreen;

public class DeleteObjectInputController extends InputAdapter {
    private final GameScreen gameScreen;
    private SelectableInstance deletionSelected = null;
    public DeleteObjectInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    private final float deletionOpacity = 0.5f;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) gameScreen.world.getClosestModelInstance(ray);
        if (instance != null) {
            if (deletionSelected != null) {
                deletionSelected.setOpacity(1f);
            }
            if (instance == deletionSelected) {
                gameScreen.world.deleteObject(instance);
                deletionSelected = null;
            } else {
                deletionSelected = ((SelectableInstance) gameScreen.world.getClosestModelInstance(ray));
                deletionSelected.setOpacity(deletionOpacity);
            }
        } else {
            if (deletionSelected != null) {
                deletionSelected.setOpacity(1f);
                deletionSelected = null;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return deletionSelected != null;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) gameScreen.world.getClosestModelInstance(ray);

        return false; // Continue to the next 'touchDragged' listener.
    }
}
