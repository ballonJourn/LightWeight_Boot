package com.dylan.rpc.remoting.transport;

import com.dylan.extension.ExtensionLoader;
import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.Request;
import com.dylan.rpc.exchange.Response;
import com.dylan.rpc.properties.RpcProperties;

//引用常数
import com.dylan.constants.CommonConstants;
import com.dylan.rpc.registry.ServiceDiscovery;

import java.net.InetSocketAddress;


//Class 'AbstractClient' must either be declared abstract or implement abstract method 'send(Request)' in 'RemotingClient'

//这个错误信息的意思是，你的AbstractClient类没有实现RemotingClient接口中定义的抽象方法send(Request)。

// 因为AbstractClient类实现了RemotingClient接口，所以它必须提供所有接口中声明的抽象方法的具体实现，或者将自己也声明为抽象类。
//
// 如果你将AbstractClient类声明为抽象类，那么你就不能创建它的实例对象，而只能通过它的子类来实例化。

public abstract class AbstractClient implements RemotingClient{

    private final ServiceDiscovery serviceDiscovery;

    //在客户端实例创建时被调用。在构造方法中，发生异常时会捕获并处理异常，关闭客户端，然后抛出RemotingException异常。
    public AbstractClient(){
        //从配置中获取一个名为SUBSCRIBE_PROTOCOL_KEY的参数值，然后将其存储在一个名为protocol的字符串变量中。
        // 这个参数的值表示服务发现所使用的协议类型，它指定了具体的服务发现实现方式。
        String SUBSCRIBE_PROTOCOL_KEY = CommonConstants.SUBSCRIBE_PROTOCOL_KEY;

        String protocol = RpcProperties.getParameter(SUBSCRIBE_PROTOCOL_KEY);

        //错误信息 "Unknown class: 'serviceDiscovery'" 表示在以下这行代码中出现了问题：
        //问题在于，你使用了小写的 serviceDiscovery 作为类名，但应该使用大写的 ServiceDiscovery，因为你定义的接口名是 ServiceDiscovery。Java 对类名大小写敏感，所以你需要使用正确的类名来解决这个问题。
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension(protocol);
        // 获取一个名为serviceDiscovery的成员变量，该成员变量是一个用于服务发现的对象，用于查找服务的网络地址。
    }




    //这段代码的作用是处理客户端发送请求的过程。根据传入的请求对象找到目标服务器的网络地址，然后使用这个地址将请求发送到目标服务器，并返回响应。
    //具体的请求发送和响应处理逻辑将依赖于doSend方法的实现，这个方法通常由具体的子类来实现，以适应不同的通信协议和远程服务器类型。
    public Response send(Request request){
        //根据传入的request对象查找服务的网络地址（通常是主机名和端口号），将结果存储在endpoint变量中。
        //这个操作是为了确定请求将被发送到哪个远程服务器。
        Endpoint endpoint = serviceDiscovery.lookup(request);


        //使用上一步获取的endpoint对象中的主机名和端口号，以及传入的request对象，调用doSend方法。
        //doSend方法是一个抽象方法，需要在具体的子类中实现。它的作用是将请求发送给远程服务器，并等待响应。
        // 这里通过创建InetSocketAddress对象来表示目标地址，然后调用doSend方法来执行实际的请求发送操作。
        return doSend(new InetSocketAddress(endpoint.getHost(), endpoint.getPort()), request);

    }

    public abstract Response doSend(InetSocketAddress inetSocketAddress,Request request);

    @Override
    public void close() {
        doClose();
    }

    protected abstract void doOpen();

    protected abstract void doClose();

}
