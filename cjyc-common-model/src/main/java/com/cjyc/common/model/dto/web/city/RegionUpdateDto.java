package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 大区修改实体参数
 * @Author LiuXingXiang
 * @Date 2019/11/7 17:28
 **/
@Data
public class RegionUpdateDto implements Serializable {
    private static final long serialVersionUID = -6894490899772730452L;
    @ApiModelProperty("大区名称")
    @NotBlank(message = "大区名称不能为空")
    private String regionName;

    @ApiModelProperty("大区编码")
    @NotBlank(message = "大区编码不能为空")
    private String regionCode;

    @ApiModelProperty("覆盖省列表")
    private List<@Valid RegionCityDto> provinceList;

    @ApiModelProperty("备注")
    private String remark;
}
