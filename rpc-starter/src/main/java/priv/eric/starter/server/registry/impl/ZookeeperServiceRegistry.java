package priv.eric.starter.server.registry.impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.server.registry.ServiceRegistry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static priv.eric.starter.common.Constant.ERIC_SERVICE_ROOT;

@Slf4j
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private final ZkClient zkClient;

    public ZookeeperServiceRegistry(String address) {
        zkClient = new ZkClient(address);
    }

    @Override
    public void register(ServiceInstanceInfo serviceInstanceInfo) throws Exception {
        log.info("registry service info: {}", serviceInstanceInfo);

        // 注册服务, 永久节点
        String serviceNodePath = ERIC_SERVICE_ROOT + serviceInstanceInfo.getServiceName();
        if (!zkClient.exists(serviceNodePath)) {
            zkClient.createPersistent(serviceNodePath);
        }

        // 注册节点信息, 临时节点
        String uriPath = URLEncoder.encode(JSON.toJSONString(serviceInstanceInfo), StandardCharsets.UTF_8.name());
        if (zkClient.exists(uriPath)) {
            zkClient.delete(uriPath);
        }
        zkClient.createEphemeral(uriPath);
    }

}
