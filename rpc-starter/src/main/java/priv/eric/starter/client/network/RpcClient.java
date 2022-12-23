package priv.eric.starter.client.network;

import priv.eric.starter.entity.ServiceInstanceInfo;

public interface RpcClient {

    /**
     * 发送消息
     *
     * @param data                消息数据
     * @param serviceInstanceInfo 消息接收方
     * @return 发送的消息
     */
    byte[] sendMsg(byte[] data, ServiceInstanceInfo serviceInstanceInfo) throws InterruptedException;

}
