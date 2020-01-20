package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 调度车辆明细
 * @Author Liu Xing Xiang
 * @Date 2019/12/13 14:06
 **/
@Data
public class DispatchCarDetailVo implements Serializable {
    private static final long serialVersionUID = 8436763608318044836L;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车系")
    private String model;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty("车辆logo图片链接")
    private String logoImg;

    @ApiModelProperty(value = "提车时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long expectStartDate;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "调度记录列表")
    private List<DispatchRecordVo> dispatchRecordVoList;
}
