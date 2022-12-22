package priv.eric.starter.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.entity.annotation.ServiceExpose;
import priv.eric.starter.server.registry.ServiceRegistry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class DefaultRpcListener implements ApplicationListener<ContextRefreshedEvent> {

    private ServiceRegistry serviceRegistry;

    public DefaultRpcListener() {
    }

    public DefaultRpcListener(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // TODO: 2022/12/22 完成事件监听器逻辑
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() == null) {
            try {
                refreshRpcServer(applicationContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void refreshRpcServer(ApplicationContext applicationContext) throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ServiceExpose.class);

        Environment environment = applicationContext.getEnvironment();
        String serviceName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        String ip = InetAddress.getLocalHost().getHostAddress();

        ServiceInstanceInfo instanceInfo = ServiceInstanceInfo.n()
                .setServiceName(serviceName)
                .setIp(ip)
                .setPort(port)
                .build();

        for (Object o : beans.values()) {
            registerServiceInstanceInfo(o, instanceInfo);
        }
    }

    private void registerServiceInstanceInfo(Object bean, ServiceInstanceInfo instanceInfo) throws Exception {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            // 为实现接口, 无法通过JDK代理增强
            return;
        }

        Class<?> interfaceClazz = interfaces[0];
        String interfaceName = interfaceClazz.getName();
        instanceInfo.setClazz(interfaceClazz);
        instanceInfo.setInstanceId(interfaceName);
        instanceInfo.setBean(bean);

        serviceRegistry.register(instanceInfo);
    }

}
