package com.dylan.rpc.loadbalance;


import com.dylan.rpc.domain.Endpoint;
import com.dylan.rpc.exchange.RequestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 轮询负载均衡，支持权重
 * 借鉴 weibo motan 的基于权重的轮询负载算法：
 * https://github.com/weibocom/motan/blob/master/motan-core/src/main/java/com/weibo/api/motan/cluster/loadbalance/ConfigurableWeightLoadBalance.java
 *
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "roundrobin";

    //创建一个selectors成员变量，它是一个ConcurrentMap，用于存储每个RPC服务的轮询选择器（RoundRobinSelector对象）。
    //这个选择器会针对每个RPC服务维护一个独立的轮询状态。
    private final ConcurrentMap<String, RoundRobinSelector> selectors = new ConcurrentHashMap<>();

    //选择一个节点来发送请求,接受一个endpoints列表，其中包含了可用的服务节点，以及一个请求对象request，用于获取RPC服务的名称。
    @Override
    protected Endpoint doSelect(List<Endpoint> endpoints, RequestData request) {
        //获取endpoints列表的哈希码，用于标识不同的节点列表状态。
        int endpointsHashCode = System.identityHashCode(endpoints);

        //获取RPC服务的名称，以便识别不同的RPC服务。
        String key = request.getRpcServiceName();

        //从selectors中获取与RPC服务名称对应的轮询选择器。
        RoundRobinSelector selector = selectors.get(key);

        //如果没有找到对应的轮询选择器，或者选择器的哈希码不匹配当前endpoints列表的哈希码
        if (selector == null || selector.identityHashCode != endpointsHashCode) {

            //就需要创建一个新的轮询选择器
            selectors.put(key, new RoundRobinSelector(endpoints, endpointsHashCode));
            selector = selectors.get(key);
        }

        //select方法用于选择一个节点，它通过原子操作在cursor上加 1 ，然后取余计算索引，从randomKeyList中选择一个节点返回。

        //这样，每次调用select方法都会返回下一个节点，实现了轮询的效果。
        return selector.select();
    }


    private static final class RoundRobinSelector {

        private final int identityHashCode;

        private final int randomKeySize;
        private final List<Endpoint> randomKeyList = new ArrayList<>();
        private final AtomicInteger cursor = new AtomicInteger(0);

        //在构造函数中，首先计算了endpoints列表中所有节点权重的最大公约数（GCD）
        //然后，根据节点的权重，将节点按权重加入到randomKeyList中，并打乱列表的顺序以实现随机性。
        RoundRobinSelector(List<Endpoint> endpoints, int identityHashCode) {
            this.identityHashCode = identityHashCode;
            List<Integer> weightsArr = endpoints.stream()
                    .map(Endpoint::getWeight)
                    .collect(Collectors.toList());
            // findGcd() 求出最大公约数，若不为1，对权重做除法
            int weightGcd = findGcd(weightsArr.toArray(new Integer[]{}));

            //如果不为1，将节点的权重除以最大公约数
            if (weightGcd != 1) {
                for (Endpoint endpoint : endpoints) {
                    endpoint.setWeight(endpoint.getWeight() / weightGcd);
                }
            }
            //按照权重次数添加节点次数，比如10，5，那么权重10的添加两次，5添加一次，以确保负载均衡算法按照权重选择节点。
            for (Endpoint endpoint : endpoints) {
                for (int i = 0; i < endpoint.getWeight(); i++) {
                    randomKeyList.add(endpoint);
                }
            }
            Collections.shuffle(randomKeyList);
            randomKeySize = randomKeyList.size();
        }

        //早期的分布式系统和计算机网络，特别是在服务器集群和负载均衡设备的发展中
        //为了实现高可用性和高性能，需要一种方式来均匀地分发请求到多个服务器节点上。
        // 轮询算法应运而生，它简单而有效，能够实现均匀分配请求的目标。

        /**
         * cursor 是一个 AtomicInteger，它用来记录当前选择的节点索引。
         * 在多线程环境下，如果多个线程同时访问和修改一个共享的整数变量，可能会出现竞态条件（Race Condition）的问题，导致数据不一致性和错误的结果。
         * AtomicInteger是Java标准库中的一个原子整数类，它提供了一组原子操作，可以确保在多线程环境下对整数的操作是线程安全的。
         *
         * cursor.getAndAdd(1) 原子地增加 cursor 的值，以实现循环递增。这保证了每次调用 select() 方法都会选择下一个节点。
         *
         * Math.abs(...) 用于获取索引的绝对值，以确保索引为正数。
         *
         * % randomKeySize 取余操作，将索引限定在 randomKeyList 的有效范围内，以避免索引溢出。
         * @return
         */
        public Endpoint select() {
            return randomKeyList.get(Math.abs(cursor.getAndAdd(1)) % randomKeySize);
        }

        /**
         * 求最大公约数
         */
        private int findGcd(int n, int m) {
            return (n == 0 || m == 0) ? n + m : findGcd(m, n % m);
        }

        /**
         * 求最大公约数
         */
        private int findGcd(Integer[] arr) {
            if (arr.length == 1) return arr[0];
            int i = 0;
            for (; i < arr.length - 1; i++) {
                arr[i + 1] = findGcd(arr[i], arr[i + 1]);
            }
            return findGcd(arr[i], arr[i - 1]);
        }
    }

}