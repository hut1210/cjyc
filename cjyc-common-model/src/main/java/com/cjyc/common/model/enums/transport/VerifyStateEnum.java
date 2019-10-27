package com.cjyc.common.model.enums.transport;

public enum VerifyStateEnum {

    //状态：0待审核，2审核通过，4已驳回(审核不通过)，7已冻结
    BE_AUDITED("待审核", 0),
    AUDIT_PASS("已审核", 2),
    AUDIT_REJECT("已驳回", 4),
    FROZEN("已冻结",7);

    public String name;
    public int code;

    VerifyStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
