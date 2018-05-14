package com.marama.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class MBlock extends Entity {
    public MBlock(AssetManager assetManager) {
        super(assetManager, "models/m-block.obj");
    }

    @Override
    public MBlockInstance createInstance() {
        return new MBlockInstance(super.getModel("models/m-block.obj"));
    }
}
