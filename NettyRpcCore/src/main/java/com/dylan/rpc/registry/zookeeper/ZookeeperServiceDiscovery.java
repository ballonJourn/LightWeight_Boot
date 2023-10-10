package com.dylan.rpc.registry.zookeeper;

import com.dylan.rpc.registry.zookeeper.client.ZookeeperTemplate;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;


import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.Request;
import com.dylan.rpc.exchange.RequestData;
import com.dylan.rpc.loadbalance.LoadBalance;
import com.dylan.rpc.registry.ServiceDiscovery;

//引用常数
import com.dylan.constants.CommonConstants;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.dylan.util.CollectionUtils;
import com.dylan.util.URLUtil;


public class ZookeeperServiceDiscovery implements ServiceDiscovery {
    private static final Map<String, List<Endpoint>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private LoadBalance loadBalance;
    private ZookeeperTemplate zookeeperTemplate;


    //lookup根据传入的request对象查找服务的网络地址（通常是主机名和端口号），将结果存储在endpoint变量中。
    //这个操作是为了确定请求将被发送到哪个远程服务器。
    /**
     * 总体来说，这段代码的流程如下：
     *
     * lookup方法首先从Request中获取RPC服务的名称（rpcServiceName）。
     *
     * 然后，它调用listServiceEndpoints方法,用于从服务发现中获取网络地址列表。
     * 它通过ZooKeeper等服务注册中心监视服务地址的变化，并缓存地址列表以提高性能。
     * 这段代码的关键部分是将ZooKeeper节点数据转换为Endpoint对象，然后将其缓存起来以备将来使用。
     *
     * 如果找到了网络地址列表，它会使用负载均衡策略（loadBalance.select）从列表中选择一个Endpoint，该Endpoint将用于向服务发送请求。
     *
     * @param request
     * @return
     */
    @Override
    public Endpoint lookup(Request request) {
        RequestData requestData = (RequestData) request.getRequestData();
        // 从请求中获取RPC服务的名称
        String rpcServiceName = requestData.getRpcServiceName();

        // 调用listServiceEndpoints方法获取指定RPC服务的所有可用网络地址
        List<Endpoint> endpoints = listServiceEndpoints(rpcServiceName);

        if (CollectionUtils.isEmpty(endpoints)) {
            // 如果没有找到可用的网络地址，可以选择抛出异常，我们选择注释掉
//            throw new RpcException("Not found specified service");
        }
        // 使用负载均衡算法loadBalance从可用网络地址列表中选择一个Endpoint
        // 这个Endpoint将被用于向该服务发送请求
        return loadBalance.select(endpoints, requestData);
        // 最后返回一个可用服务器，类是endpoint
    }

    private List<Endpoint> listServiceEndpoints(String rpcServiceName) {
        // 构建一个锁，用于同步操作，防止并发问题
        String lock = ZKServiceDiscoveryLock.buildLock(rpcServiceName);
        synchronized (lock) {
            // 如果已经缓存了该服务的网络地址列表，直接返回缓存的列表
            if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
                return SERVICE_ADDRESS_MAP.get(rpcServiceName);
            }
            // 构建调用服务节点路径，类似：/rpc/com.dylan.service.DemoService/127.0.0.1:8080
            String serviceNodePath = URLUtil.fullURL(CommonConstants.ZK_ROOT, rpcServiceName);

            // 注册服务节点的监听器，用于监视服务地址列表的变化
            PathChildrenCache cache = registerNodeWatcher(serviceNodePath, rpcServiceName);

            // 获取当前服务地址列表的数据
            List<ChildData> childDataList = cache.getCurrentData();
            // 如果当前列表为空，返回一个空列表
            if (CollectionUtils.isEmpty(childDataList)) {
                return Collections.emptyList();
            }
            // 将服务地址列表的数据流式转换为Endpoint对象
            List<Endpoint> serviceEndpoints = childDataList.stream().map(child -> {
                String address = child.getPath().replace(serviceNodePath + "/", "");
                String weight = child.getData() == null ? "1" : new String(child.getData(), StandardCharsets.UTF_8);

                String[] split = address.split(":");
                Endpoint endpoint = new Endpoint(split[0], Integer.parseInt(split[1]));
                endpoint.setWeight(Integer.parseInt(weight));

                return endpoint;
            }).collect(Collectors.toList());

            // 缓存服务地址列表，以便下次直接使用
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceEndpoints);
            return serviceEndpoints;
        }
        //结束锁
    }
    private PathChildrenCache registerNodeWatcher(String serviceNodePath, String rpcServiceName) {
        try {
            return zookeeperTemplate.watchChildrenForNodePath(serviceNodePath, (client, event) -> {
                List<String> childNodes = client.getChildren().forPath(serviceNodePath);
                List<Endpoint> endpoints = new ArrayList<>();
                for (String child : childNodes) {
                    byte[] dataBytes = client.getData().forPath(child);
                    String weight = dataBytes == null ? "1" : new String(dataBytes, StandardCharsets.UTF_8);

                    String[] split = child.split(":");
                    Endpoint endpoint = new Endpoint(split[0], Integer.parseInt(split[1]));
                    endpoint.setWeight(Integer.parseInt(weight));
                    endpoints.add(endpoint);
                }
                SERVICE_ADDRESS_MAP.put(rpcServiceName, endpoints);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
