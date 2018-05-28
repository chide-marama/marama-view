package com.marama.view.entities.instances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

/**
 * A {@link EntityInstance} is a {@link ModelInstance} that adds a {@link BoundingBox}.
 * This {@link BoundingBox} can be used for selection detection or calculating if the instance is outside of the frustum.
 */
public class EntityInstance extends ModelInstance {
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;

    public final BoundingBox boundingBox = new BoundingBox();
    private final static Vector3 position = new Vector3();

    private boolean selected;
    private Material defaultMaterial;
    private Material selectedMaterial = new Material(ColorAttribute.createDiffuse(Color.PINK));

    /**
     * Instantiate a new {@link EntityInstance} with a {@link BoundingBox}.
     *
     * @param model           The model to create the {@link EntityInstance} from.
     * @param defaultMaterial
     */
    public EntityInstance(Model model, Material defaultMaterial) {
        super(model);

        this.defaultMaterial = defaultMaterial;

        selected = false;
        materials.add(new Material()); // Add a default empty material that we can clear and set.
        setMaterial(defaultMaterial);

        // bounding box
        calculateBoundingBox(boundingBox);
        boundingBox.getCenter(center); // Actually sets the center value
        boundingBox.getDimensions(dimensions); // Actually sets the dimensions value
        radius = dimensions.len() / 2f;
    }

    /**
     * Select or deselect the current instance and update's its {@link Material}.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;

        if (selected) {
            setMaterial(selectedMaterial);
        } else {
            setMaterial(defaultMaterial);
        }
    }

    /**
     * Apply a new {@link Material} to the {@link EntityInstance}.
     *
     * @param material The {@link Material} to apply.
     */
    public void setMaterial(Material material) {
        materials.get(0).clear();
        materials.get(0).set(material);
    }

    /**
     * Check whether the {@link EntityInstance} is visible or not.
     *
     * @param camera The current active {@link PerspectiveCamera}.
     * @return Whether the {@link EntityInstance} is visible.
     */
    public boolean isVisible(final PerspectiveCamera camera) {
        return camera.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }

    /**
     * Obtain information on a {@link Ray} that was cast and whether it intersected this {@link EntityInstance}.
     *
     * @param ray The {@link Ray} that was cast.
     * @return -1 on no intersection or the squared distance between the center.
     */
    public float intersects(Ray ray) {
        // Calculate the center of the object.
        transform.getTranslation(position).add(center);

        // Calculate the location on the ray where the y-coordinate is the same as the y-coordinate
        // of the center of the object.
        final float len = ray.direction.dot(
                position.x - ray.origin.x,
                position.y - ray.origin.y,
                position.z - ray.origin.z);


        if (len < 0f) {
            return -1f;
        }

        // Calculate the squared distance between this point and the center of the object.
        float dist2 = position.dst2(
                ray.origin.x + ray.direction.x * len,
                ray.origin.y + ray.direction.y * len,
                ray.origin.z + ray.direction.z * len);

        // If the distance is more than the radius then we return -1f otherwise we return the squared distance.
        return (dist2 <= radius * radius) ? dist2 : -1f;
    }
}
