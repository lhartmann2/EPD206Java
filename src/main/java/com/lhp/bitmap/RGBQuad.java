package com.lhp.bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RGBQuad {

    private byte rgbBlue;
    private byte rgbGreen;
    private byte rgbRed;
    private byte rgbReversed;

    public RGBQuad() {
    }

    public RGBQuad(byte rgbBlue, byte rgbGreen, byte rgbRed, byte rgbReversed) {
        this.rgbBlue = rgbBlue;
        this.rgbGreen = rgbGreen;
        this.rgbRed = rgbRed;
        this.rgbReversed = rgbReversed;
    }

    public RGBQuad(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        this.rgbBlue = bb.get();
        this.rgbGreen = bb.get();
        this.rgbRed = bb.get();
        this.rgbReversed = bb.get();
    }

    public byte getRgbBlue() {
        return rgbBlue;
    }

    public void setRgbBlue(byte rgbBlue) {
        this.rgbBlue = rgbBlue;
    }

    public byte getRgbGreen() {
        return rgbGreen;
    }

    public void setRgbGreen(byte rgbGreen) {
        this.rgbGreen = rgbGreen;
    }

    public byte getRgbRed() {
        return rgbRed;
    }

    public void setRgbRed(byte rgbRed) {
        this.rgbRed = rgbRed;
    }

    public byte getRgbReversed() {
        return rgbReversed;
    }

    public void setRgbReversed(byte rgbReversed) {
        this.rgbReversed = rgbReversed;
    }

    @Override
    public String toString() {
        return "RGBQuad{" +
                "rgbBlue=" + rgbBlue +
                ", rgbGreen=" + rgbGreen +
                ", rgbRed=" + rgbRed +
                ", rgbReversed=" + rgbReversed +
                '}';
    }
}
