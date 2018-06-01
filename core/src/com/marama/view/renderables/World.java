package com.marama.view.renderables;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
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
import com.marama.view.entities.instances.EntityInstance;

/**
 * The {@link World} is an {@link Environment} that is able to render 3D {@link EntityInstance}'s.
 */
public class World extends Environment implements Renderable {
    private boolean loading;
    private DirectionalLight directionalLight;
    private Array<EntityInstance> entityInstances; // All models that are in the World.
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
     * Instantiates a new {@link World} which is able to render 3D {@link EntityInstance}'s.
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

        // Render all the EntityInstances.
        modelBatch.begin(perspectiveCamera);
        for (final EntityInstance instance : entityInstances) {
            if (instance.isVisible(perspectiveCamera)) { // Apply frustum culling.
                modelBatch.render(instance, this);
            }
        }
        modelBatch.end();
        shapeRenderer.setProjectionMatrix(perspectiveCamera.combined); // Accept the used PerspectiveCamera matrix.
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();

        for (final EntityInstance instance : entityInstances) {
            if (instance instanceof EntityInstance) {
                EntityInstance selectableInstance = (EntityInstance) instance;
                if (!selectableInstance.isSelected()) {
                    selectableInstance.drawBoundingBox(shapeRenderer);
                }
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        entityInstances.clear();
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

    /**
     * Retrieving a {@link EntityInstance} from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return The {@link EntityInstance} if it was found, otherwise null.
     */
    public EntityInstance getModelInstance(int screenX, int screenY) {
        int index = getModelInstanceIndex(screenX, screenY);

        if (index > -1) {
            return entityInstances.get(index);
        }

        return null;
    }
    public EntityInstance getModelInstance(Ray ray) {
        int index = getModelInstanceIndex(ray);

        if (index > -1) {
            return entityInstances.get(index);
        }

        return null;
    }

    /**
     * Retrieving a {@link EntityInstance} index from screen coordinates.
     *
     * @param screenX The x coordinate, origin is in the upper left corner.
     * @param screenY The y coordinate, origin is in the upper left corner.
     * @return The index of the {@link EntityInstance} if it was found, otherwise -1.
     */
    private int getModelInstanceIndex(int screenX, int screenY) {
        Ray ray = perspectiveCamera.getPickRay(screenX, screenY);
        return getModelInstanceIndex(ray);
    }


    public int getModelInstanceIndex(Ray ray){
        int result = -1;
            float distance = -1f;

            Vector3 position = new Vector3();
            for (int i = 0; i < entityInstances.size; ++i) {
                final EntityInstance instance = entityInstances.get(i);

                // Set the center location of the instance.
                instance.transform.getTranslation(position);
                position.add(instance.center);

                float dist2 = ray.origin.dst2(position); // The squared distance from the ray origin to the instance position.

                if (distance >= 0f && dist2 > distance) continue; // instance is not closer than a previous one.

                if (Intersector.intersectRaySphere(ray, instance.center, instance.radius, null)) { // Whether an intersection occurs
                    result = i;
                    distance = dist2;
                }
            }
           return result;
    }

    public int getClosestFaceIndex(Ray ray, EntityInstance entityInstance){
        Vector3 intersect = new Vector3();
        Intersector.intersectRayBounds(ray, entityInstance.boundingBox, intersect);
        System.out.println(intersect);

        int closest = -1;
        float min = Integer.MAX_VALUE;
        for (int j = 0; j < entityInstance.faces.size; j++) {
            Vector3 temp = entityInstance.faces.get(j);
            float dist = temp.dst(intersect);
            if (dist < min ) {
                min = dist;
                closest = j;
            }

        }

        System.out.println(closest);
        return closest;
    }
    /**
     * Initializes the {@link World} with some default settings.
     */
    private void init() {
        // All assets are currently loading
        loading = true;

        // Instantiate properties for rendering models.
        entityInstances = new Array<EntityInstance>();
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
            EntityInstance instance = mBlock.createInstance();
            instance.transform.setToTranslation(x, 2, 2);
            entityInstances.add(instance);
        }
        EntityInstance instance = mBlock.createInstance();
        instance.transform.setToTranslation(5, 5, 0);
        entityInstances.add(instance);
//        EntityInstance instance2 = mBlock.createInstance();
//        instance2.transform.setToTranslation(5, 5, 0);
//        entityInstances.add(instance2);
        // Quit loading else this function will be called every render.
        loading = false;
    }

    public void addBlock(EntityInstance instance, int currentFaceIndex) {
        EntityInstance newblock = mBlock.createInstance();
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
        entityInstances.add(newblock);
    }
}
