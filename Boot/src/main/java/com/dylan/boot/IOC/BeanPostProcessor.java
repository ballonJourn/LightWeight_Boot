package com.dylan.boot.IOC;

public interface BeanPostProcessor {

        /**
         * BeanPostProcessor接口是spring对外扩展的接口之一，就像我们要想渗透间谍到老板身边很难，但是通过公司的公开渠道
         * 去应聘秘书这一开放岗位来部署间谍就很简单
         *
         * 其主要用途是在bean实例化的时候，允许开发人员插手bean的实例化之前和之后的过程。
         *
         * 通过这个接口，我们才能把AOP和IOC联系起来，为bean添加point cut和Advice。所以BPP是作为桥梁的，重要
         * 而且命名也很清晰，将Processor Post 进 Bean里面  也就是切入了
         *
         * @param bean
         * @param beanName
         * @return
         * @throws Exception
         */
        /**
         * Spring 的AOP是通过接入BeanPostProcessor后置处理器开始的
         *
         * BeanPostProcessor的使用非常简单，只需要提供一个实现接口BeanPostProcessor的实现类，然后在Bean的配置文件中设置即可。
         *
         * initializeBean()方法为容器产生的Bean实例对象添加BeanPostProcessor后置处理器
         */

        /**
         * 在Spring中，BeanPostProcessor的实现子类非常多，分别完成不同的操作，如：AOP面向切面编程的注册通知适配器、Bean对象的数据校验、Bean继承属性、方法的合并等等，

         * 我们以最简单的AOP切面织入来简单了解其主要的功能。

         * 下面我们来分析其中一个创建AOP代理对象的子类AbstractAutoProxyCreator类。该类重写了postProcessAfterInitialization()方法。

         * @param bean
         * @param beanName
         * @return
         * @throws Exception
         */

        Object postProcessBeforeInitialization(Object bean, String beanName) ;

        Object postProcessAfterInitialization(Object bean, String beanName) throws Exception ;


}
