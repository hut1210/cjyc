package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 大区vo
 * @Author LiuXingXiang
 * @Date 2019/11/7 13:29
 **/
@Data
public class RegionVo implements Serializable {
    private static final long serialVersionUID = -8907210911560768082L;
    @ApiModelProperty("大区编码")
    private String regionCode;

    @ApiModelProperty("大区名称")
    private String regionName;

    @ApiModelProperty("包含省分数量")
    private Integer provinceCount;
}
