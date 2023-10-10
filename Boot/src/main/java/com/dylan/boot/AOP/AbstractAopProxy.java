package com.dylan.boot.AOP;

public abstract class AbstractAopProxy implements AopProxy {

    protected AdvisedSupport advised;

    public AbstractAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }

    //加abstract之前 不加这段函数爆红  加了之后不红了 为什么 ？ 同样是抽象类，abstract也不需要实现重写内容
//    @Override
//    public Object getProxy() {
//        return null;
//    }

}
