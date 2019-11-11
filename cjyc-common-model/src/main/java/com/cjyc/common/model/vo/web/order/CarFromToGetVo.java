package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CarFromToGetVo extends OrderCar {

    @ApiModelProperty("出发地业务中心地址")
    private String startStoreFullAddress;
    @ApiModelProperty("目的地业务中心地址")
    private String endStoreFullAddress;


    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @ApiModelProperty(value = "起始地所属业务中心ID")
    private Long startBelongStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;

    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "目的地所属业务中心ID")
    private Long endBelongStoreId;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "运单车辆状态：0待指派，2已指派，5待装车，15待装车确认，45已装车，70已卸车，90确认交车, 100确认收车, 105待重连，120已重连")
    private Integer state;

    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "取车方式:1上门，2 自送/自取")
    private Integer takeType;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "装车交接类型：1客户家，2中途交接，3业务中心")
    private Integer loadTurnType;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String loadPhotoImg;

    @ApiModelProperty(value = "实际开始装车时间")
    private Long loadTime;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "卸车交接类型：1客户指定地，2中途交接，3业务中心")
    private Integer unloadTurnType;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;


}
