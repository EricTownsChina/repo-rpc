package priv.eric.starter.server.registry;

import lombok.extern.slf4j.Slf4j;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.server.registry.ServiceRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry {

    /**
     * 默认注册的服务
     */
    private final Map<String, ServiceInstanceInfo> registryService = new ConcurrentHashMap<>();

    @Override
    public void register(ServiceInstanceInfo serviceInstanceInfo) throws Exception {
        if (serviceInstanceInfo == null) {
            throw new IllegalArgumentException("服务实例信息为空!");
        }

        String serviceName = serviceInstanceInfo.getServiceName();
        registryService.put(serviceName, serviceInstanceInfo);
    }

    @Override
    public ServiceInstanceInfo getRegistryInfo(String serviceName) throws Exception {
        return registryService.get(serviceName);
    }

}
