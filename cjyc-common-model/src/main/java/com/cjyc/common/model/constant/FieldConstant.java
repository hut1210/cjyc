package com.cjyc.common.model.constant;

public class FieldConstant {
    private FieldConstant(){}

    /**
     * 字典表中项值
     */
    public static final String ITEM = "system_config";

    /**
     * 用户端轮播图KEY
     */
    public static final String SYSTEM_PICTURE_CUSTOMER = "system_picture_customer";
    /**
     * 司机端轮播图KEY
     */
    public static final String SYSTEM_PICTURE_DRIVER = "system_picture_driver";
    /**
     * 业务员端轮播图KEY
     */
    public static final String SYSTEM_PICTURE_SALE = "system_picture_sale";

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

    /*******分享下单状态******/
    /**
     * 未下单
     */
    public static final int NOT_PLACE_ORDER = 0;
    /**
     * 已下单
     */
    public static final int PLACE_ORDER = 1;

    /*******司机端任务状态请求常量 不能删除 XML中使用******/
    /**
     * 分配任务
     */
    public static final String WAIT_HANDLE_TASK = "1";
    /**
     * 提车任务
     */
    public static final String PICK_CAR_TASK = "2";
    /**
     * 交车任务
     */
    public static final String GIVE_CAR_TASK = "3";
    /**
     * 已交付
     */
    public static final String FINISH_TASK = "4";

    /*******业务员端任务状态请求常量 不能删除  XML中使用******/
    /**
     * 全部
     */
    public static final String ALL_TASK = "0";
    /**
     * 待提车;待入库
     */
    public static final String WAIT_PICK_CAR = "1";
    /**
     * 待交车;待出库
     */
    public static final String WAIT_GIVE_CAR = "2";
    /**
     * 已交付：历史记录
     */
    public static final String FINISH = "3";
}