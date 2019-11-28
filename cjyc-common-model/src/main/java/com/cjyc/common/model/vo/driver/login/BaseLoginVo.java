package com.cjyc.common.model.vo.driver.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BaseLoginVo extends DriverLoginVo implements Serializable {
    private static final long serialVersionUID = 1212783695647256969L;
    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结  7已驳回，9已停用（CommonStateEnum")
    private Integer carrierState;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结  7已驳回，9已停用（CommonStateEnum")
    private Integer state;

}