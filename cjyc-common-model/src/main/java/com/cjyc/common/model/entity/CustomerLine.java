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
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_line")
@ApiModel(value="CustomerLine对象", description="")
public class CustomerLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "班线编号")
    private String lineCode;

    @ApiModelProperty(value = "起始地址")
    private String startAdress;

    @ApiModelProperty(value = "起始地联系人")
    private String startContact;

    @ApiModelProperty(value = "起始地联系人电话")
    private String startContactPhone;

    @ApiModelProperty(value = "目的地地址")
    private String endAdress;

    @ApiModelProperty(value = "目的地联系人")
    private String endContact;

    @ApiModelProperty(value = "目的地联系人电话")
    private String endContactPhone;

    private Long createTime;


}
