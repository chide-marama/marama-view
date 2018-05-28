package com.marama.tests.entities.instances;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.marama.view.entities.instances.EntityInstance;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityInstanceTest {

    @Test
    public void shouldUpdateMaterialWhenSelectableInstanceIsSelected() {
        EntityInstance entityInstance1 = new EntityInstance(new Model(), new Material());
        EntityInstance entityInstance2 = new EntityInstance(new Model(), new Material());
        entityInstance1.setSelected(true);

        // Materials should differ from each other.
        assertNotEquals(entityInstance1.materials.get(0), entityInstance2.materials.get(0));
    }
}