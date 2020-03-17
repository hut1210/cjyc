package com.cjyc.common.model.entity;

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
 * 银行卡绑定信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@Accessors(chain = true)
@TableName("s_bank_card_bind")
@ApiModel(value="BankCardBind对象", description="银行卡绑定信息表")
public class BankCardBind implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "绑卡人员ID")
    private Long userId;

    @ApiModelProperty(value = "绑卡人员类型")
    private Integer userType;

    @ApiModelProperty(value = "卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty(value = "颜色类型 1：红色 2：蓝色 3：绿色")
    private Integer cardColour;

    @ApiModelProperty(value = "省直辖市名称")
    private String provinceName;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "银行卡户主")
    private String cardName;

    @ApiModelProperty(value = "银行卡卡号")
    private String cardNo;

    @ApiModelProperty(value = "银行卡留存电话")
    private String cardPhone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "开户银行编号")
    private String bankCode;

    @ApiModelProperty(value = "开户银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行开户许可证")
    private String bankLicence;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态：0不可用，1可用")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
