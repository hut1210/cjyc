package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WaybillVo {

    @ApiModelProperty("承运商")
    private String carrier;
    @ApiModelProperty("司机名称")
    private String driverName;
    @ApiModelProperty("司机电话")
    private String driverPhone;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("车牌号")
    private String vehiclePlateNo;
    @ApiModelProperty("车辆列表")
    private List<WaybillCarVo> list;




    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "调度类型：1自己处理，2人工调度")
    private Integer source;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "推荐线路")
    private String recommendLine;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "承运商类型：0承运商，1业务员，2客户自己")
    private Integer carrierType;

    private String carrierName;

    @ApiModelProperty(value = "车数量")
    private Integer carNum;

    @ApiModelProperty(value = "运单状态：0待分配承运商（竞抢），15待承运商承接任务，55运输中，100已完成，111超时关闭，113已取消，115已拒接")
    private Integer state;

    @ApiModelProperty(value = "运单总运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运费支付状态")
    private Integer freightPayState;

    @ApiModelProperty(value = "运费支付时间")
    private String freightPayTime;

    @ApiModelProperty(value = "运费支付流水单号")
    private String freightPayBillno;

    @ApiModelProperty(value = "运费是否固定（包板）0否，1是")
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "调度人")
    private String createUser;

    @ApiModelProperty(value = "调度人ID")
    private Long createUserId;

    @ApiModelProperty(value = "所属业务中心ID")
    private Long inputStoreId;


}
