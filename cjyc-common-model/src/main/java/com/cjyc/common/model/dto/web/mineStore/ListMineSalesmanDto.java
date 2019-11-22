package com.cjyc.common.model.dto.web.mineStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 我的业务中心-我的业务员列表请求信息
 */

@Data
@ApiModel
@Validated
public class ListMineSalesmanDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "业务中心标识不能为空")
    @ApiModelProperty(value = "业务中心标识", required = true)
    private Long storeId;
    @ApiModelProperty(value = "业务员电话")
    private String phone;
    @ApiModelProperty(value = "业务员名称")
    private String name;
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
}
