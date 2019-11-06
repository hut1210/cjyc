package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 业务中心区域查询参数类
 * @Author LiuXingXiang
 * @Date 2019/11/6 15:52
 **/
@Data
public class StoreAreaQueryDto implements Serializable {
    private static final long serialVersionUID = 9177744066955537372L;
    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区编码")
    private String areaCode;

    @ApiModelProperty(value = "业务中心ID")
    @NotNull(message = "业务中心ID不能为空")
    private Long storeId;
}
