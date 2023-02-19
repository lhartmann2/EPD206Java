package com.lhp.paint;

public enum MirrorImage {

    MIRROR_NONE(0x00),
    MIRROR_HORIZONTAL(0x01),
    MIRROR_VERTICAL(0x02),
    MIRROR_ORIGIN(0x03);

    private byte value;

    private MirrorImage(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
