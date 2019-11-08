package com.cjyc.common.model.dto.web.city;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 大区查询实体
 * @Author LiuXingXiang
 * @Date 2019/11/7 11:52
 **/
@Data
public class RegionQueryDto extends BasePageDto {
    private static final long serialVersionUID = 5294385347980922339L;
    @ApiModelProperty("大区名称")
    private String regionName;
}
