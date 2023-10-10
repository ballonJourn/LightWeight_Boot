package com.dylan.IOC;

public class SimpleIocTest {
    public static void main(String[] args) throws Exception {
        String location = SimpleIoc.class.getClassLoader().getResource("com/dylan/IOC/ioc.xml").getFile();
        SimpleIoc bf = new SimpleIoc(location);
        Car car = (Car) bf.getBean("car");
        System.out.println(car + " | " + car.getName());
        Wheel wheel = (Wheel) bf.getBean("wheel");
        System.out.println(wheel + " | " + wheel.getBrand());
    }
}
