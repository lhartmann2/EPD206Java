package com.lhp.paint;

public class PaintTime {
    private int year;
    private byte month, day, hour, min, sec;

    public PaintTime() {}

    public PaintTime(int year, byte month, byte day, byte hour, byte min, byte sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMin() {
        return min;
    }

    public void setMin(byte min) {
        this.min = min;
    }

    public byte getSec() {
        return sec;
    }

    public void setSec(byte sec) {
        this.sec = sec;
    }
}
