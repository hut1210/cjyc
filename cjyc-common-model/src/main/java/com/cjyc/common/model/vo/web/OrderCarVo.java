package com.cjyc.common.model.vo.web;

import com.cjyc.common.model.entity.OrderCar;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 通用OrderCarVo，附加订单信息（不用做返回信息）
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OrderCarVo extends OrderCar implements Serializable {

    @ApiModelProperty(value = "客户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户电话")
    private String customerPhone;

    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人")
    private Integer customerType;

    @ApiModelProperty(value = "订单所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inputStoreId;

    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startStoreId;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "省")
    private String endProvince;
    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;
    @ApiModelProperty(value = "市")
    private String endCity;
    @ApiModelProperty(value = "市编号")
    private String endCityCode;
    @ApiModelProperty(value = "区")
    private String endArea;
    @ApiModelProperty(value = "区编号")
    private String endAreaCode;
    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreId;
    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endBelongStoreId;
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    public String getEndStoreName() {
        return endStoreName == null ? "" : endStoreName;
    }

    @ApiModelProperty(value = "省")
    private String endStoreProvince;

    @ApiModelProperty(value = "省编号")
    private String endStoreProvinceCode;
    @ApiModelProperty(value = "市")
    private String endStoreCity;
    @ApiModelProperty(value = "市编号")
    private String endStoreCityCode;
    @ApiModelProperty(value = "区")
    private String endStoreArea;
    @ApiModelProperty(value = "区编号")
    private String endStoreAreaCode;
    @ApiModelProperty(value = "目的地业务中心地址")
    private String endStoreAddress;
    @ApiModelProperty(value = "目的地业务中心联系人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreLooplinkUserId;
    @ApiModelProperty(value = "目的地业务中心联系人")
    private String endStoreLooplinkName;
    @ApiModelProperty(value = "目的地业务中心联系人手机号")
    private String endStoreLooplinkPhone;
    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private Integer orderState;




    public Long getEndStoreId() {
        return endStoreId == null ? 0 : endStoreId;
    }

    public String getEndStoreProvince() {
        return endStoreProvince == null ? "" : endStoreProvince;
    }

    public String getEndStoreProvinceCode() {
        return endStoreProvinceCode == null ? "" : endStoreProvinceCode;
    }

    public String getEndStoreCity() {
        return endStoreCity == null ? "" : endStoreCity;
    }

    public String getEndStoreCityCode() {
        return endStoreCityCode == null ? "" : endStoreCityCode;
    }

    public String getEndStoreArea() {
        return endStoreArea == null ? "" : endStoreArea;
    }

    public String getEndStoreAreaCode() {
        return endStoreAreaCode == null ? "" : endStoreAreaCode;
    }

    public String getEndStoreAddress() {
        return endStoreAddress == null ? "" : endStoreAddress;
    }

    public Long getEndStoreLooplinkUserId() {
        return endStoreLooplinkUserId;
    }

    public String getEndStoreLooplinkName() {
        return endStoreLooplinkName == null ? "" : endStoreLooplinkName;
    }

    public String getEndStoreLooplinkPhone() {
        return endStoreLooplinkPhone == null ? "" : endStoreLooplinkPhone;
    }

}
