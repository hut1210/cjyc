package com.cjyc.common.model.dto.salesman.city;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 入参对象
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CityPageDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行政区编码（含大区、省、市、区）")
    private String code;

    @ApiModelProperty(value = "上级城市编码")
    private String parentCode;

    @ApiModelProperty(value = "行政区名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "行政区级别： 0大区， 1省， 2市， 3区县", required = true)
    private Integer level;

    @ApiModelProperty(value = "热门城市： 0否， 1是")
    private Boolean hot;

}
