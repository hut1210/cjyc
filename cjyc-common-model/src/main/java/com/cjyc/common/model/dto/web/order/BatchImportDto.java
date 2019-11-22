package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchImportDto {

    @NotNull(message = "clientId不能为空")
    @ApiModelProperty(value = "1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", required = true)
    private int clientId;
    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "操作人id", required = true)
    private Long loginId;
    @ApiModelProperty(value = "操作人")
    private Long userName;
    @NotNull(message = "createCustomerFlag不能为空")
    @ApiModelProperty(value = "用户不存在，是否创建用户",required = true)
    private Boolean createCustomerFlag;
    /**车辆列表*/
    private List<CommitOrderCarDto> orderCarList;

    @ApiModelProperty(value = "1C端 2大客户 3-伙人")
    private int customerType;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @ApiModelProperty(value = "客户电话")
    private String customerPhone;
    @NotBlank
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "订单列表")
    private List<BatchImportOrderDto> list;



    @ApiModelProperty(value = "订单所属业务中心ID")
    private Long inputStoreId;
    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

}
