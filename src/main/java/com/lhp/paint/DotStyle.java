package com.lhp.paint;

public enum DotStyle {

    DOT_FILL_AROUND(1), //1x1
    DOT_FILL_RIGHTUP(2); //2x2

    private byte value;

    private DotStyle(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
