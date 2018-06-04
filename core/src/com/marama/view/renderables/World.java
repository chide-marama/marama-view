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
import com.marama.view.entities.Entity;
import com.marama.view.entities.MBlock;
import com.marama.view.entities.instances.SelectableInstance;
import com.marama.view.util.Axes;

/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link ModelInstance}'s
 */
public class World extends Environment implements Renderable {
    private boolean loading;
    private DirectionalLight directionalLight;
    private Array<ModelInstance> modelInstances; // All models that are in the World.
    private PerspectiveCamera perspectiveCamera;
    private CameraInputController cameraInputController;
    private AssetManager assetManager;
    private ModelBatch modelBatch;
    private MBlock mBlock;
    private ShapeRenderer shapeRenderer;

    public Ray getRay(int screenX, int screenY) {
        return perspectiveCamera.getPickRay(screenX, screenY);
    }

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
        this.assetManager = assetManager;
        this.shapeRenderer = new ShapeRenderer();
        init();
    }

    @Override
    public void render(float delta) {
        if (loading && assetManager.update()) // When the assets are done loading.
            doneLoading();

        // Allow the camera to be controlled.
        cameraInputController.update();

        // Render all the SelectableInstances.
        modelBatch.begin(perspectiveCamera);
        for (final ModelInstance instance: modelInstances) {
                modelBatch.render(instance, this);
        }
        modelBatch.end();
        shapeRenderer.setProjectionMatrix(perspectiveCamera.combined); // Accept the used PerspectiveCamera matrix.
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();

        for (final ModelInstance instance : modelInstances) {
            if (instance instanceof SelectableInstance) {
                SelectableInstance modelInstance = (SelectableInstance) instance;
                if (!modelInstance.isSelected()) {
                    modelInstance.drawBoundingBox(shapeRenderer);
                }
            }
        }

        shapeRenderer.end();
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


    public int getModelInstanceIndex(Ray ray){
        int result = -1;
        float distance = -1f;

        Vector3 position = new Vector3();
        //Ray ray = perspectiveCamera.getPickRay(screenX, screenY);

        for (int i = 0; i < modelInstances.size; ++i) {
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
     * Uses raycasting to find the closest intersection point with an object.
     * It normalises the intersection point to make it so the point is
     * relative to the modelInstance.
     * Then it compares the intersection point to the location of the faces
     * and gets the closest one.
     * @param ray The pick ray you want to intersect with the object, the origin
     * @param modelInstance The object whose face you want to detect
     * @return
     */
    public int getClosestFaceIndex(Ray ray, SelectableInstance modelInstance){
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
            if (dist < min ) {
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
        perspectiveCamera.position.set(7f, 7f, 7f);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.near = 1f;
        perspectiveCamera.far = 300f;
        perspectiveCamera.update();

        // Make the camera move on input.
        cameraInputController = new CameraInputController(this.perspectiveCamera);

        // Create a model for rendering.
        mBlock = new MBlock(assetManager);
    }

    public void addObject() {
        SelectableInstance instance = mBlock.createInstance();
        instance.transform.setToTranslation(10, 10, 10);
        modelInstances.add(instance);
    }

    /**
     * Will be called when the {@link AssetManager} is done loading.
     */
    private void doneLoading() {
//        // Create a nice 3D grid of MBlocks.
//        for (float x = -3f; x <= 3f; x += 2f) {
//            for (float z = -3f; z <= 3f; z += 2f) {
//                for (float y = -3f; y <= 3f; y += 2f) {
//                }
//            }
//        }
        for(float x = -5f; x<=5f; x+=2f){
            SelectableInstance instance = mBlock.createInstance();
            instance.transform.setToTranslation(x, 2, 2);
            modelInstances.add(instance);
      }
        SelectableInstance instance = mBlock.createInstance();
        instance.transform.setToTranslation(5, 5, 0);
        modelInstances.add(instance);
//        SelectableInstance instance2 = mBlock.createInstance();
//        instance2.transform.setToTranslation(5, 5, 0);
//        modelInstances.add(instance2);
        // Quit loading else this function will be called every render.
        loading = false;
    }

    public void addBlock(SelectableInstance instance, int currentFaceIndex) {
        SelectableInstance newblock = mBlock.createInstance();
        Vector3 position = instance.transform.getTranslation(new Vector3());
        switch (currentFaceIndex) {
            case 0:
                System.out.println("Up");
                newblock.transform.setToTranslation(new Vector3(0, 1, 0).add(position));
                break;
            case 1:
                System.out.println("Right");
                newblock.transform.setToTranslation(new Vector3(1, 0, 0).add(position));
                break;
            case 2:
                System.out.println("Front");
                newblock.transform.setToTranslation(new Vector3(0, 0, 1).add(position));
                break;
            case 3:
                System.out.println("Down");
                newblock.transform.setToTranslation(new Vector3(0, -1, 0).add(position));
                break;
            case 4:
                System.out.println("Left");
                newblock.transform.setToTranslation(new Vector3(-1, 0, 0).add(position));
                break;
            case 5:
                System.out.println("Back");
                newblock.transform.setToTranslation(new Vector3(0, 0, -1).add(position));
                break;
        }
        modelInstances.add(newblock);
    }
}
