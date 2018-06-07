package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;
import com.marama.view.screens.GameScreen;

public class AddBlockInputController extends InputAdapter {
    private GameScreen gameScreen;
    private EntityManager entityManager = EntityManager.getInstance();
    private SelectableInstance targetInstance;
    private int currentFaceIndex;
    private int targetFaceIndex;

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param gameScreen The ({@link GameScreen}) instance that renders 3D models.
     */
    public AddBlockInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) gameScreen.world.getModelInstance(ray);
        if(instance==null)
            return false;

        if(targetInstance==null){
            targetInstance = entityManager.createSelectableInstance(gameScreen.getActiveMarama());
            targetInstance.toggleSelected();
            gameScreen.world.addFaceToFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex + 3) % 6));
            targetInstance.setMaterial(new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        }
        if (targetInstance!=instance) {
            currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, instance);
            targetFaceIndex = getFaceIndex(currentFaceIndex, targetInstance);
            //world.addBlocktoFace((SelectableInstance)instance, currentFaceIndex, "donut");
            gameScreen.world.moveFacetoFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex + 3) % 6));
        }

        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        if (targetInstance!=null) {
            targetInstance.toggleSelected();
            targetInstance.resetMaterial();
            targetInstance=null;
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    //TODO:
    private int getFaceIndex(int currentFaceIndex, ModelInstance targetInstance) {

        return 1;

    }

}
