package com.cjyc.common.model.vo.web.admin;

import com.cjyc.common.model.entity.Admin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CacheAdminVo extends Admin {

    @ApiModelProperty("业务中心ID")
    private Long storeId;

}
