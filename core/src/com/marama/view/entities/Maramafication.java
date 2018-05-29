package com.marama.view.entities;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.marama.view.MaramaParts.Joint;
import com.marama.view.renderables.World;

import java.util.ArrayList;

/**
 * A {@link Entity} with a predefined .obj file.
 */
public class Maramafication extends Entity {
    public Class<Model> type;
    public int id;
    public ArrayList<Joint> joints;
    public String name;
    public String filePath;
    public Model model;
    public FileHandle modelFileHandle;

    /**
     * Instantiates a new {@link Maramafication}.
     *
     * @param entityManager The reference to the used {@link EntityManager} in {@link World}.
     */
    public Maramafication(String fileName) {
        this(fileName, 0);
    }

    public Maramafication(String fileName, int id) {
        this.id = id;
        this.filePath = fileName;
        this.joints = new ArrayList<Joint>();
    }


    public Model getModel() {
        return model;
    }

    public void setModel(ObjLoader objLoader) {
        model = objLoader.loadModel(modelFileHandle);
    }

//    @Override
//    public SelectableInstance createInstance() {
//        return new SelectableInstance(super.getModel("models/m-block.obj"), new Material(ColorAttribute.createDiffuse(Color.WHITE)));
//    }
}
