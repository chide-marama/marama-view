package com.marama.view.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.marama.view.entities.MaramaEntity;

public class MaramaEntityLoader extends AsynchronousAssetLoader<MaramaEntity, MaramaEntityLoader.EntityLoaderParameter> {


    private MaramaEntity entity;

    public MaramaEntityLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    protected MaramaEntity getLoadedEntity() {
        return entity;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MaramaEntityLoader.EntityLoaderParameter parameter) {
        entity = null;
        entity = new MaramaEntity(file);
    }

    @Override
    public MaramaEntity loadSync(AssetManager manager, String fileName, FileHandle file, MaramaEntityLoader.EntityLoaderParameter parameter) {
        MaramaEntity entity = this.entity;
        this.entity = null;
        return entity;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MaramaEntityLoader.EntityLoaderParameter parameter) {
        return null;
    }

    static public class EntityLoaderParameter extends AssetLoaderParameters<MaramaEntity> {
    }

}
