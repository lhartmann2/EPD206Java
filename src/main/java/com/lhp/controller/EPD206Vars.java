package com.lhp.controller;

public enum EPD206Vars {
    //Commands
    PANEL_SETTING(0x00),
    POWER_SETTING(0x01),
    POWER_OFF(0x02),
    POWER_OFF_SEQUENCE_SETTING(0x03),
    POWER_ON(0x04),
    POWER_ON_MEASURE(0x05),
    BOOSTER_SOFT_START(0x06),
    DEEP_SLEEP(0x07),
    DATA_START_TRANSMISSION_1(0x10),
    DATA_STOP(0x11),
    DISPLAY_REFRESH(0x12),
    DATA_START_TRANSMISSION_2(0x13),
    PARTIAL_DATA_START_TRANSMISSION_1(0x14),
    PARTIAL_DATA_START_TRANSMISSION_2(0X15),
    PARTIAL_DISPLAY_REFRESH(0X16),
    LUT_FOR_VCOM(0X20),
    LUT_WHITE_TO_WHITE(0X21),
    LUT_BLACK_TO_WHITE(0X22),
    LUT_WHITE_TO_BLACK(0X23),
    LUT_BLACK_TO_BLACK(0X24),
    PLL_CONTROL(0X30),
    TEMPERATURE_SENSOR_COMMAND(0X40),
    TEMPERATURE_SENSOR_CALIBRATION(0X41),
    TEMPERATURE_SENSOR_WRITE(0X42),
    TEMPERATURE_SENSOR_READ(0X43),
    VCOM_AND_DATA_INTERVAL_SETTING(0X50),
    LOW_POWER_DETECTION(0X51),
    TCON_SETTING(0X60),
    TCON_RESOLUTION(0X61),
    SOURCE_AND_GATE_START_SETTING(0X62),
    GET_STATUS(0X71),
    AUTO_MEASURE_VCOM(0X80),
    VCOM_VALUE(0X81),
    VCM_DC_SETTING_REGISTER(0X82),
    PROGRAM_MODE(0XA0),
    ACTIVE_PROGRAM(0XA1),
    READ_OTP_DATA(0XA2);


    private byte value;

    private EPD206Vars(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }

}
