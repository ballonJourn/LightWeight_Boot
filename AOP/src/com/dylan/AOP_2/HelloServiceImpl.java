package com.dylan.AOP_2;

/**
 * AOP 就是在执行sayHello的时候想添加额外操作，又不能影响原对象，所以使用代理，以及用Point cut, Advice定义了额外操作的主谓宾等
 *
 */

public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHelloWorld() {
        System.out.println("Hello World");
    }
}
