package com.cjyc.common.model.dto.salesman.task;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description 出入库查询对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/10 15:10
 **/
@Data
public class OutAndInStorageQueryDto extends BasePageDto {
    private static final long serialVersionUID = 6432279430431382992L;
    @ApiModelProperty(value = "用户登录id")
    @NotNull(message = "用户登录id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "库存状态：0全部,1待入库,2待出库,3已入库和已出库")
    @Pattern(regexp = "[0|1|2|3]",message = "库存状态只能是0,1,2,3中的一位数")
    private String storageState;

    @ApiModelProperty(value = "接单日期")
    private Long creatTime;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "出发地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "入库日期开始")
    private Long inStorageTimeS;

    @ApiModelProperty(value = "入库日期结束")
    private Long inStorageTimeE;

    @ApiModelProperty(value = "出库日期开始")
    private Long outStorageTimeS;

    @ApiModelProperty(value = "出库日期结束")
    private Long outStorageTimeE;

    private String storeId;
}
