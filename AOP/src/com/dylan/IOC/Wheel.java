package com.dylan.IOC;

public class Wheel {
    //<property name="brand" value="Michelin"></property>
    //        <property name="specification" value="265/60 R18"></property>
    private String brand;
    private String specification;
    public String getBrand(){
        return brand;
    }
    public String getSpecification(){
        return specification;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public void setSpecification(String specification){
        this.specification = specification;
    }
}
