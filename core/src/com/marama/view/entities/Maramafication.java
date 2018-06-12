package com.marama.view.entities;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.marama.view.entities.exceptions.ModelNotFoundException;
import com.marama.view.entities.instances.SelectableInstance;

import java.util.UUID;

public class Maramafication {
    private UUID id;
    private String name;
    private String filePath;
    private String imageLocation;
    private Model model;
    private FileHandle modelFileHandle;
    private Material material = new Material(ColorAttribute.createDiffuse(Color.WHITE));

    /**
     * Instantiates a new {@link Maramafication}.
     *
     * @param fileName
     */
    public Maramafication(UUID id, String fileName, String name, String imageLocation) {
        this.id = id;
        this.filePath = fileName;
        this.name = name;
        this.imageLocation = imageLocation;
    }

    /**
     * Create an instance of the current maramafication.
     *
     * @return {@link SelectableInstance}
     */
    public SelectableInstance createInstance() throws ModelNotFoundException {
        if (model == null) {
            throw new ModelNotFoundException();
        }
        return new SelectableInstance(model, material, name);
    }

    public void setModelFileHandle(FileHandle fileHandle) {
        this.modelFileHandle = fileHandle;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Model getModel() {
        return model;
    }

    /**
     * Sets the {@link Model} of the current Maramafication.
     * This is implemented so the World can set the Model of a maramafication with its own ObjLoader.
     * A reason why this is implemented here, is because it has to happen on a thread with OpenGL context.
     *
     * @param objLoader The loader to load the model with.
     */
    public void setModel(ObjLoader objLoader) {
        model = objLoader.loadModel(modelFileHandle);
    }

    public String getImageLocation() {
        return imageLocation;
    }
}
