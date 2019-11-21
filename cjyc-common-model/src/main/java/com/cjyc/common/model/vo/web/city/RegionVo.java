package com.cjyc.common.model.vo.web.city;

import com.cjyc.common.model.entity.City;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 大区vo
 * @Author LiuXingXiang
 * @Date 2019/11/7 13:29
 **/
@Data
public class RegionVo extends City {
    private static final long serialVersionUID = -8907210911560768082L;
    @ApiModelProperty("包含省分数量")
    private Integer provinceCount;
}
