package com.cjyc.common.model.vo.web;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:Hut
 * @Date:2019/10/10 13:40
 */
@Data
public class WayBillCarrierVo extends BasePageDto {

    @ApiModelProperty(value = "运单编号")
    private String wayBillNo;//运单编号

    @ApiModelProperty(value = "车辆编号")
    private String carNo;//车辆编号

    @ApiModelProperty(value = "Vin码")
    private String vin;//Vin码

    @ApiModelProperty(value = "品牌")
    private String brand;//品牌

    @ApiModelProperty(value = "车系")
    private String model;//车系

    @ApiModelProperty(value = "调度类型")
    private Integer dispatchType;//调度类型

    @ApiModelProperty(value = "运载方式")
    private Integer deliveryWay;//运载方式

    @ApiModelProperty(value = "提车日期")
    private String startDate;//提车日期

    @ApiModelProperty(value = "提车日期")
    private String endDate;//提车日期

    @ApiModelProperty(value = "实际提车日期")
    private String loadStartTime;//实际提车日期

    @ApiModelProperty(value = "实际提车日期")
    private String loadEndTime;//实际提车日期

    @ApiModelProperty(value = "实际交车日期")
    private String unLoadStartTime;//实际交车日期

    @ApiModelProperty(value = "实际交车日期")
    private String unLoadEndTime;//实际交车日期

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;//承运商名称

    @ApiModelProperty(value = "司机名字")
    private String driverName;//司机名字

    @ApiModelProperty(value = "司机电话")
    private String phone;//司机电话

    @ApiModelProperty(value = "车牌号")
    private String vehicleNo;//车牌号

    @ApiModelProperty(value = "提送车业务中心")
    private String storeId;//提送车业务中心

    @ApiModelProperty(value = "订单编号")
    private String orderNo;//订单编号

    @ApiModelProperty(value = "发车人")
    private String pickContactName;//发车人

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;//发车人联系方式

    @ApiModelProperty(value = "收车人")
    private String backContactName;//收车人
}
