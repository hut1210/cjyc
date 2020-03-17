package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 订单车辆信息
 * @Author Liu Xing Xiang
 * @Date 2020/3/12 11:40
 **/
@Data
public class OrderCarSubmitReqDto implements Serializable {
    private static final long serialVersionUID = -3441938847460577267L;

    @NotBlank(message = "品牌不能为空")
    @ApiModelProperty(value = "品牌", required = true)
    private String brand;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号", required = true)
    private String model;

    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "车牌号格式不正确，请检查")
    @ApiModelProperty(value = "车牌号", required = true)
    private String plateNo;

    //@Pattern(regexp = "(^$)|(^[0-9a-zA-Z]{1,20}$)", message = "vin码格式不正确，请检查")
    @ApiModelProperty(value = "vin码")
    private String vin;

    @NotNull(message = "是否能动标识不能为空")
    @ApiModelProperty(value = "是否能动 0-否 1-是", required = true)
    private Integer isMove;

    @NotNull(message = "是否新车标识不能为空")
    @ApiModelProperty(value = "是否新车 0-否 1-是", required = true)
    private Integer isNew;

    @ApiModelProperty(value = "车值/万")
    private int valuation;

    @ApiModelProperty(value = "保险费/元，保险费计算规则：车值<=10万：保险费0元；11万<=车值<=19万：保险费50元；20万<=车值<=29万：保险费10元,以此类推")
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "车辆应收干线费 单位：分",hidden = true)
    private BigDecimal trunkFee;
}
