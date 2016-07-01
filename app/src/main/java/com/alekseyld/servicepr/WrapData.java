package com.alekseyld.servicepr;

/**
 * Created by Alekseyld on 30.06.2016.
 */
public class WrapData {
    private String name;
    private int number;

    WrapData(String name, int number){
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
