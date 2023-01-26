package priv.eric.starter.client;

import priv.eric.starter.client.discovery.ServiceDiscovery;
import priv.eric.starter.client.network.RpcClient;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.serialization.MsgSerialization;
import priv.eric.starter.serialization.RpcRequest;
import priv.eric.starter.serialization.RpcResponse;

import java.lang.reflect.Proxy;

public class ClientProxyFactory {

    private final ServiceDiscovery serviceDiscovery;

    private final MsgSerialization msgSerialization;

    private final RpcClient rpcClient;

    public ClientProxyFactory(ServiceDiscovery serviceDiscovery, MsgSerialization msgSerialization, RpcClient rpcClient) {
        this.serviceDiscovery = serviceDiscovery;
        this.msgSerialization = msgSerialization;
        this.rpcClient = rpcClient;
    }

    /**
     * 获取代理对象, 远程调用获取结果
     *
     * @param clazz 接口Class
     * @param <T>   结果类型
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            String serviceName = clazz.getName();
            ServiceInstanceInfo instanceInfo = serviceDiscovery.selectOneInstance(serviceName);
            if (instanceInfo == null) {
                throw new NullPointerException("未获取到" + serviceName + "的远程服务信息!");
            }

            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setServiceName(serviceName);
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParameterTypes(method.getParameterTypes());
            rpcRequest.setParameters(args);

            byte[] rpcRequestBytes = msgSerialization.serializeRpcRequest(rpcRequest);
            byte[] rpcResponseBytes = rpcClient.sendMsg(rpcRequestBytes, instanceInfo);

            RpcResponse rpcResponse = msgSerialization.deserializeRpcResponse(rpcResponseBytes);
            if (rpcResponse.getException() != null) {
                throw rpcResponse.getException();
            }

            return rpcResponse.getReturnValue();
        });
    }

}
