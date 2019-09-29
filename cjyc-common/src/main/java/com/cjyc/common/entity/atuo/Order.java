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
 * 订单表(客户下单)
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
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
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 订单编号
     */
    @TableField("no")
    private String no;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 客户名称
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 省
     */
    @TableField("start_province")
    private String startProvince;

    /**
     * 省编号
     */
    @TableField("start_province_code")
    private String startProvinceCode;

    /**
     * 市
     */
    @TableField("start_city")
    private String startCity;

    /**
     * 市编号
     */
    @TableField("start_city_code")
    private String startCityCode;

    /**
     * 区
     */
    @TableField("start_area")
    private String startArea;

    /**
     * 区编号
     */
    @TableField("start_area_code")
    private String startAreaCode;

    /**
     * 出发地详细地址
     */
    @TableField("start_address")
    private String startAddress;

    /**
     * 出发地经度
     */
    @TableField("start_lng")
    private String startLng;

    /**
     * 出发地纬度
     */
    @TableField("start_lat")
    private String startLat;

    /**
     * 省
     */
    @TableField("end_province")
    private String endProvince;

    /**
     * 省编号
     */
    @TableField("end_province_code")
    private String endProvinceCode;

    /**
     * 市
     */
    @TableField("end_city")
    private String endCity;

    /**
     * 市编号
     */
    @TableField("end_city_code")
    private String endCityCode;

    /**
     * 区
     */
    @TableField("end_area")
    private String endArea;

    /**
     * 区编号
     */
    @TableField("end_area_code")
    private String endAreaCode;

    /**
     * 目的地详细地址
     */
    @TableField("end_address")
    private String endAddress;

    /**
     * 目的地经度
     */
    @TableField("end_lng")
    private String endLng;

    /**
     * 目的地纬度
     */
    @TableField("end_lat")
    private String endLat;

    /**
     * 预计出发时间（提车日期）
     */
    @TableField("expect_start_date")
    private Long expectStartDate;

    /**
     * 预计到达时间
     */
    @TableField("expect_end_date")
    private Long expectEndDate;

    /**
     * 车辆总数
     */
    @TableField("car_num")
    private Integer carNum;

    /**
     * 线路ID
     */
    @TableField("line_id")
    private Long lineId;

    /**
     * 提车方式:1 自送，2代驾上门，3拖车上门
     */
    @TableField("pick_type")
    private Integer pickType;

    /**
     * 发车人
     */
    @TableField("pick_contact_name")
    private String pickContactName;

    /**
     * 发车人联系方式
     */
    @TableField("pick_contact_phone")
    private String pickContactPhone;

    /**
     * 送车方式： 1 自提，2代驾上门，3拖车上门
     */
    @TableField("back_type")
    private Integer backType;

    /**
     * 收车人
     */
    @TableField("back_contact_name")
    private String backContactName;

    /**
     * 收车人联系方式
     */
    @TableField("back_contact_phone")
    private String backContactPhone;

    /**
     * 加急
     */
    @TableField("hurry_days")
    private Integer hurryDays;

    /**
     * 订单来源：1用户app，2用户小程序，12业务员app，12业务员小程序，21韵车后台
     */
    @TableField("source")
    private Integer source;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 创建人：客户/业务员
     */
    @TableField("create_user_name")
    private String createUserName;

    /**
     * 创建人类型：0客户，1业务员
     */
    @TableField("create_user_type")
    private Integer createUserType;

    /**
     * 确认时间
     */
    @TableField("check_time")
    private Long checkTime;

    /**
     * 确认人：业务员
     */
    @TableField("check_employee_name")
    private String checkEmployeeName;

    /**
     * 确认人ID
     */
    @TableField("check_employee_id")
    private Long checkEmployeeId;

    /**
     * 订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）
     */
    @TableField("state")
    private Integer state;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否开票：0否（默认根据设置），1是
     */
    @TableField("invoice_flag")
    private Integer invoiceFlag;

    /**
     * 发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票
     */
    @TableField("invoice_type")
    private Integer invoiceType;

    /**
     * 应收提车费
     */
    @TableField("pick_fee")
    private BigDecimal pickFee;

    /**
     * 应收干线费
     */
    @TableField("trunk_fee")
    private BigDecimal trunkFee;

    /**
     * 应收配送费
     */
    @TableField("back_fee")
    private BigDecimal backFee;

    /**
     * 应收保险费
     */
    @TableField("insurance_fee")
    private BigDecimal insuranceFee;

    /**
     * 应收订单定金（保留字段）
     */
    @TableField("deposit_fee")
    private BigDecimal depositFee;

    /**
     * 代收中介费（为资源合伙人代收）
     */
    @TableField("agency_fee")
    private BigDecimal agencyFee;

    /**
     * 应收总价：提车费+干线费+送车费+保险费+中介费
     */
    @TableField("total_fee")
    private BigDecimal totalFee;

    /**
     * 车辆均摊费用（提车费/送车费/中转费）方式：0均分余数散列（默认），1不均分
     */
    @TableField("fee_share_type")
    private Integer feeShareType;

    /**
     * 合同ID
     */
    @TableField("customer_contract_id")
    private Long customerContractId;

    /**
     * 客户付款方式：0时付（默认），1账期
     */
    @TableField("customer_pay_type")
    private Integer customerPayType;

    /**
     * 客户支付尾款状态：0未支付，1部分支付，2支付完成
     */
    @TableField("wl_pay_state")
    private Integer wlPayState;

    /**
     * 上次客户支付尾款时间
     */
    @TableField("wl_pay_time")
    private Long wlPayTime;

    /**
     * 线下收款标识：默认0（不允许），
     */
    @TableField("offline_pay_flag")
    private Integer offlinePayFlag;


}
