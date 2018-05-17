package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.marama.view.renderables.World;

/**
 * A {@link Entity} with a predefined .obj file.
 */
public class MBlock extends Entity {
    /**
     * Instantiates a new {@link MBlock}.
     *
     * @param assetManager The reference to the used {@link AssetManager} in {@link World}.
     */
    public MBlock(AssetManager assetManager) {
        super(assetManager, "models/m-block.obj");
    }

    @Override
    public SelectableInstance createInstance() {
        return new SelectableInstance(super.getModel("models/m-block.obj"));
    }
}
