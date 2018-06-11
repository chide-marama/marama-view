package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.screens.GameScreen;

public class DragObjectInputController extends InputAdapter {
    private GameScreen gameScreen;
    private SelectableInstance selectableInstance = null;
    private ActiveAxis activeAxis = null;
    private Vector3 translation = null;
    private Vector3 intersection = new Vector3();
    private Vector2 lastTouch = null; // Used for keeping track of mouse position on screen between calls of touch handlers.

    public DragObjectInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch = new Vector2(screenX, screenY); // Used in touchDragged.
        Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = null;

        for (int i = 0; i < gameScreen.world.getModelInstances().size; ++i) {
            ModelInstance modelInstance = gameScreen.world.getModelInstances().get(i);
            if (modelInstance instanceof SelectableInstance) {
                instance = (SelectableInstance) modelInstance;

                // X axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxX, intersection)) {
                    activeAxis = ActiveAxis.X;
                    break;
                }

                // Y axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxY, intersection)) {
                    activeAxis = ActiveAxis.Y;
                    break;
                }

                // Z axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxZ, intersection)) {
                    activeAxis = ActiveAxis.Z;
                    break;
                }
            }
        }

        if (instance != null && instance.isSelected()) {
            selectableInstance = instance; // Keep track of the found instance.
            intersection.sub(selectableInstance.getPosition());
        }

        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectableInstance != null && activeAxis != null) {
            // Calculate the difference between this touchDragged call and the previous.
            Vector2 newTouch = new Vector2(screenX, screenY);

            // Calculate the difference between the mouse location in world space on this call and the previous call.
            Vector3 newTouchInWorld = projectScreenToWorldSpace(newTouch);
            Vector3 lastTouchInWorld = projectScreenToWorldSpace(lastTouch);
            Vector3 deltaInWorld = (newTouchInWorld.cpy().sub(lastTouchInWorld)).scl(10); // 10 speed modifier; otherwise movement is a crawl.

            // Assign the new position of the object based on the selected axis.
            translation = selectableInstance.getPosition();
            switch (activeAxis) {
                case X:
                    translation.x += deltaInWorld.x;
                    break;
                case Y:
                    translation.y += deltaInWorld.y;
                    break;
                case Z:
                    translation.z += deltaInWorld.z;
                    break;
            }

            // Animate the selectable instance.
            selectableInstance.transform.setTranslation(translation);
            lastTouch = newTouch; // Update the latest touch data.

            return true; // Block the next 'touchDragged' listener.
        }
        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectableInstance != null) {
            // Apply new position to selectableInstance

            if (translation != null) {
                selectableInstance.updateBoundingBoxPositions();
            }

            // Reset.
            selectableInstance = null;
            translation = null;
            activeAxis = null;
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    /**
     * Converts a Vec2 representing screen space to a Vec3 world space by unprojection.
     * No side effects, unlike LibGDX its functions.
     *
     * @param screenSpace Vec2 representing mouse location for example.
     * @return Vec3 representing screenSpace in world space.
     */
    private Vector3 projectScreenToWorldSpace(Vector2 screenSpace) {
        Vector3 worldSpace = new Vector3();
        gameScreen.world.getPerspectiveCamera().unproject(worldSpace.set(screenSpace.x, screenSpace.y, 0));
        return worldSpace;
    }

    /**
     * The value for tracking the current active axis.
     */
    private enum ActiveAxis {
        X,
        Y,
        Z
    }
}
