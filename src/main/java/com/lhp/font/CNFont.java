package com.lhp.font;

//GB2312 (Simplified Chinese)
public class CNFont {
    private byte[][] table;
    int size;
    int ASCIIWidth;
    int width;
    int height;

    public CNFont() {
    }

    public CNFont(byte[][] table, int size, int ASCIIWidth, int width, int height) {
        this.table = table;
        this.size = size;
        this.ASCIIWidth = ASCIIWidth;
        this.width = width;
        this.height = height;
    }

    public byte[][] getTable() {
        return table;
    }

    public void setTable(byte[][] table) {
        this.table = table;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getASCIIWidth() {
        return ASCIIWidth;
    }

    public void setASCIIWidth(int ASCIIWidth) {
        this.ASCIIWidth = ASCIIWidth;
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
}
