package com.lhp.paint;

public class PaintImage {
    private byte[] image;
    private int width, height, widthMemory, heightMemory;
    private Color color;
    private Rotate rotate;
    private MirrorImage mirror;
    private int widthByte, heightByte;

    public PaintImage() {
        this.image = new byte[44993];
    }

    public PaintImage(byte[] image, int width, int height, int widthMemory, int heightMemory, Color color, Rotate rotate, MirrorImage mirror, int widthByte, int heightByte) {
        if(image.length < 1)
            this.image = new byte[44993];
        else
            this.image = image;
        this.width = width;
        this.height = height;
        this.widthMemory = widthMemory;
        this.heightMemory = heightMemory;
        this.color = color;
        this.rotate = rotate;
        this.mirror = mirror;
        this.widthByte = widthByte;
        this.heightByte = heightByte;
    }

    public byte[] getImage() {
        return image;
    }

    public byte getImageAtAddr(int addr) {
        return image[addr];
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setImageAddr(double addr, byte data) {
        this.image[(int)addr] = data;
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

    public int getWidthMemory() {
        return widthMemory;
    }

    public void setWidthMemory(int widthMemory) {
        this.widthMemory = widthMemory;
    }

    public int getHeightMemory() {
        return heightMemory;
    }

    public void setHeightMemory(int heightMemory) {
        this.heightMemory = heightMemory;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rotate getRotate() {
        return rotate;
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    public MirrorImage getMirror() {
        return mirror;
    }

    public void setMirror(MirrorImage mirror) {
        this.mirror = mirror;
    }

    public int getWidthByte() {
        return widthByte;
    }

    public void setWidthByte(int widthByte) {
        this.widthByte = widthByte;
    }

    public int getHeightByte() {
        return heightByte;
    }

    public void setHeightByte(int heightByte) {
        this.heightByte = heightByte;
    }
}


