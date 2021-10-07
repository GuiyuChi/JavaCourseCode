package inbound;

import filter.HeaderHttpRequestFilter;
import filter.HttpRequestFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHandler extends ChannelInboundHandlerAdapter {

    private HttpRequestFilter requestFilter = new HeaderHttpRequestFilter();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            requestFilter.filter(fullRequest, ctx);
            // 启动服务端为 gateway-server-0.0.1-SNAPSHOT.jar
            fetchGet(fullRequest, ctx, "http://localhost:8088/api/hello");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /*
     * 通过http客户端访问真实的后台接口 (okHttp)
     */
    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        FullHttpResponse response = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).addHeader("token", fullRequest.headers().get("token")).build();
            Response realResponse = client.newCall(request).execute();
            String value = realResponse.body().string();

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (IOException e) {
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
                ctx.flush();
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
