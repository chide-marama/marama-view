package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.marama.view.renderables.World;

/**
 * A wrapper class with functionality for {@link Model} and {@link ModelInstance} creation.
 */
public abstract class Entity {
    private String filePath;
    private Class<Model> type;
    private final AssetManager assetManager;

    /**
     * Instantiates a new {@link Entity} from a .obj file.
     *
     * @param assetManager The {@link AssetManager} that manages assets in the {@link World}
     * @param filePath The path to a .obj file.
     */
    public Entity(AssetManager assetManager, String filePath) {
        this.assetManager = assetManager;
        this.filePath = filePath;
        this.type = Model.class;
        this.assetManager.load(filePath, type);
    }

    /**
     * Return the {@link Entity}'s {@link Model}.
     *
     * @param fileName
     * @return {@link Model}
     */
    public Model getModel(String fileName) {
        return assetManager.get(fileName, type);
    }

    /**
     * Creates a new {@link ModelInstance}.
     *
     * @return {@link ModelInstance}
     */
    public ModelInstance createInstance() {
        return new ModelInstance(getModel(filePath));
    }
}
