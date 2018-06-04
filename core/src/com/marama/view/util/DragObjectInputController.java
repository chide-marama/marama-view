package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;

enum ActiveAxis { X, Y, Z }

public class DragObjectInputController extends InputAdapter {
    private World world;

    private SelectableInstance selectableInstance = null;
    private ActiveAxis activeAxis = null;

    private Vector3 moved = new Vector3();
    private Vector3 instanceStart = new Vector3();
    private Vector3 intersectionPoint = new Vector3();
    private Matrix4 boundingBoxTranslation = new Matrix4();

    public DragObjectInputController(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);

        for (int i = 0; i < world.getModelInstances().size; ++i) {
            ModelInstance modelInstance = world.getModelInstances().get(i);
            if (modelInstance instanceof SelectableInstance) {
                SelectableInstance instance = (SelectableInstance) modelInstance;
                boolean intersectionOccurred = false;

                // X axis
                if (Intersector.intersectRayBounds(ray, instance.axes.boundingBoxX, intersectionPoint)) {
                    activeAxis = ActiveAxis.X;
                    intersectionOccurred = true;
                }

                // TODO: Y and Z

                if (intersectionOccurred) {
                    selectableInstance = instance;
                    instance.transform.getTranslation(instanceStart); // Set the current position of the instance
                }
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY); // Keep casting a ray to update the instance position.
        float distance = (-ray.origin.y / ray.direction.y);


        if (selectableInstance != null && activeAxis != null) {
            if (activeAxis == ActiveAxis.X) {
                distance += (intersectionPoint.x - instanceStart.x);
                moved.set(ray.direction).scl(distance).add(ray.origin);
                selectableInstance.transform.setTranslation(new Vector3(moved.x, 0, 0));
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectableInstance != null) {
            this.updateBoundingBoxes();
            this.resetValues();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    private void updateBoundingBoxes() {
        // TODO: update instance bounding box

        // Axes
        if (activeAxis == ActiveAxis.X) {
            boundingBoxTranslation.translate(new Vector3(moved.x, 0, 0).sub(instanceStart));
        }

        if (activeAxis == ActiveAxis.Y) {
            // TODO: Y
        }

        if (activeAxis == ActiveAxis.Z) {
            // TODO: Z
        }

        selectableInstance.axes.boundingBoxX.mul(boundingBoxTranslation);
        selectableInstance.axes.boundingBoxY.mul(boundingBoxTranslation);
        selectableInstance.axes.boundingBoxZ.mul(boundingBoxTranslation);
    }

    private void resetValues() {
        boundingBoxTranslation.idt();
        selectableInstance = null;
        activeAxis = null;
    }
}
