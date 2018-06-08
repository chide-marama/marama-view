package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
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
    private Vector3 targetFace;
    private BlendingAttribute blendingAttribute = new BlendingAttribute();
    private ColorAttribute colorAttribute = ColorAttribute.createDiffuse(new Color(0x00ff00aa));
    private Material moveMaterial = new Material(blendingAttribute, colorAttribute);

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param gameScreen The ({@link GameScreen}) instance that renders 3D models.
     */
    public AddBlockInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        blendingAttribute.opacity = 0.25f;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        targetInstance = createPreview();
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance worldInstance = (SelectableInstance) gameScreen.world.getModelInstance(ray);


        if (worldInstance != null) {
            addTargetInstance(ray);
        }
        return false; // Continue to the next 'touchDown' listener.
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
            if (!targetInstance.isSelected()) {
                addTargetInstance(ray);
            }
            if (targetInstance.isSelected()) {
                moveTargetInstance(ray);

            }
        return targetInstance.isSelected(); // Continue to the next 'touchDragged' listener.
    }

    private void moveTargetInstance(Ray ray) {
        SelectableInstance worldInstance = (SelectableInstance) gameScreen.world.getModelInstance(ray);
        if(worldInstance!=null && worldInstance!=targetInstance) {
            currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, worldInstance);
            targetFace = getFace(currentFaceIndex, targetInstance);
            gameScreen.world.moveFaceToFaceBasic(worldInstance, targetInstance, worldInstance.faces.get(currentFaceIndex), targetFace);
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!targetInstance.isSelected()) {
            Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
            addTargetInstance(ray);
        }

        if (targetInstance!=null) {
            targetInstance.resetMaterial();
            targetInstance.setSelected(false);
            targetInstance=null;
        }

        return false; // Continue to the next 'touchUp' listener.
}


    private void addTargetInstance(Ray ray) {
        SelectableInstance worldInstance = (SelectableInstance) gameScreen.world.getModelInstance(ray);
        if(worldInstance!=null) {
            currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, worldInstance);
            targetFace = getFace(currentFaceIndex, targetInstance);
            gameScreen.world.addFaceToFaceBasic(worldInstance, targetInstance, worldInstance.faces.get(currentFaceIndex), targetFace);
            targetInstance.setSelected(true);
        }
    }

    //TODO:Make this work for things other than cubes
    private Vector3 getFace(int currentFaceIndex, SelectableInstance targetInstance) {

        int targetFaceIndex = (currentFaceIndex+3)%6;
        return targetInstance.faces.get(targetFaceIndex);

    }

    private SelectableInstance createPreview(){
        SelectableInstance preview = entityManager.createSelectableInstance(gameScreen.getActiveMarama());
        preview.setMaterial(moveMaterial);
        return preview;
    }

}
