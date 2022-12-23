package priv.eric.starter.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import priv.eric.starter.client.ClientProxyFactory;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.entity.annotation.ServiceExpose;
import priv.eric.starter.server.network.RpcServer;
import priv.eric.starter.server.registry.ServiceRegistry;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class DefaultRpcListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRpcListener.class);

    private ServiceRegistry serviceRegistry;

    private RpcServer rpcServer;

    private ClientProxyFactory clientProxyFactory;

    public DefaultRpcListener() {
    }

    public DefaultRpcListener(ServiceRegistry serviceRegistry, RpcServer rpcServer, ClientProxyFactory clientProxyFactory) {
        this.serviceRegistry = serviceRegistry;
        this.rpcServer = rpcServer;
        this.clientProxyFactory = clientProxyFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() == null) {
            try {
                // 刷新当前服务作为服务端暴露的服务信息(接口信息, @ServiceExpose)
                refreshRpcServer(applicationContext);
                // 启动当前服务为rpc服务端, 监听指定端口
                rpcServer.start();
                // 刷新当前服务作为客户端引用的远程bean信息(@ServiceRef)
                refreshRpcClient(applicationContext);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void refreshRpcClient(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(beanName -> {
            Class<?> clazz = applicationContext.getType(beanName);
            if (clazz == null) {
                return;
            }

            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                Class<?> fieldClazz = field.getType();
                Object bean = applicationContext.getBean(beanName);
                Object proxyBean = clientProxyFactory.getProxyInstance(fieldClazz);
                try {
                    field.setAccessible(true);
                    field.set(bean, proxyBean);
                } catch (IllegalAccessException e) {
                    logger.error("依赖注入失败, bean.name: {}, exception: ", beanName, e);
                }
            }
        });
    }

    private void refreshRpcServer(ApplicationContext applicationContext) throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ServiceExpose.class);

        Environment environment = applicationContext.getEnvironment();
        String serviceName = environment.getProperty("spring.application.name");
        Integer port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.port")));
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
