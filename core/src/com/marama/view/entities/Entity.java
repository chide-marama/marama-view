package com.marama.view.entities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.marama.view.renderables.World;

/**
 * A wrapper class with functionality for {@link Model} and {@link ModelInstance} creation.
 */
public abstract class Entity {
//    private Model model;

    /**
     * Instantiates a new {@link Entity} from a .obj file.
     *
     * @param entityManager The {@link EntityManager} that manages maramafications in the {@link World}
     * @param filePath The path to a .obj file.
     */

//    public Entity(String filePath) {
//        this(filePath, 0);
//    }
//
//    public Entity(String filePath, int id) {
//        this.id = id;
//        this.filePath = filePath;
//        this.joints = new ArrayList<Joint>();
//    }

    /**
     * Return the {@link Entity}'s {@link Model}.
     *
     * @param fileName
     * @return {@link Model}
     */
//    public Model getModel(String fileName) {
//        return entityManager.get(fileName, type);
//    }

    /**
     * Creates a new {@link ModelInstance}.
     *
     * @return {@link ModelInstance}
     */
//    public ModelInstance createInstance() {
//        return new ModelInstance(getModel(filePath));
//    }
}
