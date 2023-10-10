package com.dylan.AOP;

public class SimpleAopTest {
    public static void main(String[] args){
        // 1.创建一个MethodInvocation 对象，就是point cut ，为目标(秘书例子中是老板，嫌疑例子中是县令)的Advice(接电话或者审判) 添加额外操作(过滤不重要电话或只抓黑丝袜老百姓)
        // 这里添加的额外操作是记录Log
//        MethodInvocation logMethodInvocation = () -> System.out.println("log task start");
        MethodInvocation log = new MethodInvocation() {
            @Override
            public void secretinvoke() {
                System.out.println("log task start");
            }
        };

        HelloServiceImpl helloService = new HelloServiceImpl();

//--------------这里helloService是目标，logMethodInvocation是point cut------------------------

        // 2.创建一个添加了point cut的Advice给目标helloService
        Advice beforeAdvice = new BeforeAdvice(helloService, log);

        // 3.雇佣秘书(代理)来操作目标的接下来行为
        //  3.1 通过SimpleAop.getProxy() 得到一个与HelloService对象相同的动态代理对象，而且多了额外的操作(log task)
        //  3.2 当动态代理对象的方法被调用时,添加了point cut的Advice里的point cut的invoke()方法会被调用，可以重写invoke来进行额外操作
        //  3.3 因为InvocationHandler 接口只有一个方法，即 invoke 方法。
        //      当sayHello被调用时，继承了InvocationHandler的Advice会先通过invoke()接收秘书，并让秘书进行额外操作。
        //      这里通过传递log给Advice beforeAdvice,使得我们能在invoke方法返回之前重写秘书的过滤操作(这里是log)

        HelloService proxy = (HelloService) SimpleAop.getProxy(helloService, beforeAdvice);
        proxy.sayHelloWorld();
        String a = "com.jd.".replaceAll(".", "/") + "my";
        System.out.println(a);

        //囊括下来就是，雇佣代理，让sayHelloWorld 的时候，额外操作log task start
    }
}
