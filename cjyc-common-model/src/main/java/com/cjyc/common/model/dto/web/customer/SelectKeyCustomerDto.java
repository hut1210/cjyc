package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectKeyCustomerDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = -3635364908902130729L;
    @ApiModelProperty(value = "标志 0:订单 1:客户")
    private Integer flag;

    @ApiModelProperty(value = "大客户编号")
    private String customerNo;

    @ApiModelProperty(value = "大客户全称")
    private String name;

    @ApiModelProperty(value = "状态：0待审核，1未登录，2已审核，3审核拒绝，7已冻结")
    private Integer state;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty("客户类型  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty(value = "创建人")
    private String createUserName;
}