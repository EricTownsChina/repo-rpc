package priv.eric.starter.server.registry;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.eric.starter.entity.ServiceInstanceInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static priv.eric.starter.common.Constant.ERIC_SERVICE_ROOT;

@Slf4j
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);
    private ZkClient zkClient;

    public ZookeeperServiceRegistry(String address) {
        initZkClient(address);
    }

    private void initZkClient(String registerAddress) {
        zkClient = new ZkClient(registerAddress);
        zkClient.setZkSerializer(new ZkSerializer() {
            @Override
            public byte[] serialize(Object o) throws ZkMarshallingError {
                return String.valueOf(o).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        });
    }

    @Override
    public void register(ServiceInstanceInfo serviceInstanceInfo) throws Exception {
        log.info("registry service info: {}", serviceInstanceInfo);

        // 注册服务信息, 永久节点
        String serviceNodePath = ERIC_SERVICE_ROOT + serviceInstanceInfo.getServiceName();
        if (!zkClient.exists(serviceNodePath)) {
            zkClient.createPersistent(serviceNodePath);
        }

        // 注册实例信息, 临时节点
        String uriPath = URLEncoder.encode(JSON.toJSONString(serviceInstanceInfo), StandardCharsets.UTF_8.name());
        if (zkClient.exists(uriPath)) {
            zkClient.delete(uriPath);
        }
        zkClient.createEphemeral(uriPath);
        logger.info("注册zookeeper节点: {}", uriPath);
    }

    @Override
    public ServiceInstanceInfo getRegistryInfo(String serviceName) throws Exception {
        return null;
    }

}
