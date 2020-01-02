package com.cjyc.common.model.dto.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 查询车辆明细参数
 * @Author Liu Xing Xiang
 * @Date 2020/1/2 13:10
 **/
@Data
public class CarDetailDto implements Serializable {
    private static final long serialVersionUID = -1886205956262936071L;
    @ApiModelProperty(value = "车辆编号")
    @NotBlank(message = "车辆编号不能为空")
    private String carNo;
}
