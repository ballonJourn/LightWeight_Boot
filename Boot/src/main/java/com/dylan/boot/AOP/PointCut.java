package com.dylan.boot.AOP;

public interface PointCut {
    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
