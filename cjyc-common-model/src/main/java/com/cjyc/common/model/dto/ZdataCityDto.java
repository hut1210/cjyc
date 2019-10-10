package com.cjyc.common.model.dto;

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
 * @since 2019-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zdata_city")
@ApiModel(value="ZdataCityDto对象", description="")
public class ZdataCityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "上级行政区编码")
    private String parentCode;

    @ApiModelProperty(value = "上级行政区名称")
    private String parentName;

    @ApiModelProperty(value = "上上级行政区编码")
    private String grandpaCode;

    @ApiModelProperty(value = "上上级行政区名称")
    private String grandpaName;

    @ApiModelProperty(value = "级别")
    private Boolean level;


}
