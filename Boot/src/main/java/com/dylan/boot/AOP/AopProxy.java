package com.dylan.boot.AOP;

public interface AopProxy {
    Object getProxy();
    /**
     * ProxyFactory类同时继承了AdvisedSupport类并实现了AopProxy接口。这种组合通常用于创建一个类，
     *
     * 该类既包含了AOP代理的配置信息（通过继承AdvisedSupport类），又负责实际的代理创建（通过实现AopProxy接口）。
     *
     * 让我们分解一下为什么要同时继承和实现：
     *
     * 继承AdvisedSupport类： AdvisedSupport类是Spring AOP框架中用于存储AOP配置信息的基类。
     * 它包含了关于切面、通知、目标对象等AOP相关配置的信息。
     * 通过继承AdvisedSupport类，ProxyFactory类可以继承这些配置信息，以便在创建代理时使用。
     * 这使得代理对象能够按照配置来应用切面逻辑。
     *
     * 实现AopProxy接口： AopProxy接口定义了创建AOP代理对象的标准方法，即getProxy方法。
     * 通过实现AopProxy接口，ProxyFactory类承担了实际的代理创建责任。
     * 在getProxy方法中，它调用了createAopProxy方法来创建AOP代理。
     *
     * 总的来说，ProxyFactory类的继承AdvisedSupport是为了继承AOP配置信息，以便能够了解如何应用切面，
     * 而实现AopProxy接口则是为了提供代理对象的创建方法。这种组合模式使得代码结构清晰，并将AOP配置和代理创建的责任分开，
     * 以支持更好的可维护性和扩展性。这是一种常见的设计模式，用于将不同的功能组合在一起以实现特定的任务。
     */
}
