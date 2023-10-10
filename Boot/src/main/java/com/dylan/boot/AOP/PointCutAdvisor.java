package com.dylan.boot.AOP;

public interface PointCutAdvisor extends Advisor {
    PointCut getPointCut();
}
