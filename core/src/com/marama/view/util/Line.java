package com.marama.view.util;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;

public class Line {
    Vector3 start;
    Vector3 end;
    Color color;

    public Line(Vector3 start, Vector3 end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }
}
