package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;

public class MBlock extends Entity {
    public MBlock(AssetManager assetManager) {
        super(assetManager, "models/m-block.obj");
    }

    @Override
    public MBlockInstance createInstance() {
        return new MBlockInstance(super.getModel("models/m-block.obj"));
    }
}
