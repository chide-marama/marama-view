package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.util.MaramaficationLoader;

public class EntityManager extends AssetManager {
    final ObjectMap<String, Maramafication> maramafications = new ObjectMap();

    public ObjLoader objLoader = new ObjLoader();

    public EntityManager() {
        setLoader(Maramafication.class, new MaramaficationLoader(getFileHandleResolver()));
    }

    public void load(String filepath) {
        load(filepath, Maramafication.class);
        finishLoading();
        Maramafication m = get(filepath);
        maramafications.put(m.name, m);
        m.setModel(objLoader);
    }

    public SelectableInstance getSelectableInstance(String name) {
        return new SelectableInstance(getMaramaficationByName(name).getModel(), new Material(ColorAttribute.createDiffuse(Color.WHITE)));
    }

    public Maramafication getMaramaficationByName(String name) {
        return maramafications.get(name);
    }
}
