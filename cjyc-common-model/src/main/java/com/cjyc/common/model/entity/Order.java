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
 * 订单表(客户下单)
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 订单编号
     */
    private String no;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 省
     */
    private String startProvince;

    /**
     * 省编号
     */
    private String startProvinceCode;

    /**
     * 市
     */
    private String startCity;

    /**
     * 市编号
     */
    private String startCityCode;

    /**
     * 区
     */
    private String startArea;

    /**
     * 区编号
     */
    private String startAreaCode;

    /**
     * 出发地详细地址
     */
    private String startAddress;

    /**
     * 出发地经度
     */
    private String startLng;

    /**
     * 出发地纬度
     */
    private String startLat;

    /**
     * 省
     */
    private String endProvince;

    /**
     * 省编号
     */
    private String endProvinceCode;

    /**
     * 市
     */
    private String endCity;

    /**
     * 市编号
     */
    private String endCityCode;

    /**
     * 区
     */
    private String endArea;

    /**
     * 区编号
     */
    private String endAreaCode;

    /**
     * 目的地详细地址
     */
    private String endAddress;

    /**
     * 目的地经度
     */
    private String endLng;

    /**
     * 目的地纬度
     */
    private String endLat;

    /**
     * 预计出发时间（提车日期）
     */
    private Long expectStartDate;

    /**
     * 预计到达时间
     */
    private Long expectEndDate;

    /**
     * 车辆总数
     */
    private Integer carNum;

    /**
     * 线路ID
     */
    private Long lineId;

    /**
     * 提车方式:1 自送，2代驾上门，3拖车上门
     */
    private Integer pickType;

    /**
     * 发车人
     */
    private String pickContactName;

    /**
     * 发车人联系方式
     */
    private String pickContactPhone;

    /**
     * 送车方式： 1 自提，2代驾上门，3拖车上门
     */
    private Integer backType;

    /**
     * 收车人
     */
    private String backContactName;

    /**
     * 收车人联系方式
     */
    private String backContactPhone;

    /**
     * 加急
     */
    private Integer hurryDays;

    /**
     * 订单来源：1用户app，2用户小程序，12业务员app，12业务员小程序，21韵车后台
     */
    private Integer source;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人：客户/业务员
     */
    private String createUserName;

    /**
     * 创建人类型：0客户，1业务员
     */
    private Integer createUserType;

    /**
     * 确认时间
     */
    private Long checkTime;

    /**
     * 确认人：业务员
     */
    private String checkSalesmanName;

    /**
     * 确认人ID
     */
    private Long checkSalesmanId;

    /**
     * 订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否开票：0否（默认根据设置），1是
     */
    private Integer invoiceFlag;

    /**
     * 发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票
     */
    private Integer invoiceType;

    /**
     * 应收提车费
     */
    private BigDecimal pickFee;

    /**
     * 应收干线费
     */
    private BigDecimal trunkFee;

    /**
     * 应收配送费
     */
    private BigDecimal backFee;

    /**
     * 应收保险费
     */
    private BigDecimal insuranceFee;

    /**
     * 应收订单定金（保留字段）
     */
    private BigDecimal depositFee;

    /**
     * 代收中介费（为资源合伙人代收）
     */
    private BigDecimal agencyFee;

    /**
     * 应收总价：提车费+干线费+送车费+保险费+中介费
     */
    private BigDecimal totalFee;

    /**
     * 车辆均摊费用（提车费/送车费/中转费）方式：0均分余数散列（默认），1不均分
     */
    private Integer feeShareType;

    /**
     * 合同ID
     */
    private Long customerContractId;

    /**
     * 客户付款方式：0时付（默认），1账期
     */
    private Integer customerPayType;

    /**
     * 客户支付尾款状态：0未支付，1部分支付，2支付完成
     */
    private Integer wlPayState;

    /**
     * 上次客户支付尾款时间
     */
    private Long wlPayTime;

    /**
     * 线下收款标识：默认0（不允许），
     */
    private Integer offlinePayFlag;


}
