package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.util.MaramaEntityLoader;

public class EntityManager {
    //    private ArrayList<Entity> EntityList;
    final ObjectMap<String, Model> modelList = new ObjectMap<String, Model>();
    private AssetManager assetManager;

    public EntityManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.setLoader(MaramaEntity.class, new MaramaEntityLoader(this.assetManager.getFileHandleResolver()));
    }

    public void loadObj(String filepath) {
        assetManager.load(filepath, Model.class);
        assetManager.finishLoading();
        modelList.put(filepath, (Model) assetManager.get(filepath));
    }

    public Model getModel(String filepath) {
        return modelList.get(filepath);
    }

//    public void getAllMaramafications(){
//
//        List<Model> list =  new List<Model>;
//        assetManager.getAll(Model.class, list);
//    }
}
