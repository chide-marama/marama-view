package com.marama.view.util;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;

public class Line {
    public Vector3 start;
    public Vector3 end;
    public Color color;

    public Line(Vector3 start, Vector3 end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.line(start, end);
    }
}
