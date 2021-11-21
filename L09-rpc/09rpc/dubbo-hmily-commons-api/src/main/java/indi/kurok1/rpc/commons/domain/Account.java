package indi.kurok1.rpc.commons.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户
 */
@Data
public class Account implements Serializable {

    private Long id;
    private Long userId;
    private BigDecimal onHandQty;//已有数量
    private BigDecimal lockedQty;//锁定数量
    private BigDecimal inTransitQty;//在途数量

}
