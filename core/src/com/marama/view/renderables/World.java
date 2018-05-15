package com.marama.view.renderables;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.marama.view.entities.MBlock;
import com.marama.view.entities.MBlockInstance;


/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link ModelInstance}'s
 */
public class World extends Environment implements Renderable {
    public CameraInputController cameraInputController;
    public Array<ModelInstance> modelInstances;

    private boolean loading;
    private DirectionalLight directionalLight;
    public PerspectiveCamera perspectiveCamera;
    private AssetManager assetManager;
    private ModelBatch modelBatch;
    private MBlock mBlock;

    /**
     * Instantiates a new {@link World} which is able to render 3D {@link ModelInstance}'s
     * @param directionalLight
     * @param perspectiveCamera
     * @param assetManager
     */
    public World(DirectionalLight directionalLight, PerspectiveCamera perspectiveCamera, AssetManager assetManager) {
        super();

        this.directionalLight = directionalLight;
        this.perspectiveCamera = perspectiveCamera;
        this.assetManager = assetManager;

        init();
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

    /**
     * Initializes the {@link World} with some default settings.
     */
    private void init() {
        // All assets are currently loading
        loading = true;

        // Instantiate properties for rendering models.
        modelInstances = new Array<ModelInstance>();
        modelBatch = new ModelBatch();

        // Set the color and direction of the light.
        directionalLight.set(new Color(0.8f, 0.8f, 0.8f, 1.0f), new Vector3(-1f, -0.8f, -0.2f));

        // Adding the directionalLight to the Environment/World.
        add(directionalLight);
        set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        // Default camera settings.
        perspectiveCamera.position.set(7f, 7f, 7f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();

        // Make the camera move on input
        cameraInputController = new CameraInputController(this.perspectiveCamera);

        // Create a model for rendering
        mBlock = new MBlock(assetManager); // TODO: Defining models for rendering should be done somewhere else.
    }

    /**
     * Will be called when the {@link AssetManager} is done loading.
     */
    private void doneLoading() {
        // Create a nice 3D grid of MBlocks.
        for (float x = -3f; x <= 3f; x += 2f) {
            for (float z = -3f; z <= 3f; z += 2f) {
                for (float y = -3f; y <= 3f; y += 2f) {
                    MBlockInstance instance = mBlock.createInstance();
                    instance.transform.setToTranslation(x, y, z);
                    modelInstances.add(instance);
                }
            }
        }

        // Quit loading else this function will be called every render.
        loading = false;
    }
}
