package com.marama.view.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.renderables.World;
import com.marama.view.screens.GameScreen;

/**
 * A {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
 */
public class SelectObjectInputController extends InputAdapter {
    private GameScreen gameScreen;
    private SelectableInstance newSelectableInstance = null;
    private SelectableInstance currentSelectableInstance = null;

    private Vector2 touchDownMousePosition;
    private int maxDifference = 6;

    /**
     * Instantiates an {@link InputAdapter} specifically for selecting 3D objects rendered in {@link World}.
     *
     * @param gameScreen The ({@link GameScreen}) instance that renders 3D models.
     */
    public SelectObjectInputController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        newSelectableInstance = (SelectableInstance) gameScreen.world.getClosestModelInstance(screenX, screenY);
        touchDownMousePosition = new Vector2(screenX, screenY);
        return false; // Continue to the next 'touchDown' listener.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Check whether the mouse was moved.
        Vector2 touchUpMousePosition = new Vector2(screenX, screenY);
        Vector2 difference = touchUpMousePosition.sub(touchDownMousePosition);
        int differenceX = Math.abs((int) difference.x);
        int differenceY = Math.abs((int) difference.y);

        if (differenceX > maxDifference || differenceY > maxDifference) {
            return false;
        }

        // Continue with selection
        if (newSelectableInstance != null) {
            SelectableInstance selectableInstance = (SelectableInstance) gameScreen.world.getClosestModelInstance(screenX, screenY);

            if (newSelectableInstance == selectableInstance) {
                newSelectableInstance.toggleSelected();

                // If newSelectableInstance and currentSelectableInstance are not equal, deselect currentSelectableInstance.
                if (currentSelectableInstance != null && newSelectableInstance != currentSelectableInstance) {
                    currentSelectableInstance.setSelected(false);
                }

                currentSelectableInstance = newSelectableInstance;
                newSelectableInstance = null;
            }
        }
        return false; // Continue to the next 'touchUp' listener.
    }
}
