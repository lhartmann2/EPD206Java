package com.lhp.font;

public enum Fonts {
    MAX_HEIGHT_FONT(41),
    MAX_WIDTH_FONT(32);

    private byte value;

    private Fonts(int value) {
        this.value = (byte)value;
    }

    public byte getValue() {
        return value;
    }
}
