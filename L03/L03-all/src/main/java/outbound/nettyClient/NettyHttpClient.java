package outbound.nettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kotlin.text.Charsets;

import java.net.InetSocketAddress;
import java.net.URI;

public class NettyHttpClient {
    public void connect(String host, int port, final String uri, final ChannelHandlerContext outCtx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.remoteAddress(new InetSocketAddress(host, port));
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, false);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 编码
                    ch.pipeline().addLast(new HttpClientCodec());
                    // 聚合
                    ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                    // 解压
//                    ch.pipeline().addLast(new HttpContentCompressor());
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));

                    ch.pipeline().addLast(new NettyHttpClientHandler(uri, outCtx));
                }
            });

            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


}
