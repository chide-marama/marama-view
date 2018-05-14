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
     * @param fileName
     * @return
     */
    public Model getModel(String fileName) {
        return assetManager.get(fileName, type);
    }

    /**
     *
     * @return
     */
    public ModelInstance createInstance() {
        return new ModelInstance(getModel(filePath));
    }
}
