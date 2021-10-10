package outbound.nettyClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientHandler extends ChannelInboundHandlerAdapter {
    private String uriStr;

    // 用于输出的ctx
    private ChannelHandlerContext outCtx;

    public NettyHttpClientHandler(String uri, ChannelHandlerContext outCtx) {
        this.uriStr = uri;
        this.outCtx = outCtx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        ByteBuf content = response.content();
        HttpHeaders headers = response.headers();
        String value = content.toString(CharsetUtil.UTF_8);

        System.out.println("content:" + value);
        System.out.println("header:" + headers.toString());

        // 获取到数据后，关闭client与实际后端的连接
        ctx.close();

        FullHttpResponse outResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
        outResponse.headers().set("Content-Type", "application/json");
        outResponse.headers().setInt("Content-Length", response.content().readableBytes());
        outCtx.writeAndFlush(outResponse);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI uri = new URI(uriStr);
        String msg = "";

        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
        request.headers()
//                .set(HttpHeaderNames.HOST, "localhost:8088")
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        // 发送数据
        ctx.writeAndFlush(request);
    }
}
