package com.lhp.paint;

public enum Rotate {
    ROTATE_0(0),
    ROTATE_90(90),
    ROTATE_180(180),
    ROTATE_270(270);

    private int value;

    private Rotate(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
