package com.cjyc.common.model.vo.driver.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 车辆详情
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 13:26
 **/
@Data
public class CarDetailVo implements Serializable {
    private static final long serialVersionUID = -6283733541452871958L;
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "交车日期:已交付的运单")
    private Long completeTime;

    @ApiModelProperty(value = "提车图片地址，逗号分隔")
    private String loadPhotoImg;

    @ApiModelProperty(value = "交车图片地址，逗号分隔:已交付的运单")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;
}
