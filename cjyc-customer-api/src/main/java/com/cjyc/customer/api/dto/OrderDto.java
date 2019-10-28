package com.cjyc.customer.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @auther litan
 * @description: 订单入参实体类
 * @date:2019/10/8
 */
@Data
public class OrderDto implements Serializable {

    public interface SimpleOrderVali {
    }
    public interface DetailOrderVali {
    }

    @ApiModelProperty(value = "0-保存（草稿） 1-下单",required = true)
    private int saveType;//0-保存（草稿） 1-下单
    @ApiModelProperty(value = "0-详单 1-简单",required = true)
    private int isSimple;

    @ApiModelProperty(value = "订单Id(修改订单时必传)")
    private Long orderId;

    @ApiModelProperty(value = "客户id",required = true)
    private Long customerId;
    @ApiModelProperty(value = "客户姓名",required = true)
    private String customerName;

    @ApiModelProperty(value = "始发省",required = true)
    @NotBlank(groups = {SimpleOrderVali.class,DetailOrderVali.class},message = "始发城市code不能为空")
    private String startProvince;
    @ApiModelProperty(value = "始发省code",required = true)
    private String startProvinceCode;

    @ApiModelProperty(value = "始发市",required = true)
    @NotBlank(groups = {DetailOrderVali.class},message = "始发城市code不能为空")
    private String startCity;
    @ApiModelProperty(value = "始发市code",required = true)
    private String startCityCode;

    @ApiModelProperty(value = "始发区县")
    private String startArea;
    @ApiModelProperty(value = "始发区县code")
    private String startAreaCode;
    @ApiModelProperty(value = "始发地详细地址",required = true)
    private String startAddress;

    @ApiModelProperty(value = "目的省",required = true)
    private String endProvince;
    @ApiModelProperty(value = "目的省code",required = true)
    private String endProvinceCode;
    @ApiModelProperty(value = "目的市",required = true)
    private String endCity;
    @ApiModelProperty(value = "目的市code",required = true)
    private String endCityCode;
    @ApiModelProperty(value = "目的区县")
    private String endArea;
    @ApiModelProperty(value = "目的区县code")
    private String endAreaCode;
    @ApiModelProperty(value = "目的地详细地址",required = true)
    private String endAddress;

    @ApiModelProperty(value = "期望提车日期",required = true)
    private String expectStartDate;
    @ApiModelProperty(value = "车辆数",required = true)
    private int carNum;
    @ApiModelProperty(value = "班线id",required = true)
    private String lineId;
    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门",required = true)
    private int pickType;
    @ApiModelProperty(value = "发车联系人",required = true)
    private String pickContactName;
    @ApiModelProperty(value = "发车联系人电话",required = true)
    private String PickContactPhone;
    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门",required = true)
    private int backType;
    @ApiModelProperty(value = "收车联系人",required = true)
    private String backContactName;
    @ApiModelProperty(value = "收车联系人电话",required = true)
    private String backContactPhone;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否开票：0否（默认根据设置），1是")
    private int invoiceFlag;
    @ApiModelProperty(value = "是否预付款：0否（默认根据设置），1是")
    private int advanceFlag;
    @ApiModelProperty(value = "发票类型：0无， 1-普通(个人) ，2增值普票(企业) ，3增值专用发票")
    private int invoiceType;
    @ApiModelProperty(value = "预估费用 单位：分")
    private int totalFee;
    @ApiModelProperty(value = "合同ID")
    private int customerContractId;//合同ID
    @ApiModelProperty(value = "优惠券ID")
    private int couponId;
    private List<OrderCarDto> orderCarDtoList;

}
