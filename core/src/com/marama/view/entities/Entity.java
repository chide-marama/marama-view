package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 *
 */
public class Entity {
    public String filePath;
    public Class<Model> type;

    private AssetManager assetManager;

    /**
     *
     * @param assetManager
     * @param filePath
     */
    public Entity(AssetManager assetManager, String filePath) {
        this.assetManager = assetManager;

        this.filePath = filePath;
        this.type = Model.class;
        this.assetManager.load(filePath, type);
    }

    /**
     *
     * @return
     */
    public ModelInstance asInstance() {
        return new ModelInstance(assetManager.get(filePath, type));
    }
}
