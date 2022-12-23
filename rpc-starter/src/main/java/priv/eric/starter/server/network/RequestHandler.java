package priv.eric.starter.server.network;

import lombok.extern.slf4j.Slf4j;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.serialization.MsgSerialization;
import priv.eric.starter.serialization.RpcRequest;
import priv.eric.starter.serialization.RpcResponse;
import priv.eric.starter.server.registry.ServiceRegistry;

import java.lang.reflect.Method;

@Slf4j
public class RequestHandler {

    private final MsgSerialization msgSerialization;

    private final ServiceRegistry serviceRegistry;

    public RequestHandler(MsgSerialization msgSerialization, ServiceRegistry serviceRegistry) {
        this.msgSerialization = msgSerialization;
        this.serviceRegistry = serviceRegistry;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        RpcRequest rpcRequest = msgSerialization.deserializeRpcRequest(data);
        String serviceName = rpcRequest.getServiceName();
        ServiceInstanceInfo instanceInfo = serviceRegistry.getRegistryInfo(serviceName);
        if (instanceInfo == null) {
            return msgSerialization.serializeRpcResponse(RpcResponse.fail().build());
        }

        Method method = instanceInfo.getClazz().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        Object returnValue = method.invoke(instanceInfo.getBean(), rpcRequest.getParameters());
        return msgSerialization.serializeRpcResponse(RpcResponse.success().setReturnValue(returnValue).build());
    }

}
