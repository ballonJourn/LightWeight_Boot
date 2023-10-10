package com.dylan.AOP_2;

import javax.swing.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 在simpleAop里添加代理以及Advice(这里选择beforeAdvice)
 *
 *
 */
public class SimpleAop {
//    public static Object newProxyInstance(ClassLoader loader,
//                                          Class<?>[] interfaces == bean,
//                                          InvocationHandler h == Advice)
    public static Object getProxy(Object bean, Advice advice){
//        1）原句是：Main.class.getClassLoader()
//        2）让bean构造成interface
        Object proxyInstance = Proxy.newProxyInstance(SimpleAop.class.getClassLoader(), bean.getClass().getInterfaces(), advice);
        return proxyInstance;
    }
}
