package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.marama.view.entities.MBlockInstance;
import com.marama.view.renderables.World;

public class SelectObjectInputController extends InputAdapter {
    public MBlockInstance selectedModelInstance = null;

    private PerspectiveCamera perspectiveCamera;
    private Array<ModelInstance> modelInstances;

    /**
     * Creates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param perspectiveCamera The camera that is currently used in the renderable instance ({@link World}).
     * @param modelInstances The array of {@link ModelInstance}'s that is currently used in the renderable instance ({@link World}).
     */
    public SelectObjectInputController(PerspectiveCamera perspectiveCamera, Array<ModelInstance> modelInstances) {
        this.perspectiveCamera = perspectiveCamera;
        this.modelInstances = modelInstances;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selectedModelInstance = (MBlockInstance) getModelInstance(screenX, screenY, perspectiveCamera);
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MBlockInstance instance = (MBlockInstance) getModelInstance(screenX, screenY, perspectiveCamera);

        // If a selected MBlockInstance is found and it is equal to the found instance the MBlockInstance is set to
        // selected.
        if (selectedModelInstance != null && instance != null && selectedModelInstance == instance) {
            selectedModelInstance.setSelected(!selectedModelInstance.isSelected()); // Toggle ModelInstance selection
            selectedModelInstance = null; // Reset
        }

        return false; // Continue to the next 'touchUp' listener.
    }

    // TODO: Below code might have a better place somewhere else.

    /**
     * Retrieving a {@link ModelInstance} from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param camera  The camera that is currently used in the renderable instance ({@link World}).
     * @return The {@link ModelInstance} if it was found, otherwise null.
     */
    private ModelInstance getModelInstance(int screenX, int screenY, PerspectiveCamera camera) {
        int index = getModelInstanceIndex(screenX, screenY, camera);

        if (index > -1) {
            return modelInstances.get(index);
        }

        return null;
    }

    /**
     * Retrieving a {@link ModelInstance} index from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner.
     * @param screenY The y coordinate, origin is in the upper left corner.
     * @param camera  The camera that is currently used in the renderable instance ({@link World}).
     * @return The index of the {@link ModelInstance} if it was found, otherwise -1.
     */
    private int getModelInstanceIndex(int screenX, int screenY, PerspectiveCamera camera) {
        int result = -1;
        float distance = -1f;

        Vector3 position = new Vector3();
        Ray ray = camera.getPickRay(screenX, screenY);

        for (int i = 0; i < modelInstances.size; ++i) {
            final MBlockInstance instance = (MBlockInstance) modelInstances.get(i);
            instance.transform.getTranslation(position);
            position.add(instance.center);
            float dist2 = ray.origin.dst2(position);

            if (distance >= 0f && dist2 > distance) continue;

            if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) {
                result = i;
                distance = dist2;
            }
        }

        return result;
    }
}
