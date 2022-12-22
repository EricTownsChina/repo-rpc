package priv.eric.starter.serialization;

import java.io.IOException;

public interface MsgSerialization {

    RpcRequest serializeRpcRequest(byte[] data) throws Exception;

    byte[] deserializeRpcRequest(RpcRequest rpcRequest) throws Exception;

    RpcResponse serializeRpcResponse(byte[] data) throws Exception;

    byte[] deserializeRpcResponse(RpcResponse rpcResponse) throws Exception;

}
