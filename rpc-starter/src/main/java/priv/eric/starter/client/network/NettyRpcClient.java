package priv.eric.starter.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.eric.starter.entity.ServiceInstanceInfo;

import java.util.concurrent.CountDownLatch;

public class NettyRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    @Override
    public byte[] sendMsg(byte[] data, ServiceInstanceInfo serviceInstanceInfo) throws InterruptedException {
        String ip = serviceInstanceInfo.getIp();
        Integer port = serviceInstanceInfo.getPort();

        ClientChannelHandler clientChannelHandler = new ClientChannelHandler(data);
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(clientChannelHandler);
                    }
                });

        bootstrap.connect(ip, port).sync();
        return clientChannelHandler.response();

    }

    private static class ClientChannelHandler extends ChannelInboundHandlerAdapter {

        private byte[] data;

        private byte[] response;

        private final CountDownLatch countDownLatch;

        public ClientChannelHandler(byte[] data) {
            this.data = data;
            countDownLatch = new CountDownLatch(1);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf buffer = Unpooled.buffer(data.length);
            buffer.writeBytes(data);
            ctx.writeAndFlush(buffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buffer = (ByteBuf) msg;
            response = new byte[buffer.readableBytes()];
            buffer.readBytes(response);
            countDownLatch.countDown();
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("发送消息失败: ", cause);
            ctx.close();
        }

        public byte[] response() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                // ignore
            }
            return response;
        }
    }
}
