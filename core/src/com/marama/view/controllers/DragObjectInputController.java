package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;
import com.marama.view.screens.GameScreen;


public class DragObjectInputController extends InputAdapter {
    private GameScreen gameScreen;
    private SelectableInstance selectableInstance = null;
    private ActiveAxis activeAxis = null;
    private Vector3 translation = null;
    private Vector3 intersection = new Vector3();
    private Vector3 distanceClickedFromInstance = new Vector3();
    private Vector2 lastTouch = null;

    public DragObjectInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
            distanceClickedFromInstance = intersection.sub(selectableInstance.getPosition());

        }

        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectableInstance != null && activeAxis != null) {
            // Calculate the difference between this touchDragged call and the previous.
            System.out.print("");
            if (lastTouch == null) {
                lastTouch = new Vector2(screenX, screenY);
            }
            Vector2 newTouch = new Vector2(screenX, screenY);
            Vector2 delta = newTouch.cpy().sub(lastTouch);

            //Ray ray = gameScreen.world.getPerspectiveCamera().getPickRay(screenX, screenY);
            //Vector3 inter = new Vector3(intersection); // Temp vector since substraction updates the vector object and there is no static method.
            //Vector3 rayDir = new Vector3(ray.direction); // See above comment, but now regarding cross product.
            //Vector3 rayOri = new Vector3(ray.origin);
            //float dist2 = rayDir.crs(inter.sub(ray.origin)).len();
            //float dist2 = Vector3.dst(rayOri.x, rayOri.y, rayOri.z, selectableInstance.getPosition().x, selectableInstance.getPosition().y, selectableInstance.getPosition().z);

            Vector3 mouseMovementInWorld = projectScreenToWorldSpace(delta);

            // Calculate the new position of the object.
            translation = selectableInstance.getPosition();
            switch (activeAxis) {
                case X:
                    translation.x =+ Vector3.dot(mouseMovementInWorld.x, mouseMovementInWorld.y, mouseMovementInWorld.z, 1, 0, 0);
                    break;
                case Y:
                    translation.y =+ Vector3.dot(mouseMovementInWorld.x, mouseMovementInWorld.y, mouseMovementInWorld.z, 0, 1, 0);
                    break;
                case Z:
                    translation.z =+ Vector3.dot(mouseMovementInWorld.x, mouseMovementInWorld.y, mouseMovementInWorld.z, 0, 0, 1);
                    break;
            }
            // Animate the selectable instance.
            selectableInstance.transform.setTranslation(translation);

            lastTouch = newTouch; // Update the latest touch data.
            return true; // Block the next 'touchDragged' listener.
        }
        return false; // Continue to the next 'touchDragged' listener.
    }

    /**
     * Converts a Vec2 representing screen space to a Vec3 world space by unprojection.
     * No side effects, unlike LibGDX its functions.
     * @param screenSpace Vec2 representing mouse location for example.
     * @return Vec3 representing screenSpace in world space.
     */
    private Vector3 projectScreenToWorldSpace(Vector2 screenSpace) {
        Vector3 worldSpace = new Vector3();
        gameScreen.world.getPerspectiveCamera().unproject(worldSpace.set(screenSpace.x, screenSpace.y, 0));
        return  worldSpace;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectableInstance != null) {
            // Apply new position to selectableInstance

            if (translation != null) {
                selectableInstance.updatePosition();
            }

            // Reset.
            selectableInstance = null;
            translation = null;
            activeAxis = null;
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    private enum ActiveAxis {
        X,
        Y,
        Z
    }
}
