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
 * 字典配置表
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_dictionary")
@ApiModel(value="Dictionary对象", description="字典配置表")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "条目")
    private String item;

    @ApiModelProperty(value = "键")
    private String itemKey;

    @ApiModelProperty(value = "值")
    private String itemValue;

    @ApiModelProperty(value = "单位")
    private String itemUnit;

    @ApiModelProperty(value = "是否固定值：1不可修改，0可修改")
    private Integer fixedFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：0无效，1有效")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
