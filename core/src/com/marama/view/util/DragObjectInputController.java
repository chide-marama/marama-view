package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.marama.view.renderables.World;

public class DragObjectInputController extends InputAdapter {
    private World world;
    Vector3 tmpVector = new Vector3();

    public DragObjectInputController(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        Ray ray = world.getPerspectiveCamera().getPickRay(screenX, screenY);
        final float distance = -ray.origin.y / ray.direction.y;
        tmpVector.set(ray.direction).scl(distance).add(ray.origin);

        ModelInstance instance = world.getModelInstances().get(0);
        instance.transform.setTranslation(tmpVector);

        return true;
    }
}
