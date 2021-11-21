package indi.kurok1.rpc.commons.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 */
@Data
public class User implements Serializable {

    private Long id;
    private String code;
    private String name;

}
