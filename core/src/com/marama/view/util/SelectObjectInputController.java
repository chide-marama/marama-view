package com.marama.view.util;

import com.badlogic.gdx.InputAdapter;
import com.marama.view.entities.instances.SelectableInstance;
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
        newSelectableInstance = (SelectableInstance)world.getModelInstance(screenX, screenY);
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false; // Continue to the next 'touchDragged' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (newSelectableInstance != null) {
            SelectableInstance instance = (SelectableInstance)world.getModelInstance(screenX, screenY);
            if (newSelectableInstance == instance) {
                handleDiscreteSelection();
            }
        }
        return false; // Continue to the next 'touchUp' listener.
    }
    // If a selected SelectableInstance is found and it is equal to the found instance the SelectableInstance is set to
    // selected.

    /***
     * Switches the state of the entityInstance based on what the current selection is.
     */
    private void handleDiscreteSelection(){
        newSelectableInstance.switchSelect();
        // If currentEntity and NewEntity are different, switch CurrentEntity off.
        if (currentSelectableInstance != null && newSelectableInstance != currentSelectableInstance) {
            currentSelectableInstance.setSelected(false);
        }
        currentSelectableInstance = newSelectableInstance;

    }
}
