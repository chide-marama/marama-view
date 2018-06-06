package com.marama.view.renderables;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.Maramafication;
import com.marama.view.entities.exceptions.ModelNotFoundException;
import com.marama.view.entities.instances.SelectableInstance;

import java.util.Random;

/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link ModelInstance}'s
 */
public class World extends Environment implements Renderable {
    private boolean loading;

    private DirectionalLight directionalLight;
    private PerspectiveCamera perspectiveCamera;
    private ModelBatch modelBatch; // The unit that can render the modelInstances
    private CameraInputController cameraInputController;

    private EntityManager entityManager; // The unit that can hold all the models of the currently loaded maramafications.
    private Array<ModelInstance> modelInstances;
    private ShapeRenderer shapeRenderer;

    private Random random = new Random();

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
        this.entityManager = EntityManager.getInstance();
        this.shapeRenderer = new ShapeRenderer();

        init();
    }

    @Override
    public void render(float delta) {
        // When the assets are done loading.
        if (loading && entityManager.update()) {
            doneLoading();
        }

        cameraInputController.update();

        // Render all the SelectableInstances.
        modelBatch.begin(perspectiveCamera);
        for (final ModelInstance instance : modelInstances) {
            modelBatch.render(instance, this);
        }
        modelBatch.end();

        // Render shapes.
        shapeRenderer.setProjectionMatrix(perspectiveCamera.combined); // Accept the used PerspectiveCamera matrix.
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (final ModelInstance instance : modelInstances) {
            if (instance instanceof SelectableInstance) {
                SelectableInstance selectableInstance = (SelectableInstance) instance;
                if (selectableInstance.isSelected()) {
                    selectableInstance.drawAxes(shapeRenderer);
                    selectableInstance.drawDimensions(shapeRenderer);
                }
            }
        }
        shapeRenderer.end();
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

    public PerspectiveCamera getPerspectiveCamera() {
        return perspectiveCamera;
    }

    public Array<ModelInstance> getModelInstances() {
        return modelInstances;
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

    public ModelInstance getModelInstance(Ray ray) {
        int index = getModelInstanceIndex(ray);

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
        Ray ray = perspectiveCamera.getPickRay(screenX, screenY);
        return getModelInstanceIndex(ray);
    }


    /**
     * Retrieving a {@link ModelInstance} index from a {@link Ray}.
     *
     * @return The index of the {@link ModelInstance} if it was found, otherwise -1.
     */
    public int getModelInstanceIndex(Ray ray) {
        int result = -1;
        float distance = -1f;

        Vector3 position = new Vector3();
        //Ray ray = perspectiveCamera.getPickRay(screenX, screenY);

        for (int i = 0; i < modelInstances.size; ++i) {
            final SelectableInstance instance = (SelectableInstance) modelInstances.get(i);

            // Set the center location of the instance.
            instance.transform.getTranslation(position);
            position.add(instance.center);

            float dist2 = ray.origin.dst2(position); // The squared distance from the ray origin to the instance cachedPosition.

            if (distance >= 0f && dist2 > distance) continue; // instance is not closer than a previous one.

            if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) { // Whether an intersection occurs
                result = i;
                distance = dist2;
            }
        }

        return result;
    }

    /**
     * Uses raycasting to find the closest intersection point with an object.
     * It normalises the intersection point to make it so the point is
     * relative to the modelInstance.
     * Then it compares the intersection point to the location of the faces
     * and gets the closest one.
     *
     * @param ray           The pick ray you want to intersect with the object, the origin
     * @param modelInstance The object whose face you want to detect
     * @return
     */
    public int getClosestFaceIndex(Ray ray, SelectableInstance modelInstance) {
        Vector3 intersect = new Vector3();
        Vector3 position = new Vector3();
        modelInstance.transform.getTranslation(position);
        position.add(modelInstance.center);
        Intersector.intersectRaySphere(ray, position, modelInstance.radius, intersect);
        intersect.sub(position);

        int closest = -1;
        float min = Integer.MAX_VALUE;
        for (int j = 0; j < modelInstance.faces.size; j++) {
            Vector3 temp = modelInstance.faces.get(j);
            float dist = temp.dst(intersect);
            if (dist < min) {
                min = dist;
                closest = j;
            }

        }

        return closest;
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
        perspectiveCamera.position.set(5f, 5f, 5f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();

        // Make the camera move on input.
        cameraInputController = new CameraInputController(this.perspectiveCamera);

        // Add the three maramafications via the json file to the EntityManager.
        entityManager.loadMaramafication("marams/sphere.json");
        entityManager.loadMaramafication("marams/donut.json");
        entityManager.loadMaramafication("marams/block.json");

    }

    private int randInt(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }


    public void addObject(String name) {
        try {
            SelectableInstance instance = entityManager.getMaramaficationByName(name).createInstance();
            // TODO: Don't make this position random.
            Vector3 instancePosition = new Vector3(randInt(random, -5, 5), randInt(random, -5, 5), randInt(random, -5, 5));
            instance.transform.translate(instancePosition);
            modelInstances.add(instance);
        } catch(ModelNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will be called when the {@link EntityManager} is done loading.
     */
    private void doneLoading() {
        ObjectMap<String, Maramafication> maramaficationsObjectMap = entityManager.getMaramafications();
        float pos = 0f;
        for (ObjectMap.Entries<String, Maramafication> maramaficationsIterator =
             maramaficationsObjectMap.entries(); maramaficationsIterator.hasNext(); ) {

            // Get the next maramafication
            ObjectMap.Entry maramaficationEntry = maramaficationsIterator.next();
            final Maramafication maramafication = (Maramafication) maramaficationEntry.value;
            try {
                SelectableInstance currentSelectableInstance = maramafication.createInstance();
                currentSelectableInstance.transform.translate(pos, 0f, 0f);
                currentSelectableInstance.updatePosition();
                modelInstances.add(currentSelectableInstance);
                pos += 3f;
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Quit loading else this function will be called every render.
        loading = false;
    }

    /**
     * Adds a new model to one of the faces of your current instance.
     *
     * @param instance     The model you want to add another block to.
     * @param instanceFace The face of the object you want to add a block to.
     */
    public void addBlocktoFace(SelectableInstance instance, int instanceFace, String name) {
        SelectableInstance newblock = entityManager.createSelectableInstance(name);
        Vector3 position = instance.transform.getTranslation(new Vector3());

        switch (instanceFace) {
            case 0:
                newblock.transform.setToTranslation(new Vector3(0, (newblock.radius + instance.radius) / 2, 0).add(position));
                break;
            case 1:
                newblock.transform.setToTranslation(new Vector3((newblock.radius + instance.radius) / 2, 0, 0).add(position));
                break;
            case 2:
                newblock.transform.setToTranslation(new Vector3(0, 0, (newblock.radius + instance.radius) / 2).add(position));
                break;
            case 3:
                newblock.transform.setToTranslation(new Vector3(0, -(newblock.radius + instance.radius) / 2, 0).add(position));
                break;
            case 4:
                newblock.transform.setToTranslation(new Vector3(-(newblock.radius + instance.radius) / 2, 0, 0).add(position));
                break;
            case 5:
                newblock.transform.setToTranslation(new Vector3(0, 0, -(newblock.radius + instance.radius) / 2).add(position));
                break;
        }
        modelInstances.add(newblock);
    }

    /**
     * Only works for
     *
     * @param originInstance
     * @param targetInstance
     * @param originFace
     * @param targetFace
     */
    public void addFacetoFaceBasic(SelectableInstance originInstance, SelectableInstance targetInstance, Vector3 originFace, Vector3 targetFace) {
        Vector3 position = originInstance.transform.getTranslation(new Vector3());
        position.add(originFace).sub(targetFace);
        targetInstance.transform.setToTranslation(position);

        modelInstances.add(targetInstance);

    }
}
