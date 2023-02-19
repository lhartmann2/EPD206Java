package com.lhp.paint;

public enum DotPixel {

    DOT_PIXEL_1X1(1),
    DOT_PIXEL_2X2(2),
    DOT_PIXEL_3X3(3),
    DOT_PIXEL_4X4(4),
    DOT_PIXEL_5X5(5),
    DOT_PIXEL_6X6(6),
    DOT_PIXEL_7X7(7),
    DOT_PIXEL_8X8(8);

    private byte value;

    private DotPixel(int value) {
        this.value = (byte)value;
    }

    public byte getValue() {
        return value;
    }
}
