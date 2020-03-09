package com.cjyc.common.model.vo.web.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class OrderCarWaitDispatchVo extends OrderCar {
    private String dispatchStartProvinceCode;
    private String dispatchStartProvince;
    private String dispatchStartCityCode;
    private String dispatchStartCity;
    private String dispatchStartAreaCode;
    private String dispatchStartArea;
    private String dispatchEndProvinceCode;
    private String dispatchEndProvince;
    private String dispatchEndCityCode;
    private String dispatchEndCity;
    private String dispatchEndAreaCode;
    private String dispatchEndArea;

    @ApiModelProperty(value = "目的业务中心地址")
    private String startFullAddress;
    @ApiModelProperty(value = "目的业务中心地址")
    private String endFullAddress;

    @ApiModelProperty(value = "始发业务中心地址")
    @Excel(name = "提车业务中心地址", orderNum = "24",width = 20)
    private String startStoreFullAddress;
    @ApiModelProperty(value = "大区编码")
    private String regionCode;
    @ApiModelProperty(value = "大区")
    @Excel(name = "大区", orderNum = "22",width = 15)
    private String region;
    @ApiModelProperty(value = "来源")
    private Integer source;
    @ApiModelProperty("出发地")
    private String startStoreAreaCode;
    @ApiModelProperty("出发地")
    private String startStoreAddress;
    @ApiModelProperty("目的地业务中心code")
    private String endStoreAreaCode;
    @ApiModelProperty("目的地址")
    private String endStoreAddress;


    @ApiModelProperty(value = "目的业务中心地址")
    @Excel(name = "送车业务中心地址", orderNum = "26",width = 20)
    private String endStoreFullAddress;

    @ApiModelProperty(value = "目的业务中心地址")
    @Excel(name = "当前归属地", orderNum = "19",width = 20)
    private String nowCityName;

    @ApiModelProperty("提车运输状态")
    private Integer pickTransportState;
    @ApiModelProperty("干线运输状态")
    private Integer trunkTransportState;
    @ApiModelProperty("送车运输状态")
    private Integer backTransportState;

    @ApiModelProperty(value = "订单所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inputStoreId;

    @ApiModelProperty(value = "订单所属业务中心名称")
    private String inputStoreName;

    @ApiModelProperty(value = "省")
    private String startProvince;

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市")
    @Excel(name = "订单始发地", orderNum = "3",width = 15)
    private String startCity;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区")
    private String startArea;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地经度")
    private String startLng;

    @ApiModelProperty(value = "出发地纬度")
    private String startLat;

    @ApiModelProperty(value = "出发地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startStoreId;
    @ApiModelProperty(value = "出发地业务中心名称")
    @Excel(name = "提车业务中心", orderNum = "23",width = 20)
    private String startStoreName;
    @ApiModelProperty(value = "出发地业务所属中心名称")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long startBelongStoreId;

    @ApiModelProperty(value = "省")
    private String endProvince;

    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "市")
    @Excel(name = "订单目的地", orderNum = "4",width = 15)
    private String endCity;

    @ApiModelProperty(value = "市编号")
    private String endCityCode;

    @ApiModelProperty(value = "区")
    private String endArea;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地经度")
    private String endLng;

    @ApiModelProperty(value = "目的地纬度")
    private String endLat;

    @ApiModelProperty(value = "目的地业务中心ID: -1不经过业务中心")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endStoreId;

    @ApiModelProperty(value = "目的地业务中心名称")
    @Excel(name = "送车业务中心", orderNum = "25")
    private String endStoreName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long endBelongStoreId;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndDate;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "确认时间")
    private Long checkTime;
    @ApiModelProperty(value = "确认人手机号")
    private Long checkUserPhone;

    @ApiModelProperty(value = "确认人：业务员")
    @Excel(name = "接单人", orderNum = "30",width = 15)
    private String checkUserName;

    @ApiModelProperty(value = "确认人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long checkUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人：客户/业务员")
    @Excel(name = "下单人", orderNum = "28",width = 15)
    private String createUserName;

    @ApiModelProperty(value = "创建人userid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    private String customerName;

    private String customerPhone;

    private Integer customerType;

    @Excel(name = "车辆状态", orderNum = "0",width = 15)
    private String carStateStr;
    @Excel(name = "提车方式", orderNum = "1",width = 15)
    private String pickTypeStr;
    @Excel(name = "送车方式", orderNum = "2",width = 15)
    private String backTypeStr;
    @Excel(name = "是否新车", orderNum = "7",width = 15)
    private String isNewStr;
    @Excel(name = "是否能动", orderNum = "8",width = 15)
    private String isMoveStr;
    @Excel(name = "订单来源", orderNum = "21",width = 15)
    private String sourceStr;
    @Excel(name = "下单时间", orderNum = "27",width = 20)
    private String expectStartDateStr;
    @Excel(name = "接单时间", orderNum = "29",width = 20)
    private String expectEndDateStr;


    public String getExpectEndDateStr() {
        Long date = getExpectEndDate();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.COMPLEX_TIME_FORMAT);
    }

    public String getExpectStartDateStr() {
        Long date = getExpectStartDate();
        if(null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), TimePatternConstant.COMPLEX_TIME_FORMAT);
    }

    public String getSourceStr() {
        Integer source = getSource();
        if (null == source) {
            return "";
        }
        String str = "";
        switch (source) {
            case 1: str = "韵车后台"; break;
            case 2: str = "业务员APP"; break;
            case 4: str = "司机APP"; break;
            case 6: str = "用户端APP"; break;
            case 7: str = "用户端小程序"; break;
        }
        return str;
    }

    public String getIsMoveStr() {
        Integer isMove = getIsMove();
        if (null == isMove) {
            return "未知";
        }else if (isMove.equals(1)) {
            return "是";
        }else if(isMove.equals(0)) {
            return "否";
        }else {
            return "未知";
        }
    }

    public String getIsNewStr() {
        Integer isNew = getIsNew();
        if (null == isNew) {
            return "未知";
        }else if (isNew.equals(1)) {
            return "是";
        }else if(isNew.equals(0)) {
            return "否";
        }else {
            return "未知";
        }
    }

    public String getBackTypeStr() {
        Integer backType = getBackType();
        if (null == backType) {
            return "";
        }
        String str = "";
        switch (backType) {
            case 1:
                str = "自提"; break;
            case 2:
                str = "代驾上门"; break;
            case 3:
                str = "拖车上门"; break;
            case 4:
                str = "物流上门"; break;
        }
        return str;
    }

    public String getPickTypeStr() {
        Integer pickType = getPickType();
        if (null == pickType) {
            return "";
        }
        String str = "";
        switch (pickType) {
            case 1:
                str = "自送"; break;
            case 2:
                str = "代驾上门"; break;
            case 3:
                str = "拖车上门"; break;
            case 4:
                str = "物流上门"; break;
        }
        return str;
    }

    public String getCarStateStr() {
        Integer state = getState();
        if (null == state) {
            return "";
        }
        String str = "";
        switch (state) {
            case 0: str = "待调度"; break;
            case 5: str = "待提车调度"; break;
            case 10: str = "待提车"; break;
            case 12: str = "待自送交车"; break;
            case 15: str = "提车中"; break;
            case 25: str = "待干线调度"; break;
            case 35: str = "待干线提车"; break;
            case 40: str = "干线中"; break;
            case 45: str = "待配送调度"; break;
            case 50: str = "待配送提车"; break;
            case 55: str = "配送中"; break;
            case 70: str = "待自取提车"; break;
            case 100: str = "已签收"; break;
        }
        return str;
    }
}
