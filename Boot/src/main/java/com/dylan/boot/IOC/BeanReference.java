package com.dylan.boot.IOC;

public class BeanReference {
    private String name;
    private Object bean;
    public BeanReference(String name){
        //应该是找出BeanDefinition里面的ref吧？
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public String getName() {
        return name;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setName(String name) {
        this.name = name;
    }
}
