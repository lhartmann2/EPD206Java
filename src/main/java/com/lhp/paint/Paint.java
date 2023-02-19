package com.lhp.paint;

import com.lhp.font.CNFont;
import com.lhp.font.Font;

import java.io.File;
import java.nio.file.Path;

public interface Paint {

    //Init & Clear
    void Paint_NewImage(PaintImage image, int width, int height, Rotate rotate, Color color);
    void Paint_SelectImage(PaintImage image);
    void Paint_SetRotate(Rotate rotate);
    void Paint_SetMirroring(MirrorImage mirror);
    int Paint_SetPixel(int xPoint, int yPoint, Color color);

    void Paint_Clear(Color color);
    void Paint_ClearWindows(int xStart, int yStart, int xEnd, int yEnd, Color color);

    //Drawing
    int Paint_DrawPoint(int xPoint, int yPoint, Color color, DotPixel dotPixel, DotStyle dotStyle);
    int Paint_DrawLine(int xStart, int yStart, int xEnd, int yEnd, Color color, LineStyle lineStyle, DotPixel dotPixel);
    int Paint_DrawRectangle(int xStart, int yStart, int xEnd, int yEnd, Color color, DrawFill drawFill, DotPixel dotPixel);
    int Paint_DrawCircle(int xCenter, int yCenter, int radius, Color color, DrawFill drawFill, DotPixel dotPixel);

    //Display String
    int Paint_DrawChar(int xStart, int yStart, char AsciiChar, Font font, Color backgroundColor, Color foregroundColor);
    int Paint_DrawStringEN(int xStart, int yStart, String string, Font font, Color backgroundColor, Color foregroundColor);
    int Paint_DrawStringCN(int xStart, int yStart, String string, CNFont cFont, Color backgroundColor, Color foregroundColor);

    //Pic
    int Paint_ReadBitmap(String path, int xStart, int yStart);
    int Paint_DrawBitmap(byte[] imageBuffer);
}
