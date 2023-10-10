package com.dylan.boot.AOP;

public class ProxyFactory extends AdvisedSupport implements AopProxy{
    @Override
    public Object getProxy() {
        // 为什么要这样使用，直接返回new JdkDynamicAopProxy() 不行吗 ?
        return createAopProxy().getProxy();
        /**
         * 在你提供的代码中，你完全可以直接在getProxy方法中返回new JdkDynamicAopProxy()，这不会有问题。
         *
         * 然而，创建一个单独的createAopProxy方法可能是为了更好地支持可扩展性和可维护性，尤其是在未来可能需要更改AOP代理的方式或添加其他代理类型时。
         *
         * 以下是使用createAopProxy方法的一些潜在优点：
         *
         * 可扩展性： 如果以后需要添加其他类型的AOP代理（如CGLib代理），可以在createAopProxy方法中进行扩展而不必更改getProxy方法。这种设计使代码更加灵活，易于维护和扩展。
         *
         * 依赖注入： 如果createAopProxy方法需要接受一些参数或依赖注入其他组件，将这些操作放在createAopProxy方法中更有利于代码的解耦和管理。
         *
         * 可读性： 将代理对象的创建过程封装在一个单独的方法中可以提高代码的可读性和可维护性。这使得代码更容易理解，并且可以在方法名中表达出代理的创建过程。
         *
         * 虽然直接在getProxy方法中返回new JdkDynamicAopProxy()可能在这个特定情况下也可以工作，但使用一个单独的工厂方法可以提供更多的灵活性和可扩展性，以适应未来的变化和需求。
         *
         * 这种设计决策取决于项目的设计和扩展计划。
         */
    }

    private AopProxy createAopProxy(){
        return new JdkDynamicAopProxy(this);
    }
}
