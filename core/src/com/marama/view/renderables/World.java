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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.EntityManagerSingleton;
import com.marama.view.entities.Maramafication;
import com.marama.view.entities.exceptions.ModelNotFoundException;
import com.marama.view.entities.instances.SelectableInstance;

/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link ModelInstance}'s
 */
public class World extends Environment implements Renderable {
    private boolean loading;

    private DirectionalLight directionalLight;
    private PerspectiveCamera perspectiveCamera;
    private ModelBatch modelBatch; // The unit that can render the modelInstances
    private CameraInputController cameraInputController;

    private EntityManagerSingleton entityManager; // The unit that can hold all the models
    private Array<ModelInstance> modelInstances;

    /**
     * Instantiates a new {@link World} which is able to render 3D {@link ModelInstance}'s.
     *
     * @param directionalLight
     * @param perspectiveCamera
     * @param assetManager
     */
    public World(DirectionalLight directionalLight, PerspectiveCamera perspectiveCamera, AssetManager assetManager) {
        super();

        this.directionalLight = directionalLight;
        this.perspectiveCamera = perspectiveCamera;
        this.entityManager = EntityManagerSingleton.getInstance();

        init();
    }

    @Override
    public void render(float delta) {
        if (loading && entityManager.update()) // When the assets are done loading.
            doneLoading();
        cameraInputController.update();

        modelBatch.begin(perspectiveCamera);
        modelBatch.render(modelInstances, this);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        entityManager.clear();
        entityManager.dispose();
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

    public CameraInputController getCameraInputController() {
        return cameraInputController;
    }

    /**
     * Retrieving a {@link ModelInstance} from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return The {@link ModelInstance} if it was found, otherwise null.
     */
    public ModelInstance getModelInstance(int screenX, int screenY) {
        int index = getModelInstanceIndex(screenX, screenY);

        if (index > -1) {
            return modelInstances.get(index);
        }

        return null;
    }

    /**
     * Retrieving a {@link ModelInstance} index from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner.
     * @param screenY The y coordinate, origin is in the upper left corner.
     * @return The index of the {@link ModelInstance} if it was found, otherwise -1.
     */
    private int getModelInstanceIndex(int screenX, int screenY) {
        int result = -1;
        float distance = -1f;

        Vector3 position = new Vector3();
        Ray ray = perspectiveCamera.getPickRay(screenX, screenY);

        for (int i = 0; i < modelInstances.size - 1; ++i) {
            final SelectableInstance instance = (SelectableInstance) modelInstances.get(i);

            // Set the center location of the instance.
            instance.transform.getTranslation(position);
            position.add(instance.center);

            float dist2 = ray.origin.dst2(position); // The squared distance from the ray origin to the instance position.

            if (distance >= 0f && dist2 > distance) continue; // instance is not closer than a previous one.

            if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) { // Whether an intersection occurs
                result = i;
                distance = dist2;
            }
        }

        return result;
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

        // Make the camera move on input.
        cameraInputController = new CameraInputController(this.perspectiveCamera);

        // Add the three maramafications via the json file to the EntityManager.
        entityManager.loadMaramafication("marams/sphere.json");
        entityManager.loadMaramafication("marams/block.json");
        entityManager.loadMaramafication("marams/donut.json");
    }

    /**
     * Will be called when the {@link EntityManagerSingleton} is done loading.
     */
    private void doneLoading() {
//        this.maramaBlock.transform.setToScaling(3.0f, 3.0f, 3.0f);
//        modelInstances.add(this.maramaBlock);
//
//        this.maramaBlock2.transform.setToScaling(3.0f, 3.0f, 3.0f);
//        this.maramaBlock2.transform.setToTranslation(6f, 0f, 0f);
//        modelInstances.add(this.maramaBlock2);
//
//        this.maramaBlock2.transform.setToScaling(3.0f, 3.0f, 3.0f);
//        this.maramaBlock2.transform.setToTranslation(4f, 0f, 0f);
//        modelInstances.add(this.maramaBlock3);

        ObjectMap<String, Maramafication> maramaficationsObjectMap = entityManager.getMaramafications();
        float pos = 0f;
        for (ObjectMap.Entries<String, Maramafication> maramaficationsIterator =
             maramaficationsObjectMap.entries(); maramaficationsIterator.hasNext(); ) {

            // Get the next maramafication
            ObjectMap.Entry maramaficationEntry = maramaficationsIterator.next();
            final Maramafication maramafication = (Maramafication) maramaficationEntry.value;
            try {
                SelectableInstance currentSelectableInstance = maramafication.createInstance();
                currentSelectableInstance.transform.setToScaling(3.0f, 3.0f, 3.0f);
                currentSelectableInstance.transform.setToTranslation(pos, 0f, 0f);
                modelInstances.add(currentSelectableInstance);
                pos += 3f;
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Quit loading else this function will be called every render.
        loading = false;
    }
}
