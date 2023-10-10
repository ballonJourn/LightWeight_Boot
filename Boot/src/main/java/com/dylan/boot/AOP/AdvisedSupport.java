package com.dylan.boot.AOP;

import org.aopalliance.intercept.MethodInterceptor;

public class AdvisedSupport{
    private TargetSource targetSource;

    private MethodMatcher methodMatcher;

    private MethodInterceptor methodInterceptor;

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

}