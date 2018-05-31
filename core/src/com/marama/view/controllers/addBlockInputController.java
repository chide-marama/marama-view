package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.MBlock;
import com.marama.view.entities.instances.EntityInstance;
import com.marama.view.renderables.World;

public class addBlockInputController extends InputAdapter {
    private World world;
    MBlock mBlock;
    int currentFaceIndex;
    int targetFaceIndex;

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
        EntityInstance instance = world.getModelInstance(ray);
        if(instance!=null){
            currentFaceIndex = world.getClosestFaceIndex(ray, instance);
            world.addBlock(instance, currentFaceIndex);
        }
        return false; // Continue to the next 'touchUp' listener.
    }

}
