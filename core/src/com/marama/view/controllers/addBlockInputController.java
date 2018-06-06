package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;

public class addBlockInputController extends InputAdapter {
    private World world;
    private EntityManager entityManager = EntityManager.getInstance();
    private int currentFaceIndex;
    private int targetFaceIndex;

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param world The ({@link World}) instance that renders 3D models.
     */
    public addBlockInputController(World world) {
        this.world = world;
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
        Ray ray = world.getRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance)world.getModelInstance(ray);
        if(instance!=null){
            SelectableInstance targetInstance = entityManager.createSelectableInstance(instance.name);//Get from add object to world.
            currentFaceIndex = world.getClosestFaceIndex(ray, instance);
            targetFaceIndex = getFaceIndex(currentFaceIndex, targetInstance);
            //world.addBlocktoFace((SelectableInstance)instance, currentFaceIndex, "donut");
            world.addFacetoFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex+3)%5));
        }
        return false; // Continue to the next 'touchUp' listener.
    }

    //TODO:
    private int getFaceIndex(int currentFaceIndex, ModelInstance targetInstance) {

        return 1;

    }

}
