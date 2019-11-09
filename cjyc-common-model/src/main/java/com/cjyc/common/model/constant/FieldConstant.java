package com.cjyc.common.model.constant;

public class FieldConstant {
    private FieldConstant(){}

    /**
     * 字典表中项值
     */
    public static final String ITEM = "system_config";

    /**
     * 字典表中项值
     */
    public static final String SYSTEM_PICTURE = "system_picture";

    /*******开发票状态******/
    /**
     * 发票申请中
     */
    public static final int INVOICE_APPLY_IN = 1;
    /**
     * 发票已开
     */
    public static final int INVOICE_FINISH = 2;
    /**
     * 开票失败
     */
    public static final int INVOICE_FAIL = 3;

    /*******城市级别******/
    /**
     * 大区级别
     */
    public static final int REGION_LEVEL = 0;
    /**
     * 省级别
     */
    public static final int PROVINCE_LEVEL = 1;

    /*******中国编码和名称******/
    /**
     * 中国编码
     */
    public static final String CHINA_CODE = "000000";
    /**
     * 中国名称
     */
    public static final String CHINA_NAME = "中国";

    /*******未覆盖大区编号和名称******/
    /**
     * 未覆盖大区编码
     */
    public static final String NOT_REGION_CODE = "000008";
    /**
     * 未覆盖大区名称
     */
    public static final String NOT_REGION_NAME = "未覆盖大区";
}