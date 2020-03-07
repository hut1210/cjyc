package com.cjyc.common.model.dto.web.salesman;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class TypeSalesmanDto extends BasePageDto {
    private static final long serialVersionUID = -9045863844526822893L;

    @ApiModelProperty("是否分页 0:不分页 1:分页")
    @NotNull(message = "是否分页必须传")
    private Integer isPage;

    @ApiModelProperty("业务员姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("业务中心名称")
    private String storeName;

    @ApiModelProperty("提送车业务中心id")
    private Long pickBackStoreId;

}