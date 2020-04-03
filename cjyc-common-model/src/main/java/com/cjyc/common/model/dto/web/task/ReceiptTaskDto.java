package com.cjyc.common.model.dto.web.task;

import com.cjyc.common.model.enums.ClientEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ReceiptTaskDto extends BaseTaskDto {

    @ApiModelProperty(value = "客户端类型：1WEB管理后台, 2业务员APP, 3业务员小程序, 4司机APP, 5司机小程序, 6用户端APP, 7用户端小程序", hidden = true)
    private ClientEnum clientEnum;

}
