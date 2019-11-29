package com.cjyc.common.model.dto.web.salesman;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class TypeSalesmanDto extends BasePageDto {
    private static final long serialVersionUID = -9045863844526822893L;

    @ApiModelProperty("是否分页 0:不分页 1:分页")
    @NotNull(message = "是否分页必须传")
    private Integer isPage;

    @ApiModelProperty("业务员姓名")
    private String name;

    @ApiModelProperty("手机号")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "电话号码格式不对")
    private String phone;

    @ApiModelProperty("所属业务中心")
    private Long storeId;
}