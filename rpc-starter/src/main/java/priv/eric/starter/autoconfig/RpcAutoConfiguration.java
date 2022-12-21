package priv.eric.starter.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.eric.starter.client.discovery.ServiceDiscovery;
import priv.eric.starter.client.discovery.impl.ZookeeperServiceDiscovery;
import priv.eric.starter.server.registry.ServiceRegistry;
import priv.eric.starter.server.registry.impl.ZookeeperServiceRegistry;

import javax.annotation.Resource;

@Configuration
public class RpcAutoConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public ServiceRegistry serviceRegistry() {
//        String registerType = rpcProperties.getRegisterType();
        return new ZookeeperServiceRegistry(rpcProperties.getAddress());
    }

    @Bean
    public ServiceDiscovery serviceDiscovery() {
//        String registerType = rpcProperties.getRegisterType();
        return new ZookeeperServiceDiscovery(rpcProperties.getAddress());
    }

}
