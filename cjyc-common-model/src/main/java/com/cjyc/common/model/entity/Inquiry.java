package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户询价
 * </p>
 *
 * @author JPG
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_inquiry")
@ApiModel(value="Inquiry对象", description="客户询价")
public class Inquiry implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "询价ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户姓名")
    private String name;

    @ApiModelProperty(value = "客户手机号")
    private String phone;

    @ApiModelProperty(value = "始发地")
    private String startCity;

    @ApiModelProperty(value = "始发地编码")
    private String startCode;

    @ApiModelProperty(value = "目的地")
    private String endCity;

    @ApiModelProperty(value = "目的地编码")
    private String endCode;

    @ApiModelProperty(value = "运费(上游物流费)(分)")
    private BigDecimal logisticsFee;

    @ApiModelProperty(value = "上门提车费(分)")
    private BigDecimal pickFee;

    @ApiModelProperty(value = "送车费(分)")
    private BigDecimal backFee;

    @ApiModelProperty(value = "处理状态 1：未处理  2：已处理")
    private Integer state;

    @ApiModelProperty(value = "询价时间")
    private Long inquiryTime;

    @ApiModelProperty(value = "处理时间")
    private Long handleTime;

    @ApiModelProperty(value = "处理人")
    private Long handleUserId;

    @ApiModelProperty(value = "工单内容")
    private String jobContent;


}
