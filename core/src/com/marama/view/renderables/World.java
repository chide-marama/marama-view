package com.marama.view.renderables;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.marama.view.entities.MBlock;

public class World extends Environment implements Renderable {
    public CameraInputController cameraInputController;

    private PerspectiveCamera perspectiveCamera;
    private AssetManager assetManager;
    private ModelBatch modelBatch;
    private boolean loading;
    private Array<ModelInstance> modelInstances;
    private MBlock mBlock;

    public World(ColorAttribute color, DirectionalLight light, PerspectiveCamera perspectiveCamera, AssetManager assetManager) {
        loading = true;

        modelInstances = new Array<ModelInstance>();
        modelBatch = new ModelBatch();

        light.set(
                0.8f, 0.8f, 0.8f,
                -1f, -0.8f, -0.2f
        );

        perspectiveCamera.position.set(7f, 7f, 7f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();

        set(color);
        add(light);
        this.perspectiveCamera = perspectiveCamera;
        this.assetManager = assetManager;

        cameraInputController = new CameraInputController(this.perspectiveCamera);

        mBlock = new MBlock(assetManager);
    }

    @Override
    public void render(float delta) {
        if (loading && assetManager.update()) //
            doneLoading();
        cameraInputController.update();

        modelBatch.begin(perspectiveCamera);
        modelBatch.render(modelInstances, this);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        modelInstances.clear();
        assetManager.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private void doneLoading() {
        modelInstances.add(mBlock.asInstance());
    }
}
