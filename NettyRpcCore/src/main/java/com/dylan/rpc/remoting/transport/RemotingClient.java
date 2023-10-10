package com.dylan.rpc.remoting.transport;

import com.dylan.rpc.exchange.Request;
import com.dylan.rpc.exchange.Response;

public interface RemotingClient {

    Response send(Request request);

    void close();
}
