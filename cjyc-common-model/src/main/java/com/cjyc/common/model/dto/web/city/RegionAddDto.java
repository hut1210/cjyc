package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 新增大区实体
 * @Author LiuXingXiang
 * @Date 2019/11/7 13:58
 **/
@Data
public class RegionAddDto implements Serializable {
    @ApiModelProperty("大区名称")
    @NotBlank(message = "大区名称不能为空")
    private String regionName;

    @ApiModelProperty("省编码列表")
    private List<String> provinceCodeList;

    @ApiModelProperty("备注")
    private String remark;
}
