package com.dylan.boot.IOC;

public interface BeanDefinitionReader {
    /**
     * 'loadBeanDefinitions(String)' in 'com.dylan.boot.IOC.XML.XmlBeanDefinitionReader'
     * clashes with 'loadBeanDefinitions(String)' in 'com.dylan.boot.IOC.BeanDefinitionReader';
     * overridden method does not throw 'java.lang.Exception'
     * @param location
     */
    void loadBeanDefinitions(String location) throws  Exception; //接口也需要throws Exception
}
