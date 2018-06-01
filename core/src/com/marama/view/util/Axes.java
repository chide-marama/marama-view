package com.marama.view.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class Axes {
    public float length = 1.5f;
    public float coneLength = 0.15f;
    public float coneHeight = 0.05f;
    public int coneSegments = 16;

    public void draw(ShapeRenderer shapeRenderer, Vector3 origin) {
        Vector3 target;

        // X Line
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        target = new Vector3(length, 0, 0).add(origin);
        shapeRenderer.line(origin, target);

        // X Cone
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.translate(target.x, target.y, target.z);
        shapeRenderer.rotate(0, 1, 0, 90);
        shapeRenderer.cone(0, 0, 0, coneHeight, coneLength, coneSegments);

        // Y Line
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        target = new Vector3(0, length, 0).add(origin);
        shapeRenderer.line(origin, target);

        // Y Cone
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.translate(target.x, target.y, target.z);
        shapeRenderer.rotate(1, 0, 0, -90);
        shapeRenderer.cone(0, 0, 0, coneHeight, coneLength, coneSegments);

        // Z Line
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        target = new Vector3(0, 0, length).add(origin);
        shapeRenderer.line(origin, target);

        // Z Cone
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.translate(target.x, target.y, target.z);
        shapeRenderer.cone(0, 0, 0, coneHeight, coneLength, coneSegments);
    }
}
