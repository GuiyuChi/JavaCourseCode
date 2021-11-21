package indi.kurok1.rpcfx.client;

import java.lang.annotation.*;

/**
 * 标记一个接口为远程调用服务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RemoteService {

    String serviceName();

    String url();

}
