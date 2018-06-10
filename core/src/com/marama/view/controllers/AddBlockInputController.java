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
    private final GameScreen gameScreen;
    private final EntityManager entityManager = EntityManager.getInstance();
    private final BlendingAttribute blendingAttribute = new BlendingAttribute();
    private final ColorAttribute colorAttribute = ColorAttribute.createDiffuse(new Color(0x00ff00aa));
    private final Material moveMaterial = new Material(blendingAttribute, colorAttribute);

    private SelectableInstance targetInstance;
    private SelectableInstance worldInstance;

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
        if(targetInstance!=null){
            gameScreen.world.deleteObject(targetInstance);
            targetInstance=null;
        }
        targetInstance = createPreview();
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        addTargetInstance(ray);
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(targetInstance==null){
            return false;
        }
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
            if (!targetInstance.isSelected()) {
                addTargetInstance(ray);
            }
            if (targetInstance.isSelected()) {
                moveTargetInstance(ray);
            }
        return targetInstance.isSelected(); // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (targetInstance!=null) {
            targetInstance.resetMaterial();
            targetInstance.setSelected(false);
            targetInstance=null;
        }

        return false; // Continue to the next 'touchUp' listener.
}

    /**
     * Moves the targetInstance around the world after calculating the position it ought to go in
     *
     * Question for the boys: Controller or World?
     * @param ray A ray cast from the position on screen where the user clicked.
     */
    private void moveTargetInstance(Ray ray) {
        SelectableInstance worldInstance = (SelectableInstance) gameScreen.world.getClosestModelInstance(ray);
        if (worldInstance != null && worldInstance != targetInstance) {
            int currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, worldInstance);
            Vector3 targetFace = getFace(currentFaceIndex, targetInstance);
            gameScreen.world.moveFaceToFaceBasic(worldInstance, targetInstance, worldInstance.faces.get(currentFaceIndex), targetFace);
        }
    }

    /**
     * Adds the targetInstance to the world after calculating the position it ought to go in
     *
     * Question for the boys: Controller or World?
     * @param ray A ray cast from the position on screen where the user clicked.
     */
    private void addTargetInstance(Ray ray) {
        SelectableInstance worldInstance = (SelectableInstance) gameScreen.world.getClosestModelInstance(ray);
        if(worldInstance!=null) {
            int currentFaceIndex = gameScreen.world.getClosestFaceIndex(ray, worldInstance);
            Vector3 targetFace = getFace(currentFaceIndex, targetInstance);
            gameScreen.world.addFaceToFaceBasic(worldInstance, targetInstance, worldInstance.faces.get(currentFaceIndex), targetFace);
            targetInstance.setSelected(true);
        }
    }

    /**
     * Returns the Vector3 position of the face on the targetInstance that you
     *
     * Question for the boys: Controller or World?
     * @param currentFaceIndex The position in the faces array in the currentInstance
     * @param targetInstance   The instance of which you want to calculate the most fitting face
     * @return The face that is the best fit for the currentInstance
     */
    //TODO:Make this work for things other than cubes. Communicate with editor.
    private Vector3 getFace(int currentFaceIndex, SelectableInstance targetInstance) {

        int targetFaceIndex = (currentFaceIndex+3)%6;
        return targetInstance.faces.get(targetFaceIndex);

    }

    /**
     * Creates a 'preview' version of a {@link SelectableInstance} to make it stand out from the regular version
     *
     * Question for the boys: Controller or World?
     * @return The preview object as a {@link SelectableInstance}
     */
    private SelectableInstance createPreview(){
        SelectableInstance preview = entityManager.createSelectableInstance(gameScreen.getActiveMarama());
        preview.setMaterial(moveMaterial);
        return preview;
    }

}
