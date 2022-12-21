package priv.eric.starter.client.discovery;

import priv.eric.starter.entity.ServiceInstanceInfo;

public interface ServiceDiscovery {

    /**
     * 根据服务名称获取服务实例
     *
     * @param serviceName 服务名称
     * @return 服务实例
     */
    ServiceInstanceInfo selectOneInstance(String serviceName);
}
