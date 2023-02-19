package com.lhp.bitmap;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BMPFileHeader {

    private int type;
    private int size;
    private int reserved1 = 0;
    private int reserved2 = 0;
    private int offset;

    public BMPFileHeader() {
    }

    public BMPFileHeader(int type, int size, int offset) {
        this.type = type;
        this.size = size;
        this.offset = offset;
    }

    public BMPFileHeader(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        this.type = bb.getShort(0);
        this.size = bb.getInt(2);
        this.reserved1 = bb.getShort(6);
        this.reserved2 = bb.getShort(8);
        this.offset = bb.getInt(10);

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getReserved1() {
        return reserved1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void read(RandomAccessFile raf) throws IOException {
        this.type = raf.readShort();
        this.size = raf.readInt();
        this.offset = raf.readInt();
    }

    @Override
    public String toString() {
        return "BMPFileHeader{" +
                "type=" + type +
                ", size=" + size +
                ", reserved1=" + reserved1 +
                ", reserved2=" + reserved2 +
                ", offset=" + offset +
                '}';
    }
}
