package com.dylan.rpc.loadbalance;

import com.dylan.extension.SPI;
import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.RequestData;

import java.util.List;

@SPI
public interface LoadBalance {
    Endpoint select(List<Endpoint> endpoints, RequestData request);
}
