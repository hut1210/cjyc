package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
@Validated
public class SaveLocalDto {

    @NotNull(message = "登录人不能为空")
    @ApiModelProperty(value = "用户Id", required = true)
    private Long loginId;

    @ApiModelProperty(hidden = true)
    private String loginName;

    @NotNull(message = "调度类型不能为空")
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单", required = true)
    private Integer type;

    @NotEmpty(message = "调度内容不能为空")
    @ApiModelProperty(value = "调度运单列表", required = true)
    @Valid
    private List<SaveLocalWaybillDto> list;
}
