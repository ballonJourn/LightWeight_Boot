package com.dylan.AOP;

import java.lang.reflect.Proxy;

/**
 * Object-oritend 面向对象
 * Spring AOP(Aspect Oritend Programing) 中的一些概念：Advice, joint point, point cut,Aspect（= Advice point cut）
 * 1) AOP实现Aspect 是通过 为目标对象生成代理 来完成的(基于 JDK 动态代理实现)，只需3步即可完成：
 *
 *  1.1
 *      定义一个包含point cut逻辑的对象，这里假设叫 logMethodInvocation
 *  1.2
 *      定义一个 Advice 对象（实现了 InvocationHandler 接口），并将上面的 logMethodInvocation 和目标对象传入
 *  1.3
 *      将上面的 Adivce 对象和目标对象传给 JDK 动态代理方法，为目标对象生成代理
 *
 *      注意，此时point cut 已经和 Advice结合起来成为一个Aspect了
 *
 *===============================================================================================================================
 * 这里解释一下代理，代理（Proxy）是指一个对象，它充当了其他对象的替身，用于控制对原始对象的访问。代理对象可以拦截对原始对象的方法调用，并在调用前后执行额外的逻辑，通常用于实现切面（Aspect）的行为。
 *
 * 让我们通过一个通俗的例子来解释代理：
 *
 *      假设你是一个秘书，你的老板经常接到来电，而你的任务是接听这些来电并过滤掉一些不重要的电话。但你的老板不想被打扰，所以他雇佣了你作为代理，来处理来电。
 *
 *      原始对象（老板）： 老板非常繁忙，不想亲自接听所有来电。
 *
 *      代理对象（秘书）： 你作为秘书充当了老板的代理。当来电时，电话首先被转接给你。
 *
 *      代理行为（过滤电话）： 作为代理，你不仅仅是简单地将来电传递给老板。你会先询问来电者的目的，如果是一些不紧急或不相关的事情，你可能会告诉来电者老板暂时不方便接听，并要求他们在稍后再打电话。如果来电是关于重要的业务或急需解决的问题，你会将电话转给老板。
 *
 * 在这个例子中，你作为秘书充当了老板的代理，处理所有来电(joint point)并添加了额外的行为:过滤掉一些不重要的电话(point cut advice)
 *
 * 代理对象在AOP中的作用类似，它可以拦截方法调用，并在方法执行前后添加一些额外的操作，例如日志记录
 *
 * ============================================================================================================================================================
 *
 * 2) 上面步骤比较简单，不过在实现过程中，要引入一些辅助接口才能实现。接下来介绍一下简单 AOP 的代码结构：
 *
 * MethodInvocation 接口——此接口的实现类包含了Point Cut逻辑，如 logMethodInvocation，这里没有
 * Advice 接口——继承了 InvocationHandler 接口
 * BeforeAdvice 类——实现了 Advice 接口，是一个前置通知(使用MethodInvocation.invoke()传递Point Cut逻辑,组成Aspect)
 * SimpleAOP 类——生成代理类
 * SimpleAOPTest——SimpleAOP 的测试类
 * HelloService 接口——目标对象接口
 * HelloServiceImpl——目标对象实现
 *
 *
 */
public class SimpleAop {
    public static Object getProxy(Object bean, Advice advice){
        return Proxy.newProxyInstance(SimpleAop.class.getClassLoader(), bean.getClass().getInterfaces(), advice);

    }
}
