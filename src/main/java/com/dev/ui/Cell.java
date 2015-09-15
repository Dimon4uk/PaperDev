package com.dev.ui;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle  {

    public Cell(double size) {
        super(size, size);
        this.setStroke(Color.BLUE);
        this.setFill(Color.WHITE);

    }
}
