package com.cjyc.common.model.dto.web.mineStore;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 在库车辆列表查询对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/25 9:51
 **/
@Data
public class StorageCarQueryDto  extends BasePageDto {
    private static final long serialVersionUID = -3745261240468858633L;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;

    @ApiModelProperty(value = "车辆编码")
    private String carNo;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "当前业务中心")
    @NotNull(message = "所属业务中心ID不能为空")
    private Long nowStoreId;
}
