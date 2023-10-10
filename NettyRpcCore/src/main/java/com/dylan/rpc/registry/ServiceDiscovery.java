package com.dylan.rpc.registry;

import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.Request;

/**
 * 根据服务名称获取服务访问地址
 *
 * @return 服务地址
 */
public interface ServiceDiscovery {
    Endpoint lookup(Request request);
}
