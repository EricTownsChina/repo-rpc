package priv.eric.starter.server.registry;

import priv.eric.starter.entity.ServiceInstanceInfo;

public interface ServiceRegistry {

    /**
     * 注册服务
     *
     * @param serviceInstanceInfo 服务信息
     * @throws Exception 异常
     */
    void register(ServiceInstanceInfo serviceInstanceInfo) throws Exception;

    ServiceInstanceInfo getRegistryInfo(String serviceName) throws Exception;

}
