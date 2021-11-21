package indi.kurok1.rpc.dubbo.hmily;

import indi.kurok1.rpc.commons.api.AccountService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * 测试客户端
 *
 */
public class ClientApplication {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:dubbo-client.xml");
        AccountService accountService = context.getBean(AccountService.class);
        accountService.transfer(2L,1L,new BigDecimal(30));
    }

}
