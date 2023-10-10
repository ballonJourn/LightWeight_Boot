package com.dylan.AOP;

import java.lang.reflect.Method;

public class BeforeAdvice implements Advice {
    private Object bean;
    private MethodInvocation methodInvocation;
    public BeforeAdvice(Object bean, MethodInvocation methodInvocation){
        this.bean = bean;
        this.methodInvocation = methodInvocation;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

//--------------在Advice执行之前调用Point Cut规则------------------------
        //这里不是static，为什么这样用 ?sb 这里是methodInvocation.secretinvoke();不是MethodInvocation.secretinvoke();
        methodInvocation.secretinvoke();
        Object invoke = method.invoke(bean, args);
        methodInvocation.secretinvoke();
        return invoke;
    }

}
