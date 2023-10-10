package com.dylan.rpc.loadbalance;




import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.RequestData;
import com.dylan.util.CollectionUtils;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public Endpoint select(List<Endpoint> endpoints, RequestData request) {
        if (CollectionUtils.isEmpty(endpoints)) {
            return null;
        }
        if (endpoints.size() == 1) {
            return endpoints.get(0);
        }
        return doSelect(endpoints, request);
    }

    protected abstract Endpoint doSelect(List<Endpoint> endpoints, RequestData request);
}