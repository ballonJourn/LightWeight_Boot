package com.dylan.boot.AOP;

import org.aopalliance.intercept.MethodInterceptor;
//import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {
    public JdkDynamicAopProxy(AdvisedSupport advised){
        //extends AbstractAopProxy 就可以使用super()
        super(advised);
    }
    /**
     * 为目标 bean 生成代理对象
     * 返回 bean 的代理对象
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodMatcher methodMatcher = advised.getMethodMatcher();

        // 使用方法匹配器 methodMatcher 测试 bean 中原始方法 method 是否符合匹配规则
        if (methodMatcher != null && methodMatcher.matchers(method, advised.getTargetSource().getTargetClass())) {

            // 获取 Advice。MethodInterceptor 的父接口继承了 Advice
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();

            // 将 bean 的原始 method 封装成 MethodInvocation 实现类对象，
            // 将生成的对象传给 Adivce 实现类对象，执行通知逻辑
            return methodInterceptor.invoke(
                    new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        } else {
            // 当前 method 不符合匹配规则，直接调用 bean 中的原始 method
            return method.invoke(advised.getTargetSource().getTarget(), args);
        }
    }

}