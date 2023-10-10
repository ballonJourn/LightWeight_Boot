package com.dylan.boot.AOP;

import java.lang.reflect.Method;

public interface MethodMatcher {

//    Boolean matches(Method method , Class beanClass);
//    Boolean matches( Class beanClass);

    Boolean matchers(Method method, Class<?> targetClass);

}
