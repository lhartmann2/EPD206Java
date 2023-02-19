package com.lhp.paint;

public enum Color {
    WHITE(0xFF),
    BLACK(0x00),
    RED(BLACK.getValue()); //Change on displays that support red / 3rd color

    private byte value;

    private Color(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
