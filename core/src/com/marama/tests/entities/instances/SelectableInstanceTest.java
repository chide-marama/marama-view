package com.marama.tests.entities.instances;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.marama.view.entities.instances.SelectableInstance;
import org.junit.Test;
import static org.junit.Assert.*;

public class SelectableInstanceTest {

    @Test
    public void shouldUpdateMaterialWhenSelectableInstanceIsSelected() {
        SelectableInstance selectableInstance1 = new SelectableInstance(new Model(), new Material(), null);
        SelectableInstance selectableInstance2 = new SelectableInstance(new Model(), new Material(), null);
        selectableInstance1.setSelected(true);

        // Materials should differ from each other.
        assertNotEquals(selectableInstance1.materials.get(0), selectableInstance2.materials.get(0));
    }
}