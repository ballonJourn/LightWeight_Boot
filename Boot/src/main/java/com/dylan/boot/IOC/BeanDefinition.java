package com.dylan.boot.IOC;

import java.util.Map;

public class BeanDefinition {



    /**
     * 这个涉及盲区， 怎么修改名字？
     * 将读到的配置属性信息封装成 BeanDefinition 对象
     * 将封装好的 BeanDefinition 对象注册到 BeanDefinition 容器中
     * 也就是将原来的：
     * //--------------加载beanclass--------------------------------------------------------------------------------
     *                 Class beanClass = null;
     *                 //这里有个异常Unhandled exception: java.lang.ClassNotFoundException
     *                 //使用try
     *                 try{beanClass = Class.forName(className);}catch (Exception exception){}
     *
     * //--------------创建bean------------------------------------------------------------------------
     *                 Object bean = beanClass.newInstance();
     *
     * 放到这个类里来处理
     *
     * @param className
     */
    private Object bean;
    private Class beanClass;
    private String beanClassName;
    private PropertyValues propertyValues = new PropertyValues();

    public Object getBean() {
        return this.bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setBeanClassName(String className){
        this.beanClassName = className;
        /**
         * 一个知识点：
         * 使用 throws 声明异常是一种将异常传播给方法的调用者的方式，由调用者来处理异常。这通常用于受检查异常。
         *
         * 使用 try-catch 捕获和处理异常是一种在方法内部处理异常的方式，使得异常不会传播到方法的调用者。
         * 这通常用于受检查异常和未受检查异常。
         *
         * throws 主要用于告知调用者可能会抛出哪些异常，而 try-catch 用于实际处理异常的逻辑。
         */
//        this.beanClass = Class.forName(className);
        try{
            this.beanClass = Class.forName(className);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public PropertyValues getPropertyValues(){

        return this.propertyValues;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

}
