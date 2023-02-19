package com.lhp.paint;

public enum LineStyle {

    LINE_STYLE_SOLID(0),
    LINE_STYLE_DOTTED(1);

    private byte value;

    private LineStyle(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
