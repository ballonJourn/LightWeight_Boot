package com.dylan.boot.IOC.Factory;

public interface BeanFactory {
    Object getBean(String beanId)throws Exception;
}
