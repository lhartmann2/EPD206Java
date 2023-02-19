package com.lhp.controller;

import com.lhp.paint.PaintImage;
import de.fxworld.jbcm2835.*;

public class Controller {

    public static final RPiGPIOPin EPD_RST_PIN = RPiGPIOPin.RPI_GPIO_P1_11;
    public static final RPiGPIOPin EPD_DC_PIN = RPiGPIOPin.RPI_GPIO_P1_22;
    public static final RPiGPIOPin EPD_CS_PIN = RPiGPIOPin.RPI_GPIO_P1_24;
    public static final RPiGPIOPin EPD_BUSY_PIN = RPiGPIOPin.RPI_GPIO_P1_18;

    public final int EPD_WIDTH = 152;
    public final int EPD_HEIGHT = 296;

    public Controller() {
        SPI_Initialize();
    }

    public int EPD_Init() {
        EPD_Reset();

        EPD_SendCommand(EPD206Vars.BOOSTER_SOFT_START);
        EPD_SendData((byte)0x17);
        EPD_SendData((byte)0x17);
        EPD_SendData((byte)0x17);

        EPD_SendCommand(EPD206Vars.POWER_SETTING);
        EPD_SendData((byte)0x03);
        EPD_SendData((byte)0x00);
        EPD_SendData((byte)0x2B);
        EPD_SendData((byte)0x2B);
        EPD_SendData((byte)0x09);

        EPD_SendCommand(EPD206Vars.POWER_ON);
        //EPD_SendData((byte)0xDF);
        EPD_WaitUntilIdle();

        EPD_SendCommand(EPD206Vars.PANEL_SETTING);
        EPD_SendData((byte)0xDF);

        EPD_SendCommand(EPD206Vars.TCON_RESOLUTION);
        EPD_SendData((byte)0x98);
        EPD_SendData((byte)0x01);
        EPD_SendData((byte)0x28);

        EPD_SendCommand(EPD206Vars.VCOM_AND_DATA_INTERVAL_SETTING);
        EPD_SendData((byte)0xF7);

        return 0;
    }

    public void EPD_SLEEP() {
        EPD_SendCommand(EPD206Vars.POWER_OFF);
        EPD_SendCommand(EPD206Vars.DEEP_SLEEP);
        EPD_SendData((byte)0XA5);
    }


    public void SPI_Close() {
        JBcm2835Library.bcm2835_spi_end();
        JBcm2835Library.close();
    }

    public void EPD_Clear() {
        int width, j, i;

        width = ((EPD_WIDTH % 8 == 0) ? (EPD_WIDTH / 8) : (EPD_WIDTH / 8 + 1));

        EPD_SendCommand(EPD206Vars.DATA_START_TRANSMISSION_1);
        for(j = 0; j < EPD_HEIGHT; j++) {
            for(i = 0; i < width; i++) {
                EPD_SendData((byte)0xFF);
            }
        }

        EPD_SendCommand(EPD206Vars.DATA_START_TRANSMISSION_2);
        for(j = 0; j < EPD_HEIGHT; j++) {
            for(i = 0; i < width; i++) {
                EPD_SendData((byte)0xFF);
            }
        }

        EPD_SendCommand(EPD206Vars.DISPLAY_REFRESH);
        EPD_WaitUntilIdle();
    }

    public void DEV_Delay(int ms) {
        JBcm2835Library.bcm2835_delay(ms);
    }

    public void EPD_Display(PaintImage image) {
        int width, height, j, i;
        width = (EPD_WIDTH % 8 == 0) ? (EPD_WIDTH / 8) : (EPD_WIDTH / 8 + 1);
        height = EPD_HEIGHT;

        EPD_SendCommand(EPD206Vars.DATA_START_TRANSMISSION_1);
        for(j = 0; j < height; j++) {
            for(i = 0; i < width; i++) {
                EPD_SendData((byte)0XFF);
            }
        }

        EPD_SendCommand(EPD206Vars.DATA_START_TRANSMISSION_2);
        for(j = 0; j < height; j++) {
            for(i = 0; i < width; i++) {
                EPD_SendData(image.getImageAtAddr(i + j * width));
            }
        }

        EPD_SendCommand(EPD206Vars.DISPLAY_REFRESH);
        EPD_WaitUntilIdle();
    }

    private void SPI_Initialize() {
        if(JBcm2835Library.init() != 1) {
            throw new RuntimeException("Failed to Init");
        } else {
            System.out.println("BCM2835 Init Success\n");
        }

        GPIOConfig();

        JBcm2835Library.bcm2835_spi_begin();
        JBcm2835Library.bcm2835_spi_setBitOrder(SPIBitOrder.MSBFIRST);
        JBcm2835Library.bcm2835_spi_setDataMode(SPIMode.SPI_MODE0);
        JBcm2835Library.bcm2835_spi_setClockDivider(SPIClockDivider.SPI_CLOCK_DIVIDER_128);
        JBcm2835Library.bcm2835_spi_chipSelect(SPIChipSelect.SPI_CS0);
        JBcm2835Library.bcm2835_spi_setChipSelectPolarity(SPIChipSelect.SPI_CS0.getValue(), JBcm2835Library.LOW);

        System.out.println("SPI Initialized\n");
    }

    private void GPIOConfig() {
        //Output
        JBcm2835Library.bcm2835_gpio_fsel(EPD_RST_PIN, FunctionSelect.GPIO_FSEL_OUTP);
        JBcm2835Library.bcm2835_gpio_fsel(EPD_DC_PIN, FunctionSelect.GPIO_FSEL_OUTP);
        JBcm2835Library.bcm2835_gpio_fsel(EPD_CS_PIN, FunctionSelect.GPIO_FSEL_OUTP);

        //Input
        JBcm2835Library.bcm2835_gpio_fsel(EPD_BUSY_PIN, FunctionSelect.GPIO_FSEL_INPT);
    }

    private void EPD_SendData(byte data) {
        JBcm2835Library.bcm2835_gpio_write(EPD_DC_PIN, JBcm2835Library.HIGH);
        JBcm2835Library.bcm2835_gpio_write(EPD_CS_PIN, JBcm2835Library.LOW);
        JBcm2835Library.bcm2835_spi_transfer(data);
        JBcm2835Library.bcm2835_gpio_write(EPD_CS_PIN, JBcm2835Library.HIGH);
    }

    private void EPD_SendCommand(EPD206Vars reg) {
        JBcm2835Library.bcm2835_gpio_write(EPD_DC_PIN, JBcm2835Library.LOW);
        JBcm2835Library.bcm2835_gpio_write(EPD_CS_PIN, JBcm2835Library.LOW);
        JBcm2835Library.bcm2835_spi_transfer(reg.getValue());
        JBcm2835Library.bcm2835_gpio_write(EPD_CS_PIN, JBcm2835Library.HIGH);
    }

    private void EPD_Reset() {
        JBcm2835Library.bcm2835_gpio_write(EPD_RST_PIN, JBcm2835Library.HIGH);
        JBcm2835Library.bcm2835_delay(200);
        JBcm2835Library.bcm2835_gpio_write(EPD_RST_PIN, JBcm2835Library.LOW);
        JBcm2835Library.bcm2835_delay(200);
        JBcm2835Library.bcm2835_gpio_write(EPD_RST_PIN, JBcm2835Library.HIGH);
        JBcm2835Library.bcm2835_delay(200);
    }

    private void EPD_WaitUntilIdle() {
        System.out.println("Display Busy\n");
        while (JBcm2835Library.bcm2835_gpio_lev(EPD_BUSY_PIN.getValue()) == 0) {
            JBcm2835Library.bcm2835_delay(200);
        }
        System.out.println("Display released\n");
    }
}
