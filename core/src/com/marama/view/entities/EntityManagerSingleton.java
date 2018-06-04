package com.marama.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.exceptions.ModelNotFoundException;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.util.MaramaficationLoader;
import com.sun.media.jfxmedia.logging.Logger;

/**
 * The EntityManager singleton class, extending from {@link AssetManager}.
 */
public class EntityManagerSingleton extends AssetManager {
    private static EntityManagerSingleton instance = new EntityManagerSingleton();

    // Since in AssetManager there is no good way to get all assets of a certain type, we have to remember our maramafications here.
    final ObjectMap<String, Maramafication> maramafications = new ObjectMap();

    public ObjLoader objLoader = new ObjLoader();

    /**
     * An instance of {@link AssetManager}, but it's also able to load maramafications.
     */
    private EntityManagerSingleton() {
        setLoader(Maramafication.class, new MaramaficationLoader(getFileHandleResolver()));
    }

    static public EntityManagerSingleton getInstance() {
        return instance;
    }

    /**
     * The method to load a maramafication via a filepath.
     *
     * @param filePath the location of the maramafication
     */
    public void loadMaramafication(String filePath) {
        // Load the maramafication and wait till its over
        load(filePath, Maramafication.class);
        finishLoading();

        /* Get the maramafication from the assets stored in the AssetManager.
        Create the maramafication model and put it in the ObjectMap.
        We do this because the loader is in the wrong thread for adding the model.
         */
        Maramafication m = get(filePath);
        maramafications.put(m.getName(), m);
        m.setModel(objLoader);
    }

    /**
     * Create a {@link SelectableInstance} of the model of the specified name.
     *
     * @param name The name of the maramafication you want to create a SelectableInstance of
     * @return The SelectableInstance that was created
     */
    public SelectableInstance createSelectableInstance(String name) {
        SelectableInstance instance;
        try {
            instance = getMaramaficationByName(name).createInstance();
        } catch (ModelNotFoundException e) {
            instance = null;
            Logger.logMsg(Logger.ERROR, "Could not create selectable instance.");
        }
        return instance;
    }

    /**
     * Retrieve a maramafication by its name, the url will be added.
     *
     * @param name: Name of the object inside the asset list
     * @return The maramafication retrieved maramafication
     */
    public Maramafication getMaramaficationByName(String name) {
        return (Maramafication) get("marams/" + name + ".json");
    }

    public ObjectMap<String, Maramafication> getMaramafications() {
        return maramafications;
    }
}
