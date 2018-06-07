package com.marama.view.entities.instances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.marama.view.util.Axes;
import com.marama.view.util.BoundingBoxHelper;

/**
 * A {@link SelectableInstance} is a {@link ModelInstance} that adds a {@link BoundingBox}.
 * This {@link BoundingBox} can be used for selection detection.
 */
public class SelectableInstance extends ModelInstance {
    public final BoundingBox boundingBox = new BoundingBox();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;
    public Axes axes;

    private boolean selected = false;
    private Vector3 oldPosition = new Vector3();

    public String name;
    public Array<Vector3> faces;

    /**
     * Instantiate a new {@link ModelInstance} that adds functionality for selecting them.
     *
     * @param model           The model to create the {@link SelectableInstance} from.
     * @param defaultMaterial The default material that should cover the model upon creation.
     */
    public SelectableInstance(Model model, Material defaultMaterial, String name) {
        super(model);

        this.name = name;

        materials.add(new Material()); // Add a default empty material that we can clear and set.
        setMaterial(defaultMaterial);

        // bounding box
        calculateBoundingBox(boundingBox);
        boundingBox.getCenter(center); // Actually sets the center value
        boundingBox.getDimensions(dimensions); // Actually sets the dimensions value
        radius = dimensions.len() / 2f;

        faces = calculateFacesfromBounding();
        axes = new Axes(getPosition());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        this.selected = !this.selected;
    }

    /**
     * @return The current position of the {@link SelectableInstance}.
     */
    public Vector3 getPosition() {
        return transform.getTranslation(new Vector3());
    }

    /**
     * Apply a new position to this instance.
     * Will also update the bounding boxes, so don't call this when animating.
     *
     * @param position The new position to apply.
     */
    public void setPosition(Vector3 position) {
        transform.setTranslation(position);
        this.updatePosition();
    }

    /**
     * Updates the positions of the bounding boxes.
     */
    public void updatePosition() {
        // Update the bounding box
        boundingBox.mul(new Matrix4().setTranslation(getPosition().sub(oldPosition)));
        // Update the axes bounding boxes
        axes.calculateBoundingBoxes(getPosition());
        // update the oldPosition
        oldPosition = getPosition();
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

    /**
     * Draws the axes on top of this {@link SelectableInstance}.
     *
     * @param shapeRenderer
     */
    public void drawAxes(ShapeRenderer shapeRenderer) {
        axes.draw(shapeRenderer, getPosition());
    }

    /**
     * Draws a box around the {@link SelectableInstance}.
     *
     * @param shapeRenderer
     */
    public void drawDimensions(ShapeRenderer shapeRenderer) {
        Vector3 pos = transform.getTranslation(new Vector3());
        Vector3 dim = boundingBox.getDimensions(new Vector3());

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
        shapeRenderer.identity();
        shapeRenderer.box(pos.x - (dim.x / 2), pos.y - (dim.y / 2), pos.z + (dim.z / 2), dim.x, dim.y, dim.z);
    }

    /**
     * Draws the radius based on the dimensions.
     *
     * @param shapeRenderer
     */
    public void drawRadius(ShapeRenderer shapeRenderer) {
        Vector3 position = getPosition();

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.identity();
        shapeRenderer.translate(position.x, position.y, position.z);

        shapeRenderer.circle(0, 0, radius, 16);
        shapeRenderer.rotate(0, 1, 0, 90);
        shapeRenderer.circle(0, 0, radius, 16);
    }

    /**
     * Should only be used on cubes or objects with similar faces.
     */
    private Array<Vector3> calculateFacesfromBounding() {
        Array<Vector3> faces = new Array<Vector3>();
        Vector3 bounds = new Vector3();
        boundingBox.getDimensions(bounds);
        faces.add(new Vector3(0, bounds.y / 2, 0), new Vector3(bounds.x / 2, 0, 0), new Vector3(0, 0, bounds.z / 2));
        faces.add(new Vector3(0, -bounds.y / 2, 0), new Vector3(-bounds.x / 2, 0, 0), new Vector3(0, 0, -bounds.z / 2));
        return faces;
    }
}
