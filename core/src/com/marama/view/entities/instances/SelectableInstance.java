package com.marama.view.entities.instances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.marama.view.util.Axes;

/**
 * A {@link SelectableInstance} is a {@link ModelInstance} that adds a {@link BoundingBox}.
 * This {@link BoundingBox} can be used for selection detection.
 */
public class SelectableInstance extends ModelInstance {
    public final BoundingBox boundingBox = new BoundingBox();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius; // TODO: make boundingbox a box, not a sphere if possible and performant
    private boolean selected;
    private Material defaultMaterial;
    private Material selectedMaterial = new Material(ColorAttribute.createDiffuse(Color.PINK));

    /**
     * Instantiate a new {@link ModelInstance} that adds functionality for selecting them.
     *
     * @param model           The model to create the {@link SelectableInstance} from.
     * @param defaultMaterial
     */
    public SelectableInstance(Model model, Material defaultMaterial) {
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

    public boolean isSelected() {
        return selected;
    }

    /**
     * Select or deselect the current instance and update's its {@link Material}.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;

        if (selected) {
//            setMaterial(selectedMaterial);
        } else {
//            setMaterial(defaultMaterial);
        }
    }

    /**
     * Apply a new {@link Material} to the {@link SelectableInstance}.
     *
     * @param material The {@link Material} to apply.
     */
    public void setMaterial(Material material) {
        materials.get(0).clear();
        materials.get(0).set(material);
    }

    public void drawAxes(ShapeRenderer shapeRenderer) {
        Axes.draw(shapeRenderer, transform.getTranslation(new Vector3()));
    }

    public void drawSkeleton(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
        Vector3 pos = transform.getTranslation(new Vector3());
        Vector3 dim = boundingBox.getDimensions(new Vector3());
        shapeRenderer.box(pos.x - 0.5f, pos.y - 0.5f, pos.z + 0.5f, dim.x, dim.y, dim.z);
    }
}
