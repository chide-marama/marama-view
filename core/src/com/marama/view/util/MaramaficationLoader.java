package com.marama.view.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.marama.view.entities.Maramafication;

public class MaramaficationLoader extends AsynchronousAssetLoader<Maramafication, MaramaficationLoader.MaramaficationLoaderParameter> {

    private Maramafication maramafication;
    private JsonReader jsonReader;
    private ObjLoader objLoader;

    public MaramaficationLoader(FileHandleResolver resolver) {
        super(resolver);
        jsonReader = new JsonReader();
        objLoader = new ObjLoader();
    }

    protected Maramafication getLoadedEntity() {
        return maramafication;
    }


    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MaramaficationLoader.MaramaficationLoaderParameter parameter) {
        maramafication = null;
        maramafication = new Maramafication(fileName);

        JsonValue jsonValue = jsonReader.parse(file.readString());
        maramafication = new Maramafication(jsonValue.get("obj_location").asString());
        maramafication.id = jsonValue.get("id").asInt();
        maramafication.name = jsonValue.get("name").asString();

        maramafication.modelFileHandle = Gdx.files.internal(maramafication.filePath);
    }

    @Override
    public Maramafication loadSync(AssetManager manager, String fileName, FileHandle file, MaramaficationLoader.MaramaficationLoaderParameter parameter) {
        Maramafication maramafication = this.maramafication;
        this.maramafication = null;
        return maramafication;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MaramaficationLoader.MaramaficationLoaderParameter parameter) {
        return null;
    }

    static public class MaramaficationLoaderParameter extends AssetLoaderParameters<Maramafication> {
    }

}
