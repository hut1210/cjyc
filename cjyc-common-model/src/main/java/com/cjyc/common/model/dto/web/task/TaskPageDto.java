package com.cjyc.common.model.dto.web.task;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description 我的任务查询条件对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/20 13:48
 **/
@Data
public class TaskPageDto extends BasePageDto {
    private static final long serialVersionUID = -6720067907147114367L;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "业务中心ID")
    @NotNull(message = "业务中心ID不能为空")
    private Long storeId;

    @ApiModelProperty(value = "库存状态：0全部，1待入库，2待出库")
    @Pattern(regexp = "[0|1|2]",message = "库存状态只能是0,1,2中的一位数")
    private String storageState;
}
