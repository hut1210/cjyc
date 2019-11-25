package com.cjyc.common.model;

import lombok.Data;

import java.text.MessageFormat;

@Data
public class FailResultReasonVo {
    private String no;
    private String reason;

    public FailResultReasonVo(Object no, String reason) {
        this.no = no.toString();
        this.reason = reason;
    }

    public FailResultReasonVo(Object no, String reason, Object...args) {
        this.no = no.toString();
        this.reason = MessageFormat.format(reason, args);
    }
}
