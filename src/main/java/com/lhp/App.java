package com.lhp;

import com.lhp.controller.Controller;
import com.lhp.controller.Image;
import com.lhp.font.*;
import com.lhp.paint.*;

public class App {
    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
            GUIPaint paint = new GUIPaint();
            if(controller.EPD_Init() != 0) {
                throw new RuntimeException("EPD_Init Failed\n");
            }
            controller.EPD_Clear();
            controller.DEV_Delay(500);

            System.out.println("Paint_NewImage\n");
            PaintImage blackImage = new PaintImage();
            System.out.println("blackImage: "+blackImage.toString()+" ("+blackImage.hashCode()+")\n");
            paint.Paint_NewImage(blackImage, controller.EPD_WIDTH, controller.EPD_HEIGHT, Rotate.ROTATE_0, Color.WHITE);
            paint.Paint_SelectImage(blackImage);
            paint.Paint_Clear(Color.WHITE);


            System.out.println("Writing to screen...\n");
            //paint.Paint_DrawStringEN(10, 20, "Lick my dick.", new Font24(), Color.WHITE, Color.BLACK);
            //paint.Paint_DrawChar(10, 10, 'D', new Font8(), Color.BLACK, Color.WHITE);
            //paint.Paint_DrawRectangle(10, 10, 40, 20, Color.BLACK, DrawFill.DRAW_FILL_EMPTY, DotPixel.DOT_PIXEL_1X1);
            //paint.Paint_DrawCircle(50, 50, 10, Color.BLACK, DrawFill.DRAW_FILL_EMPTY, DotPixel.DOT_PIXEL_1X1);
            paint.Paint_ReadBitmap("/home/pi/100x100.bmp", 0, 0);

            controller.EPD_Display(blackImage);
            controller.DEV_Delay(500);

            controller.EPD_SLEEP();
            controller.DEV_Delay(500);
            controller.SPI_Close();
        } catch(Exception e) {
            System.out.println("Caught exception in main: "+e.getMessage()+"\n");
            e.printStackTrace();
        }
    }
}
