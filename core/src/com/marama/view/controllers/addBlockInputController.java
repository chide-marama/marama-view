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
    private SelectableInstance targetInstance;
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
         // Get from add object to world.

        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = (SelectableInstance) world.getModelInstance(ray);
        if(instance==null)
            return false;

        if(targetInstance==null){
            targetInstance = entityManager.createSelectableInstance(world.getNextMarama());
            targetInstance.toggleSelected();
            world.addFacetoFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex + 3) % 6));
        }
        if (targetInstance!=instance) {
            currentFaceIndex = world.getClosestFaceIndex(ray, instance);
            targetFaceIndex = getFaceIndex(currentFaceIndex, targetInstance);
            //world.addBlocktoFace((SelectableInstance)instance, currentFaceIndex, "donut");
            world.moveFacetoFaceBasic(instance, targetInstance, instance.faces.get(currentFaceIndex), targetInstance.faces.get((currentFaceIndex + 3) % 6));
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
