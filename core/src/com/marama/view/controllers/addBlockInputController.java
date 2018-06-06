package com.marama.view.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;
import com.marama.view.screens.GameScreen;

public class addBlockInputController extends InputAdapter {
    private GameScreen gameScreen;
    private EntityManager entityManager = EntityManager.getInstance();
    private int currentFaceIndex;
    private int targetFaceIndex;

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param gameScreen The ({@link GameScreen}) instance that renders 3D models.
     */
    public addBlockInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) gameScreen.world.getModelInstance(ray);

        if (instance != null) {
            SelectableInstance targetInstance = entityManager.createSelectableInstance(gameScreen.getActiveMarama()); // Get from add object to world.
            currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, instance);
            targetFaceIndex = getFaceIndex(currentFaceIndex, targetInstance);
            //world.addBlocktoFace((SelectableInstance)instance, currentFaceIndex, "donut");
            gameScreen.world.addFacetoFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex + 3) % 6));
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    //TODO:
    private int getFaceIndex(int currentFaceIndex, ModelInstance targetInstance) {

        return 1;

    }

}
