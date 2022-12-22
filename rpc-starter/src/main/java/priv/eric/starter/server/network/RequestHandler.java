package priv.eric.starter.server.network;

import lombok.extern.slf4j.Slf4j;
import priv.eric.starter.entity.ServiceInstanceInfo;
import priv.eric.starter.serialization.MsgSerialization;
import priv.eric.starter.serialization.RpcRequest;
import priv.eric.starter.server.registry.ServiceRegistry;

@Slf4j
public class RequestHandler {

    private MsgSerialization msgSerialization;

    private ServiceRegistry serviceRegistry;

    public RequestHandler(MsgSerialization msgSerialization, ServiceRegistry serviceRegistry) {
        this.msgSerialization = msgSerialization;
        this.serviceRegistry = serviceRegistry;
    }

    public byte[] handleRequest(byte[] data) throws Exception {
        RpcRequest rpcRequest = msgSerialization.serializeRpcRequest(data);
        String serviceName = rpcRequest.getServiceName();
        ServiceInstanceInfo registryInfo = serviceRegistry.getRegistryInfo(serviceName);

        return new byte[0];

    }

}
