package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;


public class DragObjectInputController extends InputAdapter {
    private World world;

    private SelectableInstance selectableInstance = null;
    private ActiveAxis activeAxis = null;

    private Vector3 currentInstancePosition = new Vector3();
    private Vector3 intersectionPoint = new Vector3();

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
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxX, intersectionPoint)) {
                    activeAxis = ActiveAxis.X;
                    break;
                }

                // Y axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxY, intersectionPoint)) {
                    activeAxis = ActiveAxis.Y;
                    break;
                }

                // Z axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxZ, intersectionPoint)) {
                    activeAxis = ActiveAxis.Z;
                    break;
                }
            }
        }

        if (instance != null) {
            selectableInstance = instance; // Keep track of the found instance.
            instance.transform.getTranslation(currentInstancePosition); // Set the current position of the instance.
            distanceClickedFromInstance = intersectionPoint.sub(currentInstancePosition);
        }

        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Keep casting a ray to update the instance position.
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
        // Distance where y is 0 (at the center of the World).
        float distance = (-ray.origin.y / ray.direction.y);
        // The position of the ray at the distance.
        Vector3 pickVector = new Vector3(ray.direction.scl(distance).add(ray.origin));

        // Subtract the difference from where was clicked.
        pickVector.sub(distanceClickedFromInstance);

        Vector3 translationVector = new Vector3(0, 0, 0);
        // Apply the movement to certain axes.
        if (selectableInstance != null && activeAxis != null) {
            switch (activeAxis) {
                case X:
                    translationVector.x = pickVector.x;
                    break;
                case Y:
                    translationVector.y = pickVector.y;
                    break;
                case Z:
                    translationVector.z = pickVector.z;
                    break;
            }
            selectableInstance.transform.setTranslation(translationVector);
        }

        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectableInstance != null) {
            this.updateBoundingBoxes();
            this.resetValues();
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    private void updateBoundingBoxes() {
        // TODO: update instance bounding box
        // ...

        selectableInstance.axes.calculateBoundingBoxes(currentInstancePosition);
    }

    private void resetValues() {
        selectableInstance = null;
        activeAxis = null;
    }

    private enum ActiveAxis {
        X,
        Y,
        Z
    }
}
