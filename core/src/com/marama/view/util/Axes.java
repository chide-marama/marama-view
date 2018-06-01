package com.marama.view.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public abstract class Axes {
    public static void draw(ShapeRenderer shapeRenderer, Vector3 origin) {
        Vector3 target;

        // X
        shapeRenderer.setColor(Color.RED);
        target = new Vector3(2, 0, 0).add(origin);
        shapeRenderer.line(origin, target);

        // X
        shapeRenderer.setColor(Color.BLUE);
        target = new Vector3(0, 2, 0).add(origin);
        shapeRenderer.line(origin, target);

        // X
        shapeRenderer.setColor(Color.GREEN);
        target = new Vector3(0, 0, 2).add(origin);
        shapeRenderer.line(origin, target);
    }
}
