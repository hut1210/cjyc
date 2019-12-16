package com.cjyc.common.model.enums.waybill;

public enum WaybillCarrierTypeEnum {

    /**1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己*/
    TRUNK_INDIVIDUAL("干线-个人承运商", 1),
    TRUNK_ENTERPRISE("干线-企业承运商", 2),
    LOCAL_ADMIN("同城-业务员", 3),
    LOCAL_PILOT("同城-代驾", 4),
    LOCAL_CONSIGN("同城-拖车", 5),
    SELF("客户自己", 6);

    public final String name;
    public final int code;

    WaybillCarrierTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
