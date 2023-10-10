package com.dylan.rpc.proxy;

import com.dylan.rpc.exchange.Request;
import com.dylan.rpc.exchange.RequestData;
import com.dylan.rpc.exchange.Response;
import com.dylan.rpc.remoting.transport.RemotingClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端代理(Aspect模式 todo)
 *
 * @Author dylan
 *
 */
public class RpcClientProxy implements Proxy {
    private final RemotingClient client;

    public RpcClientProxy(RemotingClient client){
        this.client = client;
    }

    //简单用一下代理，之后会结合bean
    public Object getProxy(Class<?> clazz) {
        return java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new DefaultInvocationHandler());
    }
    private class DefaultInvocationHandler implements InvocationHandler {

        //函数调用时拦截
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 构建请求体
            RequestData requestData = new RequestData();
            requestData.setInterfaceName(method.getDeclaringClass().getName());
            requestData.setMethodName(method.getName());
            requestData.setParamTypes(method.getParameterTypes());
            requestData.setParameters(args);

            Request request = new Request();
            request.setRequestData(requestData);
            // 发送请求
            Response response = client.send(request);

            checkValid(response);
            return response.getResult();
        }

        private void checkValid(Response response) {
            if (response == null) {
//                throw new RpcException("Service invocation fail.");
            }
            if (response.getStatus() <= 0 || response.getStatus() != Response.OK) {
//                throw new RpcException(response.getErrorMsg());
            }
        }
    }

}
