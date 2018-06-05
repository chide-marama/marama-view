package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.MBlock;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;

public class addBlockInputController extends InputAdapter {
    private World world;
    MBlock mBlock;
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
        ModelInstance instance = world.getModelInstance(ray);
        ModelInstance targetInstance = world.addObject();//Get from add object to world.
        if(instance!=null){
            currentFaceIndex = world.getClosestFaceIndex(ray, (SelectableInstance)instance);
            targetFaceIndex = getFaceIndex(currentFaceIndex, targetInstance);
            //world.addBlocktoFace((SelectableInstance)instance, currentFaceIndex);
            world.addFacetoFaceBasic((SelectableInstance) instance, (SelectableInstance) targetInstance, ((SelectableInstance) instance).faces.get(currentFaceIndex), ((SelectableInstance) targetInstance).faces.get((currentFaceIndex+3)%5));
        }
        return false; // Continue to the next 'touchUp' listener.
    }

    //TODO:
    private int getFaceIndex(int currentFaceIndex, ModelInstance targetInstance) {

        return 1;

    }

}
