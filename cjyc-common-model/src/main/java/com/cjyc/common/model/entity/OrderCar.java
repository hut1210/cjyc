package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细（车辆表）
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order_car")
public class OrderCar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 车辆编码
     */
    private String no;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * vin码
     */
    private String vin;

    /**
     * 估值/万
     */
    private Integer valuation;

    /**
     * 当前所在地所属业务中心
     */
    private Long nowStoreId;

    /**
     * 当前所在区
     */
    private String nowAreaCode;

    /**
     * 状态：0待路由，
5待提车调度，
10待提车，
15提车中（待交车），
20待自送交车
25待干线调度<循环>（提车入库），
35待干线提车<循环>，
40干线中<循环>（待干线交车），
45待配送调度（干线入库），
50待配送提车，
55配送中（待配送交车），
70待自取提车，
100已签收
     */
    private Integer state;

    /**
     * 车辆描述
     */
    private String description;

    /**
     * 车辆应收提车费
     */
    private BigDecimal pickFee;

    /**
     * 车辆应收干线费
     */
    private BigDecimal trunkFee;

    /**
     * 车辆应收送车费
     */
    private BigDecimal backFee;

    /**
     * 车辆应收保险费
     */
    private BigDecimal insuranceFee;

    /**
     * 保额/万
     */
    private Integer insuranceCoverageAmount;

    /**
     * 车辆代收中介费（为资源合伙人代收）
     */
    private BigDecimal agencyFee;

    /**
     * 应收状态：0未支付，1已支付
     */
    private Integer wlPayState;


}
