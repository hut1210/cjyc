package com.cjyc.common.model.vo.web.customer;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.SecondLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ListKeyCustomerVo implements Serializable {

    private static final long serialVersionUID = 2101433849725660997L;
    @ApiModelProperty(value = "大客户id(customerId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "大客户userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("大客户编号")
    private String customerNo;

    @ApiModelProperty(value = "大客户客户全称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "客户类型  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty("状态：状态  0：待审核 1：审核中  2:已审核 4：已取消  5：已冻结  7：已拒绝   9:停用")
    private Integer state;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("总单量")
    private Integer totalOrder;

    @ApiModelProperty("总运车量")
    private Integer totalCar;

    @ApiModelProperty("订单总金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "注册时间")
    @JsonSerialize(using = SecondLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty("创建人名称")
    private String createUserName;
}