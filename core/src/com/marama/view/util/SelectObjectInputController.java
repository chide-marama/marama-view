package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.marama.view.entities.SelectableInstance;
import com.marama.view.renderables.World;

/**
 * A {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
 */
public class SelectObjectInputController extends InputAdapter {
    private World world;
    private SelectableInstance newSelectableInstance = null;
    private SelectableInstance currentSelectableInstance = null;

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param world The ({@link World}) instance that renders 3D models.
     */
    public SelectObjectInputController(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        newSelectableInstance = (SelectableInstance) world.getModelInstance(screenX, screenY);
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SelectableInstance instance = (SelectableInstance) world.getModelInstance(screenX, screenY);

        // If a selected SelectableInstance is found and it is equal to the found instance the SelectableInstance is set to
        // selected.
        if (newSelectableInstance != null && instance != null && newSelectableInstance == instance) {
            newSelectableInstance.setSelected(true); // Apply the new selection.

            // Deselect the previous selection
            if (currentSelectableInstance != null) {
                currentSelectableInstance.setSelected(false);
            }

            currentSelectableInstance = newSelectableInstance; // update new current selection.
            newSelectableInstance = null; // Reset the new possible selection.
        }

        return false; // Continue to the next 'touchUp' listener.
    }
}
