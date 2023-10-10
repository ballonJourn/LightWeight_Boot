package com.dylan.boot.Model;

public class Car {
    private String name;

    private int width;
    private int money;
    private Wheel wheel;
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getWidth(){
        return width;
    }
    public void setWidth(int width){
        this.width = width;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Wheel getWheel(){
        return wheel;
    }
    public void setWheel(Wheel wheel){
        this.wheel = wheel;
    }
}
