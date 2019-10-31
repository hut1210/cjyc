package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合伙人附带信息
 * </p>
 *
 * @author JPG
 * @since 2019-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_partner")
@ApiModel(value="CustomerPartner对象", description="合伙人附带信息")
public class CustomerPartner implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "合伙人id")
    private Long customerId;

    @ApiModelProperty(value = "是否一般纳税人 0：否  1：是")
    private Integer isTaxpayer;

    @ApiModelProperty(value = "是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty(value = "结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty(value = "账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "企业执照正面")
    private String businessLicenseFrontImg;

    @ApiModelProperty(value = "企业执照反面")
    private String businessLicenseBackImg;

    @ApiModelProperty(value = "法人身份证复印件正面")
    @TableField("legal_idCard_front_img")
    private String legalIdcardFrontImg;

    @ApiModelProperty(value = "法人身份证复印件反面")
    @TableField("legal_idCard_back_img")
    private String legalIdcardBackImg;

    @ApiModelProperty(value = "联系人身份证正面")
    @TableField("linkman_idCard_front_img")
    private String linkmanIdcardFrontImg;

    @ApiModelProperty(value = "联系人身份证反面")
    @TableField("linkman_idCard_back_img")
    private String linkmanIdcardBackImg;

    @ApiModelProperty(value = "授权书正面")
    private String authorizationFrontImg;

    @ApiModelProperty(value = "授权书反面")
    private String authorizationBackImg;


}
