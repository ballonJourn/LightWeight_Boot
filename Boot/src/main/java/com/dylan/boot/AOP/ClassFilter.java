package com.dylan.boot.AOP;

import java.lang.reflect.Method;

public interface ClassFilter {
    Boolean matchers( Class beanClass);
}
