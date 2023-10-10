package com.dylan.AOP_2;

import java.lang.reflect.Method;

public class BeforeAdvice implements Advice{

    //首先引入Point Cut逻辑，我们选择定义对象来使用
    private MethodInvocation methodInvocation;
    //再引入老板，为老板添加秘书
    private Object bean;
//    Private field 'bean' is never assigned,得通过传参来传入bean，这里使用getBeforeAdvice
    public  BeforeAdvice(Object bean, MethodInvocation methodInvocation){
        this.bean = bean;
        this.methodInvocation = methodInvocation;
    }


//    public Object invoke(Object proxy, Method method, Object[] args)


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        methodInvocation.SecretInvoke();
//        return method.invoke(bean, args); 固定写法, 重新回到bean目标执行阶段
        return method.invoke(bean, args);
    }
}
