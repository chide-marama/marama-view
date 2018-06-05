package com.marama.view.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.marama.view.entities.Maramafication;

import java.util.UUID;

/**
 * Loads and stores maramafications.
 */
public class MaramaficationLoader extends AsynchronousAssetLoader<Maramafication, MaramaficationLoader.MaramaficationLoaderParameter> {
    // The currently loaded maramafication.
    private Maramafication maramafication;
    // Remember the JsonReader so that it can be used for all maramafications
    private JsonReader jsonReader;

    public MaramaficationLoader(FileHandleResolver resolver) {
        super(resolver);
        jsonReader = new JsonReader();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MaramaficationLoader.MaramaficationLoaderParameter parameter) {
        // First make sure the current is cleared
        maramafication = null;

        // Since the maramafications are currently saved as json files, read the json file
        // This should be altered when there is another way of passing maramafication information
        JsonValue jsonValue = jsonReader.parse(file.readString());

        // Set all the information according to the maramafication file
        String filePath = jsonValue.get("obj_location").asString();
        maramafication = new Maramafication(
                UUID.fromString(jsonValue.get("id").asString()),
                filePath,
                jsonValue.get("name").asString(),
                jsonValue.get("image_location").asString()
        );

        // Since the model can't be set here, due to multithreading, we only pass the FileHandle.
        maramafication.setModelFileHandle(Gdx.files.internal(filePath));
    }

    @Override
    public Maramafication loadSync(AssetManager manager, String fileName, FileHandle file, MaramaficationLoader.MaramaficationLoaderParameter parameter) {
        // Get the maramafication that is currently loaded, and clear it
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
