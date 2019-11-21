package com.cjyc.common.model.dto.web.inquiry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class HandleInquiryDto implements Serializable {
    private static final long serialVersionUID = 105485378572294434L;

    @ApiModelProperty("询价id")
    @NotNull(message = "询价id不能为空")
    private Long id;

    @ApiModelProperty("处理人id")
    @NotNull(message = "处理人id不能为空")
    private Long handleUserId;

    @ApiModelProperty("工单内容")
    @NotBlank(message = "工单内容不能为空")
    private String jobContent;

}