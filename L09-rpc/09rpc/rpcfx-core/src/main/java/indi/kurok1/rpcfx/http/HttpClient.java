package indi.kurok1.rpcfx.http;

import indi.kurok1.rpcfx.api.RpcfxRequest;
import indi.kurok1.rpcfx.api.RpcfxResponse;

public abstract class HttpClient {


    public abstract RpcfxResponse post(RpcfxRequest req, String url);

}
