package com.dylan.boot.AOP;

import com.dylan.boot.IOC.BeanDefinition;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor extends BeanDefinition {
    /**
     *    new AspectJExpressionPointcut();这里就算是给point使用构造函数了，完整构造过程如下
     *     static {
     *         i_Dont_Know.add(PointcutPrimitive.EXECUTION);
     *         i_Dont_Know.add(PointcutPrimitive.ARGS);
     *         i_Dont_Know.add(PointcutPrimitive.REFERENCE);
     *         i_Dont_Know.add(PointcutPrimitive.THIS);
     *         i_Dont_Know.add(PointcutPrimitive.TARGET);
     *         i_Dont_Know.add(PointcutPrimitive.WITHIN);
     *         i_Dont_Know.add(PointcutPrimitive.AT_ANNOTATION);
     *         i_Dont_Know.add(PointcutPrimitive.AT_WITHIN);
     *         i_Dont_Know.add(PointcutPrimitive.AT_ARGS);
     *         i_Dont_Know.add(PointcutPrimitive.AT_TARGET);
     *     }
     *
     *     public AspectJExpressionPointcut(){
     *         this(i_Dont_Know);
     *     }
     */

    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    private Advice advice;

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public AspectJExpressionPointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }

    public void setExpression(String expression) {
//Exception in thread "main" java.lang.NoSuchFieldException: expression

// 也就是说expression是已经设置好了，666，怎么做到的，在这里声明public void setExpression(String expression) ，ioc.xml就不报错了

// 只是传递expression，parse还是要自己调用
        pointcut.setExpression(expression);
    }
}
