package priv.eric.starter.serialization.handler;

import priv.eric.starter.serialization.MsgSerialization;
import priv.eric.starter.serialization.RpcRequest;
import priv.eric.starter.serialization.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultMsgSerializationHandler implements MsgSerialization {

    @Override
    public RpcRequest serializeRpcRequest(byte[] data) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcRequest) objectInputStream.readObject();
    }

    @Override
    public byte[] deserializeRpcRequest(RpcRequest rpcRequest) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcRequest);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public RpcResponse serializeRpcResponse(byte[] data) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcResponse) objectInputStream.readObject();
    }

    @Override
    public byte[] deserializeRpcResponse(RpcResponse rpcResponse) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcResponse);
        return byteArrayOutputStream.toByteArray();
    }
}
