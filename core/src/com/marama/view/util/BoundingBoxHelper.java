package com.marama.view.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;


public abstract class BoundingBoxHelper {
    public static Array<Vector3> vertices(BoundingBox boundingBox) {
        Array<Vector3> bounds = new Array<Vector3>();
        bounds.add(boundingBox.getCorner000(new Vector3()));
        bounds.add(boundingBox.getCorner001(new Vector3()));
        bounds.add(boundingBox.getCorner010(new Vector3()));
        bounds.add(boundingBox.getCorner011(new Vector3()));
        bounds.add(boundingBox.getCorner100(new Vector3()));
        bounds.add(boundingBox.getCorner101(new Vector3()));
        bounds.add(boundingBox.getCorner110(new Vector3()));
        bounds.add(boundingBox.getCorner111(new Vector3()));
        return bounds;
    }

    public static void draw(ShapeRenderer shapeRenderer, Vector3 origin, BoundingBox boundingBox) {
        Vector3 vertex1 = boundingBox.getCorner000(new Vector3());
        Vector3 vertex2 = boundingBox.getCorner001(new Vector3());
        Vector3 vertex3 = boundingBox.getCorner010(new Vector3());
        Vector3 vertex4 = boundingBox.getCorner011(new Vector3());
        Vector3 vertex5 = boundingBox.getCorner100(new Vector3());
        Vector3 vertex6 = boundingBox.getCorner101(new Vector3());
        Vector3 vertex7 = boundingBox.getCorner110(new Vector3());
        Vector3 vertex8 = boundingBox.getCorner111(new Vector3());

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.identity();

        shapeRenderer.line(vertex1, vertex2);
        shapeRenderer.line(vertex2, vertex3);
        shapeRenderer.line(vertex3, vertex4);
        shapeRenderer.line(vertex4, vertex5);
        shapeRenderer.line(vertex5, vertex6);
        shapeRenderer.line(vertex6, vertex7);
        shapeRenderer.line(vertex7, vertex8);
        shapeRenderer.line(vertex8, vertex1);
    }
}
