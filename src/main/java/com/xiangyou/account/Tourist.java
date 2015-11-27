package com.xiangyou.account;

public class Tourist {
    public static final int NO_TYPE_IDCARD = 1;
    public static final int NO_TYPE_PASSPORT = 2;

    private String name;
    private String number;
    // Number type
    private int noType;

    public static Tourist newIDCardTourist(String name, String number) {
        return new Tourist(name, number, NO_TYPE_IDCARD);
    }

    public static Tourist newPassportTourist(String name, String number) {
        return new Tourist(name, number, NO_TYPE_PASSPORT);
    }

    public Tourist(String name, String number, int type) {
        this.name = name;
        this.number = number;
        this.noType = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return noType;
    }

    public void setType(int type) {
        this.noType = type;
    }

}