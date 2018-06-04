package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;

public class DragObjectInputController extends InputAdapter {
    private World world;
    Vector3 tmpVector = new Vector3();

    SelectableInstance instance = null;

    public DragObjectInputController(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        return super.touchDown(screenX, screenY, pointer, button);

        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);


        for (int i = 0; i < world.getModelInstances().size; ++i) {
            ModelInstance modelInstance = world.getModelInstances().get(i);

            if (modelInstance instanceof SelectableInstance) {
                SelectableInstance selectableInstance = (SelectableInstance) modelInstance;


//                for (int j = 0; j < selectableInstance.axes.; j++) {
//
//                }

                if (Intersector.intersectRayBounds(ray, selectableInstance.axes.boundingBoxX, null)) {
                    instance = selectableInstance;
                }
            }
        }

        System.out.println(instance);

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
        final float distance = -ray.origin.y / ray.direction.y;
        tmpVector.set(ray.direction).scl(distance).add(ray.origin);

//        ModelInstance instance = world.getModelInstances().get(0);


        if (instance != null) {
            instance.transform.setTranslation(tmpVector.x, 0, 0); // tmpVector.y, tmpVector.z

            // TODO: fix this, the boundingbox must move with the instance
            instance.axes.boundingBoxX.mul(new Matrix4(tmpVector, new Quaternion(), new Vector3()));
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        instance = null;
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
