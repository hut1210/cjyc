package com.cjyc.common.model.dto.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 更新状态实体类
 * @Author LiuXingXiang
 * @Date 2019/11/1 15:11
 **/
@Data
public class OrderUpdateDto implements Serializable {
    private static final long serialVersionUID = -8045045839903456346L;
    public interface CancelAndPlaceOrder{}
    public interface GetDetail{}
    public interface ConfirmPickCar{}

    @ApiModelProperty(value = "车辆id")
    @NotEmpty(groups = {ConfirmPickCar.class},message = "车辆id不能为空")
    private List<Long> carIdList;

    @ApiModelProperty("订单号")
    @NotBlank(groups = {ConfirmPickCar.class,GetDetail.class,CancelAndPlaceOrder.class},message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("客户id")
    @NotNull(groups = {ConfirmPickCar.class,GetDetail.class,CancelAndPlaceOrder.class},message = "客户id不能为空" )
    private Long userId;
}
