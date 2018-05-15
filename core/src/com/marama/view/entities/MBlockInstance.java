package com.marama.view.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class MBlockInstance extends ModelInstance {
    private final static BoundingBox bounds = new BoundingBox();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius; // TODO: make boundingbox a box, not a sphere if possible
    private boolean selected;
    private Material defaultMaterial = new Material(ColorAttribute.createDiffuse(Color.WHITE));
    private Material selectedMaterial = new Material(ColorAttribute.createDiffuse(Color.PINK));

    /**
     *
     * @param model
     */
    public MBlockInstance(Model model) {
        super(model);

        selected = false;
        setMaterial(defaultMaterial);

        // bounding box
        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 3f;
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     *
     * @param selected
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
     *
     * @param material
     */
    public void setMaterial(Material material) {
        materials.get(0).clear();
        materials.get(0).set(material);
    }
}
