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
 * 业务中心配置表
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_store_conf")
@ApiModel(value="StoreConf对象", description="业务中心配置表")
public class StoreConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务中心ID")
    private Long storeId;

    @ApiModelProperty(value = "配置条目")
    private String item;

    @ApiModelProperty(value = "键")
    private String itemKey;

    @ApiModelProperty(value = "值")
    private Integer itemValue;

    @ApiModelProperty(value = "备注")
    private String rewark;


}
