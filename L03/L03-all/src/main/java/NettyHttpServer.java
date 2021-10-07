import inbound.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyHttpServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8808;
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();

                        // 解码成HttpRequest
                        p.addLast(new HttpServerCodec());
                        // 解码成FullHttpRequest
                        p.addLast(new HttpObjectAggregator(1024 * 1024));
                        p.addLast(new HttpHandler());
                    }
                });

        // 绑定IP和端口 同步等待成功
        Channel ch = b.bind(port).sync().channel();
        System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');

        //等待服务端监听端口关闭
        ch.closeFuture().sync();
    }
}
