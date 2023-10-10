package com.dylan.rpc.remoting.transport.netty;

import com.dylan.constants.CommonConstants;
import com.dylan.rpc.exchange.Message;
import com.dylan.rpc.exchange.Request;
import com.dylan.rpc.exchange.Response;
import com.dylan.rpc.properties.RpcProperties;
import com.dylan.rpc.remoting.transport.AbstractClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NettyClient extends AbstractClient {
    private Bootstrap bootstrap;
    @Override
    public Response doSend(InetSocketAddress serviceAddress, Request request) {
        NettyChannel nettyChannel = NettyChannel.getOrNewChannel(serviceAddress, () -> {
            try {
                return doConnect(serviceAddress);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if (!nettyChannel.isActive()) {
//            throw new RemotingException("Failed to send message, cause: Channel inactive, channel: -> " + serviceAddress);
        }
        Message message = new Message();


        return null;
    }
    public Channel doConnect(InetSocketAddress address) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(address);
        boolean connected = channelFuture.awaitUninterruptibly(getConnectionTimeout(), MILLISECONDS);

        if (connected && channelFuture.isSuccess()) {
//            log.info("The client has connected [{}] success.", address.toString());
            return channelFuture.channel();
        } else if (channelFuture.cause() != null) {
            // Failed to connect to provider server by other reason
            Throwable cause = channelFuture.cause();
//            log.error("Failed to connect to provider server by other reason.", cause);
//            throw new RemotingException("Client failed to connect to server " + address + ", error message is:" + cause.getMessage(), cause);
        } else {
            // Client timeout
//            RemotingException remotingException = new RemotingException("client failed to connect to server " + address + " client  timeout " + 60000);
//            log.error("Client timeout.", remotingException);
//            throw remotingException;

        }
        return null;
    }
    private int getConnectionTimeout() {
        return RpcProperties.getParameter(CommonConstants.CLIENT_CONNECTION_TIMEOUT_KEY, CommonConstants.DEFAULT_CLIENT_CONNECTION_TIMEOUT);
    }
}
