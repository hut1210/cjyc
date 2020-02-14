package com.cjyc.common.model.vo.web.bankInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BankCardVerifyVo implements Serializable {

    private static final long serialVersionUID = 5308581866872904479L;
    private String cardType;
    private String bank;
    private String key;
    private List<String> messages;
    private String validated;
    private String stat;
}