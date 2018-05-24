package com.marama.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.marama.view.MaramaParts.Joint;

import java.util.ArrayList;

public class MaramaEntity {
    private int id;
    private ArrayList<Joint> joints;
    private Model model;

    public MaramaEntity(String filepath, int id) {
        this(Gdx.files.internal(filepath), id);
    }

    public MaramaEntity(FileHandle file) {
        this(file, 0);
    }

    public MaramaEntity(FileHandle file, int id) {
        ObjLoader objLoader = new ObjLoader(); // TODO: Check if this the way to do this.
        // TODO: Feels a little strange to instantiate a ObjLoader inside a constructor.
        this.model = objLoader.loadModel(file, false);
        this.id = id;
        this.joints = new ArrayList<Joint>();
    }
}
