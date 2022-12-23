package priv.eric.starter.serialization;

public interface MsgSerialization {

    /**
     * 序列化为RPC请求
     *
     * @param data 字节数组
     * @return RPC请求
     * @throws Exception exception
     */
    RpcRequest deserializeRpcRequest(byte[] data) throws Exception;

    /**
     * 反序列化RPC请求为字节数组
     *
     * @param rpcRequest RPC请求
     * @return 字节数组
     * @throws Exception exception
     */
    byte[] serializeRpcRequest(RpcRequest rpcRequest) throws Exception;

    /**
     * 反序列化为RPC响应
     *
     * @param data 字节数组
     * @return RPC响应
     * @throws Exception exception
     */
    RpcResponse deserializeRpcResponse(byte[] data) throws Exception;

    /**
     * 序列化RPC响应为字节数组
     *
     * @param rpcResponse RPC响应
     * @return 字节数组
     * @throws Exception exception
     */
    byte[] serializeRpcResponse(RpcResponse rpcResponse) throws Exception;

}
