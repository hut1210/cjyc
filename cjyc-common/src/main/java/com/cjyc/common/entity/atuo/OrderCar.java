package com.cjyc.common.entity.atuo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-09-29
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
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 车辆编码
     */
    @TableField("no")
    private String no;

    /**
     * 品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 型号
     */
    @TableField("model")
    private String model;

    /**
     * 车牌号
     */
    @TableField("plate_no")
    private String plateNo;

    /**
     * vin码
     */
    @TableField("vin")
    private String vin;

    /**
     * 估值/万
     */
    @TableField("valuation")
    private Integer valuation;

    /**
     * 当前所在地所属业务中心
     */
    @TableField("now_store_id")
    private Long nowStoreId;

    /**
     * 当前所在区
     */
    @TableField("now_area_code")
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
    @TableField("state")
    private Integer state;

    /**
     * 车辆描述
     */
    @TableField("description")
    private String description;

    /**
     * 车辆应收提车费
     */
    @TableField("pick_fee")
    private BigDecimal pickFee;

    /**
     * 车辆应收干线费
     */
    @TableField("trunk_fee")
    private BigDecimal trunkFee;

    /**
     * 车辆应收送车费
     */
    @TableField("back_fee")
    private BigDecimal backFee;

    /**
     * 车辆应收保险费
     */
    @TableField("insurance_fee")
    private BigDecimal insuranceFee;

    /**
     * 保额/万
     */
    @TableField("insurance_coverage_amount")
    private Integer insuranceCoverageAmount;

    /**
     * 车辆代收中介费（为资源合伙人代收）
     */
    @TableField("agency_fee")
    private BigDecimal agencyFee;

    /**
     * 应收状态：0未支付，1已支付
     */
    @TableField("wl_pay_state")
    private Integer wlPayState;


}
