package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class TrunkListWaybillCarDto extends BasePageDto {

    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    private Long loginId;
    @ApiModelProperty(value = "业务范围(不用传)")
    private Set<Long> bizScope;


    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "订单来源：1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序")
    private String orderSource;
    @ApiModelProperty(value = "省编号")
    private String orderStartProvinceCode;
    @ApiModelProperty(value = "市编号")
    private String orderStartCityCode;
    @ApiModelProperty(value = "区编号")
    private String orderStartAreaCode;
    @ApiModelProperty(value = "省编号")
    private String orderEndProvinceCode;
    @ApiModelProperty(value = "市编号")
    private String orderEndCityCode;
    @ApiModelProperty(value = "区编号")
    private String orderEndAreaCode;


    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;

    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 0待指派
     * 2已指派        2待提车
     * 5待装车        2待提车
     * 15待装车确认   2待提车
     * 45已装车       3已提车
     * 70已卸车       3已提车
     * 90确认交车      3已提车
     * 100确认收车     4已交付
     * 105待重连
     * 120已重连
     */
    @ApiModelProperty(value = "0全部，2待提车，3已提车，4已交付")
    private Integer outterState;


    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "省编码")
    private String startProvinceCode;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;


    @ApiModelProperty(value = "省编码")
    private String endProvinceCode;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;


    @ApiModelProperty(value = "预计提车日期")
    private Long beginExpectStartTime;
    @ApiModelProperty(value = "预计提车日期")
    private Long endExpectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long beginExpectEndTime;
    @ApiModelProperty(value = "预计到达时间")
    private Long endExpectEndTime;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "起始实际开始装车时间")
    private Long beginLoadTime;
    @ApiModelProperty(value = "截止实际开始装车时间")
    private Long endLoadTime;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "起始实际完成卸车时间")
    private Long beginUnloadTime;
    @ApiModelProperty(value = "截止实际完成卸车时间")
    private Long endUnloadTime;

    @ApiModelProperty(value = "创建时间")
    private Long beginCreateTime;
    @ApiModelProperty(value = "创建时间")
    private Long endCreateTime;

    @ApiModelProperty(value = "当前所在业务中心ID")
    private Long nowStoreId;

}
