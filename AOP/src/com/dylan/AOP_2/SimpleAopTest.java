package com.dylan.AOP_2;

public class SimpleAopTest {
    public static void main(String[] args){
        //test还真不知道该怎么做
        /**
         * 1) 首先引入PointCut并重写逻辑(通过Me... = new Me...() 这种方式重写)
         *
         * 2) 引入老板helloService
         *
         * 3) 构建Aspect,getProxy以此生成Proxy秘书

         * 4) 在函数里面才能使用 proxy.sayHelloWorld();实例.实例方法();
         *
         * 5) main() 加上String[] args，才能运行
         */
        MethodInvocation log = new MethodInvocation(){
            @Override
            public void  SecretInvoke(){
                System.out.println("gan ni na ");
            }
        };
        fuckService f = new fuckService();
        HelloServiceImpl helloService = new HelloServiceImpl();


        //构造函数需要命名为和类名一样，这里是BeforeAdvice而不是getBeforeAdvice
        Advice beforeAdvice = new BeforeAdvice(helloService, log );

        Advice fuckAdvice = new BeforeAdvice(f, log);



        HelloService proxy = (HelloService) SimpleAop.getProxy(helloService, beforeAdvice);

        fuckService s = (fuckService) SimpleAop.getProxy(f,fuckAdvice);
        proxy.sayHelloWorld();
        //测试s.fuck
//        s.sayFuck();
        /**
         * s.sayFuck()
         * 结果：com.sun.proxy.$Proxy1 cannot be cast to com.dylan.AOP_2.fuckService
         *
         * 原因： JDK 动态代理（）只能代理实现了接口的对象:
         * newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
         *
         * 而 Cglib 动态代理则无此限制。所以在为没有实现接口的对象生成代理时，只能使用 Cglib。
         */
    }
}
