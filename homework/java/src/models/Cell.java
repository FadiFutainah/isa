package models;


import utils.Colors;

import java.util.Objects;

public class Cell {
    private int index;
    private String color;

    public Cell() {
        color = Colors.BLACK;
        index = -1;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isMovable() {
        return index != -1;
    }

    public boolean isNotBrick() {
        return !Objects.equals(color, Colors.GREEN);
    }
}
