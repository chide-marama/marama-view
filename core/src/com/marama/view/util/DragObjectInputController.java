package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;


public class DragObjectInputController extends InputAdapter {
    private World world;
    private SelectableInstance selectableInstance = null;
    private ActiveAxis activeAxis = null;
    private Vector3 translation = null;
    private Vector3 intersection = new Vector3();
    private Vector3 distanceClickedFromInstance = new Vector3();

    public DragObjectInputController(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
        SelectableInstance instance = null;

        for (int i = 0; i < world.getModelInstances().size; ++i) {
            ModelInstance modelInstance = world.getModelInstances().get(i);
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
            Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
            float distance = -ray.origin.y / ray.direction.y; // TODO: This is not right
            Vector3 moved = new Vector3(ray.direction.scl(distance).add(ray.origin)).sub(distanceClickedFromInstance);
            translation = selectableInstance.getPosition();

            switch (activeAxis) {
                case X:
                    translation.x = moved.x;
                    break;
                case Y:
                    translation.y = (moved.z * -1);
                    break;
                case Z:
                    translation.z = moved.z;
                    break;
            }

            // Animate the selectable instance
            selectableInstance.transform.setTranslation(translation);

            return true; // Block the next 'touchDragged' listener.
        }

        return false; // Continue to the next 'touchDragged' listener.
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
