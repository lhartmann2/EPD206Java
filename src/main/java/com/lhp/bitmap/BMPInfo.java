package com.lhp.bitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BMPInfo {

    private int infoSize;
    private int width;
    private int height;
    private short planes;
    private short bitCount;
    private int compression;
    private int imageSize;
    private int xPelsPerMeter;
    private int yPelsPerMeter;
    private int colorsUsed;
    private int colorsImportant;

    public BMPInfo() {
    }

    public BMPInfo(int infoSize, int width, int height, short planes, short bitCount, int compression, int imageSize, int xPelsPerMeter, int yPelsPerMeter, int colorsUsed, int colorsImportant) {
        this.infoSize = infoSize;
        this.width = width;
        this.height = height;
        this.planes = planes;
        this.bitCount = bitCount;
        this.compression = compression;
        this.imageSize = imageSize;
        this.xPelsPerMeter = xPelsPerMeter;
        this.yPelsPerMeter = yPelsPerMeter;
        this.colorsUsed = colorsUsed;
        this.colorsImportant = colorsImportant;
    }

    public BMPInfo(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        this.infoSize = bb.getInt();
        this.width = bb.getInt();
        this.height = bb.getInt();
        this.planes = bb.getShort();
        this.bitCount = bb.getShort();
        this.compression = bb.getInt();
        this.imageSize = bb.getInt();
        this.xPelsPerMeter = bb.getInt();
        this.yPelsPerMeter = bb.getInt();
        this.colorsUsed = bb.getInt();
        this.colorsImportant = bb.getInt();
    }

    public int getInfoSize() {
        return infoSize;
    }

    public void setInfoSize(int infoSize) {
        this.infoSize = infoSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public short getPlanes() {
        return planes;
    }

    public void setPlanes(short planes) {
        this.planes = planes;
    }

    public short getBitCount() {
        return bitCount;
    }

    public void setBitCount(short bitCount) {
        this.bitCount = bitCount;
    }

    public int getCompression() {
        return compression;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getxPelsPerMeter() {
        return xPelsPerMeter;
    }

    public void setxPelsPerMeter(int xPelsPerMeter) {
        this.xPelsPerMeter = xPelsPerMeter;
    }

    public int getyPelsPerMeter() {
        return yPelsPerMeter;
    }

    public void setyPelsPerMeter(int yPelsPerMeter) {
        this.yPelsPerMeter = yPelsPerMeter;
    }

    public int getColorsUsed() {
        return colorsUsed;
    }

    public void setColorsUsed(int colorsUsed) {
        this.colorsUsed = colorsUsed;
    }

    public int getColorsImportant() {
        return colorsImportant;
    }

    public void setColorsImportant(int colorsImportant) {
        this.colorsImportant = colorsImportant;
    }

    @Override
    public String toString() {
        return "BMPInfo{" +
                "infoSize=" + infoSize +
                ", width=" + width +
                ", height=" + height +
                ", planes=" + planes +
                ", bitCount=" + bitCount +
                ", compression=" + compression +
                ", imageSize=" + imageSize +
                ", xPelsPerMeter=" + xPelsPerMeter +
                ", yPelsPerMeter=" + yPelsPerMeter +
                ", colorsUsed=" + colorsUsed +
                ", colorsImportant=" + colorsImportant +
                '}';
    }
}
