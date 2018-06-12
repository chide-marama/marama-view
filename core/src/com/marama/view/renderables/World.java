package com.marama.view.renderables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.marama.view.entities.EntityManager;
import com.marama.view.entities.Maramafication;
import com.marama.view.entities.exceptions.ModelNotFoundException;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.screens.GameScreen;

/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link ModelInstance}'s
 */
public class World extends Environment implements Renderable {
    private boolean loading;

    private final GameScreen gameScreen;
    private final DirectionalLight directionalLight;
    private final PerspectiveCamera perspectiveCamera;
    private ModelBatch modelBatch; // The unit that can render the modelInstances
    private CameraInputController cameraInputController;

    private final EntityManager entityManager; // The unit that can hold all the models of the currently loaded maramafications.
    private Array<ModelInstance> modelInstances;
    private final ShapeRenderer shapeRenderer;

    /**
     * Instantiates a new {@link World} which is able to render 3D {@link ModelInstance}'s.
     *
     * @param directionalLight The light that will be applied in the {@link World}.
     * @param perspectiveCamera The main camera that will be used to view the {@link World}.
     */
    public World(GameScreen gameScreen, DirectionalLight directionalLight, PerspectiveCamera perspectiveCamera) {
        super();

        this.gameScreen = gameScreen;
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

        // Render all the ModelInstances.
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
                    if (gameScreen.getActiveTool() == 0) {
                        selectableInstance.drawDimensions(shapeRenderer);
                    }

                    if (gameScreen.getActiveTool() == 1) {
                        selectableInstance.drawAxes(shapeRenderer);
                    }
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
     * Retrieving the closest  {@link ModelInstance} from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner.
     * @param screenY The y coordinate, origin is in the upper left corner.
     * @return The {@link ModelInstance} if it was found, otherwise null.
     */
    public ModelInstance getClosestModelInstance(int screenX, int screenY) {
        int index = getClosestInstanceIndex(screenX, screenY);

        if (index > -1) {
            return modelInstances.get(index);
        }

        return null;
    }

    /**
     * Retrieving the closest {@link ModelInstance} index from a {@link Ray}.
     *
     * @param ray The ray for which you want the collision checked.
     * @return The {@link ModelInstance} if it was found, otherwise null.
     */
    public ModelInstance getClosestModelInstance(Ray ray) {
        int index = getClosestInstanceIndex(ray);

        if (index > -1) {
            return modelInstances.get(index);
        }

        return null;
    }

    /**
     * Retrieving the closest {@link ModelInstance} index from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner.
     * @param screenY The y coordinate, origin is in the upper left corner.
     * @return The index of the {@link ModelInstance} if it was found, otherwise -1.
     */
    private int getClosestInstanceIndex(int screenX, int screenY) {
        Ray ray = perspectiveCamera.getPickRay(screenX, screenY);
        return getClosestInstanceIndex(ray);
    }

    /**
     * Retrieving a {@link ModelInstance} index from a {@link Ray}.
     *
     * @return The index of the {@link ModelInstance} if it was found, otherwise -1.
     */
    private int getClosestInstanceIndex(Ray ray) {
        int result = -1;
        float distance = -1f;

        Vector3 position = new Vector3();

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
     * Uses ray casting to find the closest intersection point with an object.
     * It normalises the intersection point to make it so the point is
     * relative to the modelInstance.
     * Then it compares the intersection point to the location of the faces
     * and gets the closest one.
     *
     * @param ray           The pick ray you want to intersect with the object, the origin.
     * @param modelInstance The object whose face you want to detect.
     * @return The index of the closest.
     */
    public int getClosestFaceIndex(Ray ray, SelectableInstance modelInstance) {
        Vector3 intersect = new Vector3();
        Vector3 position = modelInstance.getPosition();

        position.add(modelInstance.center);
        Intersector.intersectRaySphere(ray, position, modelInstance.radius, intersect);
        intersect.sub(position);

        return getClosestIndex(modelInstance.faces, intersect);
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
        entityManager.loadMaramafication("marams/cylinder.json");
        entityManager.loadMaramafication("marams/block.json");
    }

    /**
     * Will be called when the {@link EntityManager} is done loading.
     */
    private void doneLoading() {
        ObjectMap<String, Maramafication> maramaficationsObjectMap = entityManager.getMaramafications();
        float pos = -2f;
        for (ObjectMap.Entries<String, Maramafication> maramaficationsIterator =
             maramaficationsObjectMap.entries(); maramaficationsIterator.hasNext(); ) {

            // Get the next maramafication
            ObjectMap.Entry maramaficationEntry = maramaficationsIterator.next();
            final Maramafication maramafication = (Maramafication) maramaficationEntry.value;
            try {
                SelectableInstance currentSelectableInstance = maramafication.createInstance();
                currentSelectableInstance.transform.translate(pos, 0f, 0f);
                currentSelectableInstance.updateBoundingBoxPositions();
                modelInstances.add(currentSelectableInstance);
                pos += 2f;
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Quit loading else this function will be called every render.
        loading = false;
    }

    /**
     * Add a {@link SelectableInstance} seamlessly to another {@link SelectableInstance} and add it to the {@link World}.
     *
     * @param originInstance The {@link SelectableInstance} to add the target instance to.
     * @param targetInstance The {@link SelectableInstance} that will be added to the origin instance.
     * @param originFace A {@link Vector3} of the face on the originInstance you want to use as a starting point.
     * @param targetFace A {@link Vector3} of the face on the targetInstance you want to add to the origininstance/
     */
    public void addFaceToFaceBasic(SelectableInstance originInstance, SelectableInstance targetInstance, Vector3 originFace, Vector3 targetFace) {
        moveFaceToFaceBasic(originInstance, targetInstance, originFace, targetFace);
        modelInstances.add(targetInstance);
    }

    /**
     * Move a {@link SelectableInstance} seamlessly to another {@link SelectableInstance}.
     *
     * @param originInstance The {@link SelectableInstance} you want to use as a starting point.
     * @param targetInstance The {@link SelectableInstance} you want to move to the origin instance.
     * @param originFace A {@link Vector3} of the face on the originInstance you want to use as a starting point.
     * @param targetFace A {@link Vector3} of the face on the targetInstance you want to move to the origininstance/
     */
    public void moveFaceToFaceBasic(SelectableInstance originInstance, SelectableInstance targetInstance, Vector3 originFace, Vector3 targetFace) {
        Vector3 position = originInstance.transform.getTranslation(new Vector3());
        position.add(originFace).sub(targetFace);
        targetInstance.transform.setToTranslation(position);
    }

    /**
     * Deletes a {@link SelectableInstance} from modelInstances
     *
     * @param selectableInstance The {@link SelectableInstance} you want to remove.
     * @return True if delete was succesful, false if not.
     */
    public boolean deleteObject(SelectableInstance selectableInstance) {
        return (modelInstances.removeValue(selectableInstance, true));
    }

    /**
     * Get the closest {@link Vector3} position when compared to a target.
     *
     * @param array  A {@link Array<Vector3> } that holds the values you want compared.
     * @param target A {@link Vector3 } for which you want to know its closest companions.
     * @return the index of the array that holds the closest Vector3 to the target.
     */
    private int getClosestIndex(Array<Vector3> array, Vector3 target) {
        int closest = -1;
        float min = Integer.MAX_VALUE;
        for (int j = 0; j < array.size; j++) {
            Vector3 temp = array.get(j);
            float dist = temp.dst(target);
            if (dist < min) {
                min = dist;
                closest = j;
            }
        }
        return closest;
    }
}
