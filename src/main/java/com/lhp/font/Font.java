package com.lhp.font;

//ASCII
public class Font {
    byte[] table;
    int width, height;

    public Font() {}

    public Font(byte[] table, int width, int height) {
        this.table = table;
        this.width = width;
        this.height = height;
    }

    public byte[] getTable() {
        return table;
    }

    public void setTable(byte[] table) {
        this.table = table;
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

    public int getByte(int ptr) {
        return table[ptr];
    }
}
