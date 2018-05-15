package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;

public class MBlock extends Entity {
    /**
     * @param assetManager
     */
    public MBlock(AssetManager assetManager) {
        super(assetManager, "models/m-block.obj");
    }

    /**
     * @return
     */
    @Override
    public MBlockInstance createInstance() {
        return new MBlockInstance(super.getModel("models/m-block.obj"));
    }
}
