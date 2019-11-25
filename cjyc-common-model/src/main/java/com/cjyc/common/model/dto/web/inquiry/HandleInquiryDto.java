package com.cjyc.common.model.dto.web.inquiry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class HandleInquiryDto implements Serializable {
    private static final long serialVersionUID = 105485378572294434L;

    @ApiModelProperty(value = "询价id",required = true)
    @NotNull(message = "询价id不能为空")
    private Long id;

    @ApiModelProperty(value = "处理人id",required = true)
    @NotNull(message = "处理人id不能为空")
    private Long handleUserId;

    @ApiModelProperty(value = "工单内容",required = true)
    @NotBlank(message = "工单内容不能为空")
    private String jobContent;

}