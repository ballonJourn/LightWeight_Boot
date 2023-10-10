package com.dylan.boot.AOP;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


import org.aspectj.weaver.tools.ShadowMatch;
import org.springframework.aop.Pointcut;

public class AspectJExpressionPointcut implements MethodMatcher, PointCut, ClassFilter{
    private PointcutParser pointcutParser;

    private String expression;

    private PointcutExpression pointcutExpression;


    private static final Set<PointcutPrimitive> i_Dont_Know = new HashSet<PointcutPrimitive>();

    static {
        i_Dont_Know.add(PointcutPrimitive.EXECUTION);
        i_Dont_Know.add(PointcutPrimitive.ARGS);
        i_Dont_Know.add(PointcutPrimitive.REFERENCE);
        i_Dont_Know.add(PointcutPrimitive.THIS);
        i_Dont_Know.add(PointcutPrimitive.TARGET);
        i_Dont_Know.add(PointcutPrimitive.WITHIN);
        i_Dont_Know.add(PointcutPrimitive.AT_ANNOTATION);
        i_Dont_Know.add(PointcutPrimitive.AT_WITHIN);
        i_Dont_Know.add(PointcutPrimitive.AT_ARGS);
        i_Dont_Know.add(PointcutPrimitive.AT_TARGET);
    }

    public AspectJExpressionPointcut(){
        this(i_Dont_Know);
    }

    //构造函数相当于使用了这个init
    //private PointcutExpression buildPointcutExpression(@Nullable ClassLoader classLoader) {
    //---------------------1.init-------------------------------------------
    //		PointcutParser parser = initializePointcutParser(classLoader);
    //		PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
    //		for (int i = 0; i < pointcutParameters.length; i++) {
    //			pointcutParameters[i] = parser.createPointcutParameter(
    //					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
    //		}
    //---------------------2.parsePointcutExpression-------------------------------------------
    //		return parser.parsePointcutExpression(replaceBooleanOperators(resolveExpression()),
    //				this.pointcutDeclarationScope, pointcutParameters);
    //	}

    //	private PointcutParser initializePointcutParser(@Nullable ClassLoader classLoader) {
    //		PointcutParser parser = PointcutParser
    //				.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
    //						SUPPORTED_PRIMITIVES, classLoader);
    //		return parser;
    //	}

    public AspectJExpressionPointcut(Set<PointcutPrimitive> supportedPrimitives_i_Dont_Know){
        pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(supportedPrimitives_i_Dont_Know);


    }


    @Override
    public AspectJExpressionPointcut getClassFilter() {
//        obtainPointcutExpression();

        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
//        obtainPointcutExpression();
        return this;
    }
//    private PointcutExpression obtainPointcutExpression() {
//        if (getExpression() == null) {
//            throw new IllegalStateException("Must set property 'expression' before attempting to match");
//        }
//        if (this.pointcutExpression == null) {
//            this.pointcutClassLoader = determinePointcutClassLoader();
//            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
//        }
//        return this.pointcutExpression;
//    }
//    private PointcutExpression buildPointcutExpression(@Nullable ClassLoader classLoader) {
//        PointcutParser parser = initializePointcutParser(classLoader);
//        PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
//        for (int i = 0; i < pointcutParameters.length; i++) {
//            pointcutParameters[i] = parser.createPointcutParameter(
//                    this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
//        }
//        return parser.parsePointcutExpression(replaceBooleanOperators(resolveExpression()),
//                this.pointcutDeclarationScope, pointcutParameters);
//    }
//
//        private String resolveExpression() {
//            String expression = getExpression();
//            Assert.state(expression != null, "No expression set");
//            return expression;
//        }
//
    /**
     * 使用 AspectJ Expression 匹配类
     * 成功匹配返回 true，否则返回 false
     */
    //@Override
    //	public boolean matches(Class<?> targetClass) {
    //		PointcutExpression pointcutExpression = obtainPointcutExpression();
    //		try {
    //			try {
    //				return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    //			}
    @Override
    public Boolean matchers(Class targetClass) {
        //1) 首先通过checkReadyToMatche构造pointcutExpression
        checkReadyToMatche();
        // 这里spring也是调用的import org.aspectj.weaver.tools.PointcutExpression; 而不是自己实现

        // 调用 couldMatchJoinPointsInType(targetClass) 方法，根据切点表达式检查传递的类是否符合条件。
        // 比如这里传递的类是<bean id="helloService" class="com.dylan.boot.Model.HelloServiceImpl"/>：
        // public class HelloServiceImpl implements HelloService{
        //    @Override
        //    public void sayHelloWorld(){
        //        System.out.println("Hello World");
        //    }
        //}
        //
        // expression是<property name="expression" value="execution(* com.dylan.boot.Model.*.sayHelloWorld(..))"/>
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    //checkReadyToMatche() 相当于 obtainPointcutExpression
    // private PointcutExpression obtainPointcutExpression() {
    //		if (getExpression() == null) {
    //			throw new IllegalStateException("Must set property 'expression' before attempting to match");
    //		}
    //		if (this.pointcutExpression == null) {
    //			this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
    //		}
    //		return this.pointcutExpression;
    //	}

    //构造函数相当于使用了这个init
    //private PointcutExpression buildPointcutExpression(@Nullable ClassLoader classLoader) {
    //---------------------1.init-------------------------------------------
    //		PointcutParser parser = initializePointcutParser(classLoader);
    //		PointcutParameter[] pointcutParameters = new PointcutParameter[this.pointcutParameterNames.length];
    //		for (int i = 0; i < pointcutParameters.length; i++) {
    //			pointcutParameters[i] = parser.createPointcutParameter(
    //					this.pointcutParameterNames[i], this.pointcutParameterTypes[i]);
    //		}
    //---------------------2.parsePointcutExpression-------------------------------------------
    //		return parser.parsePointcutExpression(replaceBooleanOperators(resolveExpression()),
    //				this.pointcutDeclarationScope, pointcutParameters);
    //	}

    //	private PointcutParser initializePointcutParser(@Nullable ClassLoader classLoader) {
    //		PointcutParser parser = PointcutParser
    //				.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
    //						SUPPORTED_PRIMITIVES, classLoader);
    //		return parser;
    //	}

    private void checkReadyToMatche() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (pointcutExpression == null) {
            pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        }
    }
    /**
     * 使用 AspectJ Expression 匹配方法
     * 成功匹配返回 true，否则返回 false
     */
    @Override
    public Boolean matchers(Method method, Class targetClass) {
        checkReadyToMatche();
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);

        if (shadowMatch.alwaysMatches()) {
            return true;
        } else if (shadowMatch.neverMatches()) {
            return false;
        }

        return false;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;

    }

}
