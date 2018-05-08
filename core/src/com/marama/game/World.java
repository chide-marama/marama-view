package com.marama.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class World extends Environment implements Renderable{
    //TODO: should be removed, or modeled, we could figure this out later
    final String SPACE_SPHERE_PATH = "models/spacesphere/spacesphere.obj";
    final String BLOCK_PATH = "models/block/block.obj";
    final String INVADER_PATH = "models/invader/invader.obj";
    final String SHIP_PATH = "models/ship/ship.obj";

    private PerspectiveCamera camera;
    private AssetManager assets;
    private ModelBatch modelBatch;
    private boolean loading;

    public CameraInputController CamController;

    // GameElements
    public Array<ModelInstance> instances = new Array<ModelInstance>();

    public Array<ModelInstance> blocks = new Array<ModelInstance>();
    public Array<ModelInstance> invaders = new Array<ModelInstance>();

    public ModelInstance ship;
    public ModelInstance space;

    public World(ColorAttribute color, DirectionalLight light, PerspectiveCamera camera, AssetManager assetManager){
        modelBatch = new ModelBatch();

        light.set(
                0.8f, 0.8f, 0.8f,
                -1f, -0.8f, -0.2f
        );

        camera.position.set(7f, 7f, 7f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;

        assets.load(SHIP_PATH, Model.class);
        assets.load(BLOCK_PATH, Model.class);
        assets.load(INVADER_PATH, Model.class);
        assets.load(SPACE_SPHERE_PATH, Model.class);

        set(color);
        add(light);
        this.camera = camera;
        this.assets = assetManager;

        loading = true;

        CamController = new CameraInputController(this.camera);
    }

    private void doneLoading() {
        ship = new ModelInstance(assets.get(SHIP_PATH, Model.class));
        ship.transform.setToRotation(Vector3.Y, 180).trn(0, 0, 6f);
        instances.add(ship);

        Model blockModel = assets.get(BLOCK_PATH, Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            ModelInstance block = new ModelInstance(blockModel);
            block.transform.setToTranslation(x, 0, 3f);
            instances.add(block);
            blocks.add(block);
        }

        Model invaderModel = assets.get(INVADER_PATH, Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -8f; z <= 0f; z += 2f) {
                ModelInstance invader = new ModelInstance(invaderModel);
                invader.transform.setToTranslation(x, 0, z);
                instances.add(invader);
                blocks.add(invader);
            }
        }
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        CamController.update();

        modelBatch.begin(camera);
        modelBatch.render(instances, this);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
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
}
