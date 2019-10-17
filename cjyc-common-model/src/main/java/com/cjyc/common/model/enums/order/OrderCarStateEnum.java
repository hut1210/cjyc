package com.cjyc.common.model.enums.order;

/**
 * 订单车辆状态
 * @author JPG
 */
public enum OrderCarStateEnum {

    /***/
    WAIT_ROUTE("待路由",0),
    WAIT_PICK_DISPATCH("待提车调度",5),
    WAIT_PICK("待提车",10),
    PICKING("提车中（待交车）",15),
    SELF_PICKING("待自送交车",20),
    WAIT_TRUNK_DISPATCH("待干线调度<循环>（提车入库）",25),
    WAIT_TRUNK("待干线提车<循环>",35),
    TRUNKING("干线中<循环>（待干线交车）",40),
    WAIT_BACK_DISPATCH("待配送调度（干线入库）",45),
    WAIT_BACK("待配送提车",50),
    BACKING("配送中（待配送交车）",55),
    SELF_BACKING("待自取提车",70),
    SIGNED("已签收",100);

    public String name;
    public int code;

    OrderCarStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
