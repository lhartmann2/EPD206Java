package com.lhp.paint;

import com.lhp.bitmap.BMPFileHeader;
import com.lhp.bitmap.BMPInfo;
import com.lhp.bitmap.RGBQuad;
import com.lhp.font.CNFont;
import com.lhp.font.Font;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class GUIPaint implements Paint {

    private PaintImage paintImage;

    private final Color IMAGE_BACKGROUND = Color.WHITE;
    private final Color FONT_FOREGROUND = Color.BLACK;
    private final Color FONT_BACKGROUND = Color.WHITE;
    private final DotStyle DOT_STYLE_DFT = DotStyle.DOT_FILL_AROUND;
    private final DotPixel DOT_PIXEL_DFT = DotPixel.DOT_PIXEL_1X1;

    @Override
    public void Paint_NewImage(PaintImage image, int width, int height, Rotate rotate, Color color) {
        this.paintImage = image;
        this.paintImage.setWidthMemory(width);
        this.paintImage.setHeightMemory(height);
        this.paintImage.setColor(color);

        this.paintImage.setWidthByte((width % 8 == 0) ? (width / 8) : (width / 8 + 1));
        this.paintImage.setHeightByte(height);

        System.out.println("widthByte: "+this.paintImage.getWidthByte()+"\nheightByte: "+this.paintImage.getHeightByte()+"\n");

        this.paintImage.setRotate(rotate);
        this.paintImage.setMirror(MirrorImage.MIRROR_NONE);

        if((rotate == Rotate.ROTATE_0) || (rotate == Rotate.ROTATE_180)) {
            this.paintImage.setWidth(width);
            this.paintImage.setHeight(height);
        } else {
            this.paintImage.setWidth(height);
            this.paintImage.setHeight(width);
        }
    }

    @Override
    public void Paint_SelectImage(PaintImage image) {
        this.paintImage = image;
    }

    @Override
    public void Paint_SetRotate(Rotate rotate) {
        if((rotate == Rotate.ROTATE_0) || (rotate == Rotate.ROTATE_90) ||
                (rotate == Rotate.ROTATE_180) || (rotate == Rotate.ROTATE_270)) {
            System.out.println("Set image rotate: "+rotate.getValue()+"\n");
            this.paintImage.setRotate(rotate);
        } else {
            throw new RuntimeException("Paint_SetRotate: Invalid Rotate value\n");
        }
    }

    @Override
    public void Paint_SetMirroring(MirrorImage mirror) {
        if((mirror == MirrorImage.MIRROR_NONE) || (mirror == MirrorImage.MIRROR_HORIZONTAL) ||
            (mirror == MirrorImage.MIRROR_VERTICAL) || (mirror == MirrorImage.MIRROR_ORIGIN)) {
            System.out.println("Set mirror: "+mirror.getValue()+"\n");
            this.paintImage.setMirror(mirror);
        } else {
            throw new RuntimeException("Paint_SetMirroring: Invalid MirrorImage value.\n");
        }
    }

    @Override
    public int Paint_SetPixel(int xPoint, int yPoint, Color color) {
        if((xPoint > this.paintImage.getWidth()) || (yPoint > this.paintImage.getHeight())) {
            System.out.println("Paint_SetPixel: Point exceeds display boundaries.\n");
            return -1;
        }

        int x, y;
        switch (this.paintImage.getRotate()) {
            case ROTATE_0:
                x = xPoint;
                y = yPoint;
                break;
            case ROTATE_90:
                x = this.paintImage.getWidthMemory() - yPoint - 1;
                y = xPoint;
                break;
            case ROTATE_180:
                x = this.paintImage.getWidthMemory() - xPoint - 1;
                y = this.paintImage.getHeightMemory() - yPoint - 1;
                break;
            case ROTATE_270:
                x = yPoint;
                y = this.paintImage.getHeightMemory() - xPoint - 1;
                break;
            default:
                System.out.println("Paint_SetPixel: Invalid Rotate value.\n");
                return -1;
        }

        switch (this.paintImage.getMirror()) {
            case MIRROR_NONE:
                break;
            case MIRROR_HORIZONTAL:
                x = this.paintImage.getWidthMemory() - x - 1;
                break;
            case MIRROR_VERTICAL:
                y = this.paintImage.getHeightMemory() - y -1;
                break;
            case MIRROR_ORIGIN:
                x = this.paintImage.getWidthMemory() - x -1;
                y = this.paintImage.getHeightMemory() - y -1;
                break;
            default:
                System.out.println("Paint_SetPixel: Invalid Mirror value.\n");
                return -1;
        }

        if(x > this.paintImage.getWidthMemory() || y > this.paintImage.getHeightMemory()) {
            System.out.println("Paint_SetPixel: Exceeding display boundaries.\n");
            return -1;
        }

        double addr = x / 8 + y * this.paintImage.getWidthByte();
        byte rData = this.paintImage.getImage()[(int)addr];

        if(color == Color.BLACK)
            this.paintImage.setImageAddr(addr, (byte)(rData & ~(0x80 >> (x % 8))));
        else
            this.paintImage.setImageAddr(addr, (byte)(rData | (0x80 >> (x % 8))));

        return 0;
    }

    @Override
    public void Paint_Clear(Color color) {
        int x, y;

        for(y = 0; y < this.paintImage.getHeightByte(); y++) {
            for(x = 0; x < this.paintImage.getWidthByte(); x++) {
                double addr = x + y*this.paintImage.getWidthByte();
                this.paintImage.setImageAddr(addr, color.getValue());
            }
        }

    }

    @Override
    public void Paint_ClearWindows(int xStart, int yStart, int xEnd, int yEnd, Color color) {
        int x, y;
        for(y = yStart; y < yEnd; y++) {
            for(x = xStart; x < xEnd; x++) {
                Paint_SetPixel(x, y, color);
            }
        }
    }

    @Override
    public int Paint_DrawPoint(int xPoint, int yPoint, Color color, DotPixel dotPixel, DotStyle dotStyle) {
        if((xPoint > this.paintImage.getWidth()) || (yPoint > this.paintImage.getHeight())) {
            System.out.println("Paint_DrawPoint: Input exceeds display boundaries.\n");
            return -1;
        }

        int xDirNum, yDirNum;
        if(dotStyle == DotStyle.DOT_FILL_AROUND) {
            for(xDirNum = 0; xDirNum < 2 * dotPixel.getValue() - 1; xDirNum++) {
                for(yDirNum = 0; yDirNum < 2 * dotPixel.getValue() - 1; yDirNum++) {
                    if((xPoint + xDirNum - dotPixel.getValue() < 0) ||
                            (yPoint + yDirNum - dotPixel.getValue() < 0))
                        break;
                    Paint_SetPixel(xPoint + xDirNum - dotPixel.getValue(),
                            yPoint + yDirNum - dotPixel.getValue(), color);
                }
            }
        } else {
            for(xDirNum = 0; xDirNum < dotPixel.getValue(); xDirNum++) {
                for(yDirNum = 0; yDirNum < dotPixel.getValue(); yDirNum++) {
                    Paint_SetPixel(xPoint + xDirNum - 1,
                            yPoint + yDirNum - 1, color);
                }
            }
        }
        return 0;
    }

    @Override
    public int Paint_DrawLine(int xStart, int yStart, int xEnd, int yEnd, Color color, LineStyle lineStyle, DotPixel dotPixel) {
        if((xStart > this.paintImage.getWidth()) || (yStart > this.paintImage.getHeight()) ||
                (xEnd > this.paintImage.getWidth()) || (yEnd > this.paintImage.getHeight())) {
            System.out.println("Paint_DrawLine: Input exceeds display boundaries.\n");
            return -1;
        }

        int xPoint = xStart;
        int yPoint = yStart;
        int dx = (xEnd - xStart >= 0 ? xEnd - xStart : xStart - xEnd);
        int dy = (yEnd - yStart <= 0 ? yEnd - yStart : yStart - yEnd);

        //Increment direction, 1 is pos, -1 is counter
        int xAddWay = xStart < xEnd ? 1 : -1;
        int yAddWay = yStart < yEnd ? 1 : -1;

        int esp = dx + dy;
        char dottedLen = 0;

        for(;;) {
            dottedLen++;
            if((lineStyle == LineStyle.LINE_STYLE_DOTTED) && (dottedLen % 3 == 0)) {
                Paint_DrawPoint(xPoint, yPoint, IMAGE_BACKGROUND, dotPixel, DOT_STYLE_DFT);
                dottedLen = 0;
            } else {
                Paint_DrawPoint(xPoint, yPoint, color, dotPixel, DOT_STYLE_DFT);
            }
            if(2 * esp >= dy) {
                if(xPoint == xEnd)
                    break;
                esp += dy;
                xPoint += xAddWay;
            }
            if(2 * esp <= dx) {
                if(yPoint == yEnd)
                    break;
                esp += dx;
                yPoint += yAddWay;
            }
        }
        return 0;
    }

    @Override
    public int Paint_DrawRectangle(int xStart, int yStart, int xEnd, int yEnd, Color color, DrawFill drawFill, DotPixel dotPixel) {
        if((xStart > this.paintImage.getWidth()) || (yStart > this.paintImage.getHeight()) ||
                (xEnd > this.paintImage.getWidth()) || (yEnd > this.paintImage.getHeight())) {
            System.out.println("Paint_DrawRectangle: Input exceeds display bounadaries.\n");
            return -1;
        }

        if(drawFill == DrawFill.DRAW_FILL_FULL) {
            int yPoint;
            for(yPoint = yStart; yPoint < yEnd; yPoint++) {
                Paint_DrawLine(xStart, yPoint, xEnd, yPoint, color, LineStyle.LINE_STYLE_SOLID, dotPixel);
            }
        } else {
            Paint_DrawLine(xStart, yStart, xEnd, yStart, color, LineStyle.LINE_STYLE_SOLID, dotPixel);
            Paint_DrawLine(xStart, yStart, xStart, yEnd, color, LineStyle.LINE_STYLE_SOLID, dotPixel);
            Paint_DrawLine(xEnd, yEnd, xEnd, yStart, color, LineStyle.LINE_STYLE_SOLID, dotPixel);
            Paint_DrawLine(xEnd, yEnd, xStart, yEnd, color, LineStyle.LINE_STYLE_SOLID, dotPixel);
        }
        return 0;
    }

    @Override
    public int Paint_DrawCircle(int xCenter, int yCenter, int radius, Color color, DrawFill drawFill, DotPixel dotPixel) {
        if((xCenter > this.paintImage.getWidth()) || (yCenter >= this.paintImage.getHeight())) {
            System.out.println("Paint_DrawCircle: Input exceeds display boundaries.\n");
            return -1;
        }

        int xCurrent = 0;
        int yCurrent = radius;

        int esp = 3 - (radius << 1);

        int sCountY;

        if(drawFill == DrawFill.DRAW_FILL_FULL) {
            while(xCurrent <= yCurrent) {
                for(sCountY = xCurrent; sCountY <= yCurrent; sCountY++) {
                    Paint_DrawPoint(xCenter + xCurrent, yCenter + sCountY, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter - xCurrent, yCenter + sCountY, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter - sCountY, yCenter + xCurrent, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter - sCountY, yCenter - xCurrent, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter - xCurrent, yCenter - sCountY, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter + xCurrent, yCenter - sCountY, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter + sCountY, yCenter - xCurrent, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                    Paint_DrawPoint(xCenter + sCountY, yCenter + xCurrent, color, DOT_PIXEL_DFT, DOT_STYLE_DFT);
                }
                if(esp < 0)
                    esp += 4 * xCurrent + 6;
                else {
                    esp += 10 + 4 * (xCurrent - yCurrent);
                    yCurrent--;
                }
                xCurrent++;
            }
        } else {
            while(xCurrent <= yCurrent) {
                Paint_DrawPoint(xCenter + xCurrent, yCenter + yCurrent, color, dotPixel, DOT_STYLE_DFT);//1
                Paint_DrawPoint(xCenter - xCurrent, yCenter + yCurrent, color, dotPixel, DOT_STYLE_DFT);//2
                Paint_DrawPoint(xCenter - yCurrent, yCenter + xCurrent, color, dotPixel, DOT_STYLE_DFT);//3
                Paint_DrawPoint(xCenter - yCurrent, yCenter - xCurrent, color, dotPixel, DOT_STYLE_DFT);//4
                Paint_DrawPoint(xCenter - xCurrent, yCenter - yCurrent, color, dotPixel, DOT_STYLE_DFT);//5
                Paint_DrawPoint(xCenter + xCurrent, yCenter - yCurrent, color, dotPixel, DOT_STYLE_DFT);//6
                Paint_DrawPoint(xCenter + yCurrent, yCenter - xCurrent, color, dotPixel, DOT_STYLE_DFT);//7
                Paint_DrawPoint(xCenter + yCurrent, yCenter + xCurrent, color, dotPixel, DOT_STYLE_DFT);//0

                if(esp < 0)
                    esp += 4 * xCurrent + 6;
                else {
                    esp += 10 + 4 * (xCurrent - yCurrent);
                    yCurrent--;
                }
                xCurrent++;
            }
        }
        return 0;
    }

    @Override
    public int Paint_DrawChar(int xStart, int yStart, char AsciiChar, Font font, Color backgroundColor, Color foregroundColor) {
        if((xStart > this.paintImage.getWidth()) || (yStart > this.paintImage.getHeight())) {
            System.out.println("Paint_DrawChar: Input exceeds display boundaries.\n");
            return -1;
        }

        if(AsciiChar == ' ') {
            System.out.println("Paint_DrawChar: Skipping space char.\n");
            return 0;
        }

        int page, column;

        int charOffset = (AsciiChar - ' ') * font.getHeight() * ((int) Math.ceil(font.getWidth() / 8.0));
        byte[] ptr = Arrays.copyOfRange(font.getTable(), charOffset, charOffset + font.getHeight() * ((int) Math.ceil(font.getWidth() / 8.0)));

        for(page = 0; page< font.getHeight(); page++) {
            for(column = 0; column < font.getWidth(); column++) {
                boolean b = (ptr[column / 8] & (0x80 >> (column % 8))) != 0;
                if(FONT_BACKGROUND == backgroundColor) {
                    if(b) {
                        Paint_SetPixel(xStart + column, yStart + page, backgroundColor);
                    }
                } else {
                    if(b) {
                        Paint_SetPixel(xStart + column, yStart + page, backgroundColor);
                    } else {
                        Paint_SetPixel(xStart + column, yStart + page, foregroundColor);
                    }
                }
            }
            if(font.getWidth() % 8 != 0) {
                ptr[font.getWidth() / 8] = 0;
            }
            ptr = Arrays.copyOfRange(ptr, (font.getWidth() / 8) + 1, ptr.length);
        }
        return 0;
    }

    @Override
    public int Paint_DrawStringEN(int xStart, int yStart, String string, Font font, Color backgroundColor, Color foregroundColor) {
        if((xStart > this.paintImage.getWidth()) || (yStart > this.paintImage.getHeight())) {
            System.out.println("Pain_DrawStringEN: Input exceeds display boundaries.\n");
            return -1;
        }

        int xPoint = xStart;
        int yPoint = yStart;
        int index=0;

        while(index < string.length()) {
            //Next line if x is at max width
            if((xPoint + font.getWidth()) > this.paintImage.getWidth()) {
                xPoint = xStart;
                yPoint += font.getHeight();
            }

            //reposition to start if y at max height
            if((yPoint + font.getHeight()) > this.paintImage.getHeight()) {
                xPoint = xStart;
                yPoint = yStart;
            }

            Paint_DrawChar(xPoint, yPoint, string.charAt(index), font, foregroundColor, backgroundColor);

            index++;
            xPoint += font.getWidth();

            //System.out.println("xPoint: "+xPoint+"\nyPoint: "+yPoint+"\nindex: "+index+"\n");
        }
        return 0;
    }

    @Override
    public int Paint_DrawStringCN(int xStart, int yStart, String string, CNFont cFont, Color backgroundColor, Color foregroundColor) {
        System.out.println("Not implemented.");
        return 0;
    }

    @Override
    public int Paint_DrawBitmap(byte[] imageBuffer) {
        int x, y, addr;

        for(y = 0; y < paintImage.getHeightByte(); y++) {
            for(x = 0; x < paintImage.getWidthByte(); x++) {
                addr = x + y * paintImage.getWidthByte();
                paintImage.setImageAddr(addr, imageBuffer[addr]);
            }
        }

        return 0;
    }

    @Override
    public int Paint_ReadBitmap(String path, int xStart, int yStart) {

        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] header = new byte[54];

            fileInputStream.read(header);

            int bmpWidth = Byte.toUnsignedInt(header[18]) +
                    (Byte.toUnsignedInt(header[19]) << 8) +
                    (Byte.toUnsignedInt(header[20]) << 16) +
                    (Byte.toUnsignedInt(header[21]) << 24);
            int bmpHeight = Byte.toUnsignedInt(header[22]) +
                    (Byte.toUnsignedInt(header[23]) << 8) +
                    (Byte.toUnsignedInt(header[24]) << 16) +
                    (Byte.toUnsignedInt(header[25]) << 24);
            int bitsPerPixel = Byte.toUnsignedInt(header[28]);
            System.out.println("bmpWidth: "+bmpWidth+"\nbmpHeight: "+bmpHeight+"\nbpp: "+bitsPerPixel);

            if(bitsPerPixel != 1) {
                System.out.println("Paint_ReadBitmap: Specified BMP image is not monochrome.");
                return -1;
            }

            int paletteStart = Byte.toUnsignedInt(header[10]) +
                    (Byte.toUnsignedInt(header[11]) << 8);
            byte[] palette = new byte[4];
            fileInputStream.read(palette);
            Color bColor, wColor;
            if((palette[0] == (byte)0xFF) && (palette[1] == (byte)0xFF) && (palette[2] == (byte)0xFF))
            {
                bColor = Color.BLACK;
                wColor = Color.WHITE;
            } else {
                bColor = Color.WHITE;
                wColor = Color.BLACK;
            }

            //Read image into cache
            int rowSize = ((bmpWidth + 32) / 32) * 4;
            int pixelArraySize = rowSize * bmpHeight;
            byte[] image = new byte[pixelArraySize];
            int totalBytesRead = 0;

            while(totalBytesRead < pixelArraySize) {
                int bytesRemaining = pixelArraySize - totalBytesRead;
                int bytesReadThisRound = fileInputStream.read(image, totalBytesRead, bytesRemaining);
                if(bytesReadThisRound < 0) {
                    throw new IOException("EOF reached too early.");
                }
                totalBytesRead += bytesReadThisRound;
            }

            //Write to display buffer
            Color color;
            byte temp;
            int imageWidthByte = (bmpWidth % 8 == 0) ? (bmpWidth / 8) : (bmpWidth / 8 + 1);

            for(int y=0; y < bmpHeight; y++) {
                for(int x=0; x<bmpWidth; x++) {
                    if((x > this.paintImage.getWidth()) || (y > this.paintImage.getHeight())) {
                        System.out.println("BREAK");
                        break;
                    }
                    temp = image[(x/8) + (y * imageWidthByte)];
                    color = (((temp << (x%8)) & 0x80) == 0x80) ? bColor : wColor;
                    Paint_SetPixel(xStart + x, yStart + y, color);
                }
            }

        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /*@Override
    public int Paint_ReadBitmap(String path, int xStart, int yStart) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);

            //Read BMP File header
            ByteBuffer bb = ByteBuffer.allocate(14);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort(dataInputStream.readShort());
            bb.putInt(dataInputStream.readInt());
            bb.putShort((short)0);
            bb.putShort((short)0);
            bb.putInt(dataInputStream.readInt());
            BMPFileHeader bmpFileHeader = new BMPFileHeader(bb.array());

            //Read BMP info header
            bb = ByteBuffer.allocate(40);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            dataInputStream.read(bb.array(), 0, 40);
            BMPInfo bmpInfo = new BMPInfo(bb.array());

            System.out.println("Paint_ReadBitmap: Pixels = " +bmpInfo.getWidth() + " * " +bmpInfo.getHeight());

            //Calc width of image in bytes
            short imageWidthByte = (short) ((bmpInfo.getWidth() % 8 == 0) ? (bmpInfo.getWidth() / 8) : (bmpInfo.getWidth() / 8 + 1));
            short bmpWidthByte = (short) ((imageWidthByte % 4 == 0) ? imageWidthByte : ((imageWidthByte / 4 + 1) * 4));

            //Allocate memory for image data
            byte[] image = new byte[imageWidthByte * bmpInfo.getHeight()];
            Arrays.fill(image, (byte) 0xFF);

            //Determine if bitmap is monochrome
            int readByte = bmpInfo.getBitCount();
            if(readByte != 1) {
                System.out.println("Paint_ReadBitmap: Specified BMP image in not monochrome.");
                //System.out.println("Continuing...");
                //return -1;
            }

            //Determine black and white based on palette
            short bmpRgbQuadSize = (short) Math.pow(2, bmpInfo.getBitCount());
            RGBQuad[] rgbQuads = new RGBQuad[bmpRgbQuadSize];
            for(int i=0; i < bmpRgbQuadSize; i++) {
                bb = ByteBuffer.allocate(4);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                dataInputStream.read(bb.array(), 0, 4);
                rgbQuads[i] = new RGBQuad(bb.array());
            }

            Color bColor, wColor;
            if((rgbQuads[0].getRgbBlue() == (byte)0xFF) && (rgbQuads[0].getRgbGreen() == (byte)0xFF)
                && (rgbQuads[0].getRgbRed() == (byte)0xFF)) {
                bColor = Color.BLACK;
                wColor = Color.WHITE;
            } else {
                bColor = Color.WHITE;
                wColor = Color.BLACK;
            }

            //Read image data into cache.
            short x, y;
            byte rData;
            dataInputStream.skipBytes(bmpFileHeader.getOffset() - 14 - 40); //Skip to beginning of image data

            for(y = 0; y < bmpInfo.getHeight(); y++) {
                for(x = 0; x< bmpWidthByte; x++) {
                    if(dataInputStream.readByte() != -1) {
                        rData = dataInputStream.readByte();
                        if(x < imageWidthByte) {
                            image[x + (bmpInfo.getHeight() - y - 1) * imageWidthByte] = rData;
                        }
                    } else {
                        System.out.println("Paint_ReadBitmap: Get BMP Data error.");
                        break;
                    }
                }
            }

            //Draw image to display buffer
            byte temp;
            Color color;
            for(y = 0; y < bmpInfo.getHeight(); y++) {
                for(x = 0; x < bmpInfo.getWidth(); x++) {
                    if(x > paintImage.getWidth() || y > paintImage.getHeight()) {
                        break;
                    }
                    temp = image[(x / 8) + (y * imageWidthByte)];
                    color = (((temp << (x%8)) & 0x80) == 0x80) ? bColor : wColor;
                    Paint_SetPixel(xStart + x, yStart + y, color);
                }
            }

            fileInputStream.close();
            dataInputStream.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }*/

    public PaintImage getPaintImage() {
        return paintImage;
    }
}
