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
 * 司机与区县绑定
 * </p>
 *
 * @author JPG
 * @since 2019-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_driver_city_con")
@ApiModel(value="DriverCityCon对象", description="司机与区县绑定")
public class CarrierCityCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "司机ID")
    private Long carrierId;

    @ApiModelProperty(value = "全国code")
    private String countryCode;

    @ApiModelProperty(value = "大区code")
    private String largeAreaCode;

    @ApiModelProperty(value = "省/直辖市code")
    private String provinceCode;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "区县编码  0 : 全国")
    private String areaCode;


}
