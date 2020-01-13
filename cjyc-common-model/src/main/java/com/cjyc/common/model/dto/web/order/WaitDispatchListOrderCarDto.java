package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.line.FromToCityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 入参
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class WaitDispatchListOrderCarDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long loginId;

    @ApiModelProperty(value = "用户ID")
    private Long roleId;

    @ApiModelProperty(value = "调度起始城市")
    private String dispatchFromCityCode;
    @ApiModelProperty(value = "调度目的城市")
    private String dispatchToCityCode;

    @ApiModelProperty(value = "调度类型：0全部，1待提车，2待干线，3待配送")
    private Long dispatchType;

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String endCityCode;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编码")
    private String carNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "预计出发时间（提车日期）开始")
    private Long beginExpectStartDate;

    @ApiModelProperty(value = "预计出发时间（提车日期）结束")
    private Long endExpectStartDate;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门")
    private Integer pickType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门")
    private Integer backType;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心,0无业务中心")
    private Long startStoreId;

    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心,0无业务中心")
    private Long endStoreId;

    @ApiModelProperty(value = "签收状态：0全部，1未签收，2已签收")
    private Long signState;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "车牌号")
    private Integer source;

    @ApiModelProperty(value = "起始目的城市列表")
    private List<FromToCityDto> fromToCityList;

    @ApiModelProperty(value = "业务范围(不用传)")
    private Set<Long> bizScope;

}
