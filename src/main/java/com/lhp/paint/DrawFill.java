package com.lhp.paint;

public enum DrawFill {

    DRAW_FILL_EMPTY(0),
    DRAW_FILL_FULL(1);

    private byte value;

    private DrawFill(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
