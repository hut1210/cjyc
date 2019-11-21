package com.cjyc.common.model.dto.driver;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 司机列表查询dto
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 17:19
 **/
@Data
public class DriverQueryDto extends BasePageDto {
    private static final long serialVersionUID = 1861114501976166079L;
    @ApiModelProperty(value = "承运商ID")
    @NotNull(message = "承运商ID不能为空")
    private Long carrierId;

    @ApiModelProperty("关键字：姓名，电话，车牌")
    private String keyword;
}
