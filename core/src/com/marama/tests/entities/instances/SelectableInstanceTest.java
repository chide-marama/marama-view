package entities.instances;


import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import base.TestApplication;
import com.marama.view.entities.instances.SelectableInstance;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelectableInstanceTest extends TestApplication {

    @Test
    public void setPositionWillUpdateTheSelectableInstancePosition() {
        SelectableInstance selectableInstance = new SelectableInstance(new Model(), new Material());

        Vector3 initialPosition = selectableInstance.getPosition();
        selectableInstance.setPosition(new Vector3(1f, 1f, 1f));
        Vector3 newPosition = selectableInstance.getPosition();

        assertNotEquals(initialPosition, newPosition);
    }

    @Test
    public void setPositionWillUpdateTheBoundingBox() {
        Model box = new ModelBuilder().createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Normal | Usage.Position);
        SelectableInstance selectableInstance = new SelectableInstance(box, new Material());

        // Grab a position from the bounding box
        Vector3 initialPosition = selectableInstance.boundingBox.getCorner001(new Vector3());

        // Apply the translation
        Vector3 translation = new Vector3(1f, 1f, 1f);
        selectableInstance.setPosition(translation);

        // Grab the new position from the bounding box
        Vector3 newPosition = selectableInstance.boundingBox.getCorner001(new Vector3());

        // The applied translation is exactly the same as the new position
        assertEquals(initialPosition.add(translation), newPosition);
    }
}