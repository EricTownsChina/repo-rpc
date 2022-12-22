package priv.eric.starter.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.eric.starter.client.discovery.ServiceDiscovery;
import priv.eric.starter.client.discovery.impl.ZookeeperServiceDiscovery;
import priv.eric.starter.server.registry.ServiceRegistry;
import priv.eric.starter.server.registry.impl.DefaultServiceRegistry;
import priv.eric.starter.server.registry.impl.ZookeeperServiceRegistry;

@Configuration
public class RpcAutoConfiguration {

    private RpcProperties rpcProperties;

    public RpcAutoConfiguration() {
    }

    public RpcAutoConfiguration(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Bean
    public ServiceRegistry serviceRegistry() {
        String registerType = rpcProperties.getRegisterType();
        if ("zookeeper".equals(registerType)) {
            return new ZookeeperServiceRegistry(rpcProperties.getRegisterAddress());
        }
        return new DefaultServiceRegistry();
    }

    @Bean
    public ServiceDiscovery serviceDiscovery() {
//        String registerType = rpcProperties.getRegisterType();
        return new ZookeeperServiceDiscovery(rpcProperties.getRegisterAddress());
    }

}
