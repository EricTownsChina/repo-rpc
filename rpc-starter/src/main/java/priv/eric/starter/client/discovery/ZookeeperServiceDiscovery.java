package priv.eric.starter.client.discovery;

import com.alibaba.fastjson2.JSON;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.eric.starter.entity.ServiceInstanceInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static priv.eric.starter.common.Constant.ERIC_SERVICE_ROOT;

public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private final ZkClient zkClient;

    public ZookeeperServiceDiscovery(String address) {
        this.zkClient = new ZkClient(address);
    }

    @Override
    public ServiceInstanceInfo selectOneInstance(String serviceName) {
        final List<String> children = zkClient.getChildren(ERIC_SERVICE_ROOT + serviceName);
        return Optional.ofNullable(children)
                .orElse(new ArrayList<>(0))
                .stream()
                .map(node -> {
                    try {
                        String serviceInstanceJson = URLDecoder.decode(node, StandardCharsets.UTF_8.name());
                        return JSON.parseObject(serviceInstanceJson, ServiceInstanceInfo.class);
                    } catch (UnsupportedEncodingException e) {
                        logger.error("parse serviceInstanceInfo error: ", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

}
