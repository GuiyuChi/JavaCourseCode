package indi.kurok1.rpcfx.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启远程服务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RemoteServiceRegistrar.class)
public @interface EnableRemoteService {

    String[] baskPackages();

}
