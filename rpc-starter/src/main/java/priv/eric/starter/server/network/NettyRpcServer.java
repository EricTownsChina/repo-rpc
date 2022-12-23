package priv.eric.starter.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private final int port;

    private final RequestHandler requestHandler;

    private Channel channel;

    public NettyRpcServer(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
    }

    @Override
    public void start() {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ChannelRequestHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channel = channelFuture.channel();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("服务端异常: ", e);
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
        if (channel == null) {
            return;
        }
        channel.close();
    }

    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("channel active : {}", ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("服务端收到消息: {}", msg);
            final ByteBuf msgByteBuf = (ByteBuf) msg;
            final byte[] msgBytes = new byte[msgByteBuf.readableBytes()];
            msgByteBuf.readBytes(msgBytes);

            final byte[] responseBytes = requestHandler.handleRequest(msgBytes);
            ByteBuf responseByteBuf = Unpooled.buffer(responseBytes.length);
            responseByteBuf.writeBytes(responseBytes);
            ctx.writeAndFlush(responseBytes);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("服务端异常: ", cause);
        }
    }
}
