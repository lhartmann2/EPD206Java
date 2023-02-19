package com.lhp.font;

//GB2312 (Simplified Chinese)
public class ChCN {
    private char[] index = new char[2];
    private final char[] matrix = new char[Fonts.MAX_HEIGHT_FONT.getValue()*Fonts.MAX_WIDTH_FONT.getValue()/8];

    public char[] getIndex() {
        return index;
    }

    public void setIndex(char[] index) {
        this.index = index;
    }

    public char[] getMatrix() {
        return matrix;
    }
}
