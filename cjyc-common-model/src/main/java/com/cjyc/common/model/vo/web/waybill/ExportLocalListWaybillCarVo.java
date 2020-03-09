package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExportLocalListWaybillCarVo implements Serializable {

    private static final long serialVersionUID = 2778740402814864979L;
    @Excel(name = "运单车辆状态", orderNum = "0",width = 15 )
    private String outterState;
    @Excel(name = "运单单号", orderNum = "1",width = 15)
    private String waybillNo;
    @Excel(name = "运单类型", orderNum = "2",width = 15)
    private Integer type;
    @Excel(name = "车辆编号", orderNum = "3",width = 20)
    private String orderCarNo;
    @Excel(name = "VIN码", orderNum = "4",width = 15)
    private String vin;
    @Excel(name = "品牌", orderNum = "5",width = 15)
    private String brand;
    @Excel(name = "车系", orderNum = "6",width = 15)
    private String model;
    @Excel(name = "是否新车", orderNum = "7",width = 15)
    private Integer isNew;
    @Excel(name = "运费(元)", orderNum = "8",width = 15)
    private BigDecimal freightFee;
    @Excel(name = "承运类型",orderNum = "9",width = 20)
    private Integer carrierType;
    @Excel(name = "实际交付日期", orderNum = "10",width = 20)
    private Long unloadTime;
    @Excel(name = "承运商名称", orderNum = "11",width = 20)
    private String carrierName;
    @Excel(name = "司机名称", orderNum = "12",width = 20)
    private String driverName;
    @Excel(name = "司机电话", orderNum = "13",width = 20)
    private String driverPhone;
    @Excel(name = "车牌号", orderNum = "14",width = 20)
    private String vehiclePlateNo;
    @Excel(name = "提送车业务中心", orderNum = "15",width = 20)
    private String startStoreName;
    @Excel(name = "提车地址", orderNum = "16",width = 35)
    private String startAddress;
    @Excel(name = "交付地址", orderNum = "17",width = 35)
    private String endAddress;
    @Excel(name = "订单编号", orderNum = "18",width = 20)
    private String orderNo;
    @Excel(name = "提车联系人", orderNum = "19",width = 20)
    private String loadLinkName;
    @Excel(name = "提车人联系电话", orderNum = "20",width = 20)
    private String loadLinkPhone;
    @Excel(name = "交付联系人", orderNum = "21",width = 20)
    private String unloadLinkName;
    @Excel(name = "交付联系电话", orderNum = "22",width = 20)
    private String unloadLinkPhone;
    @Excel(name = "创建时间", orderNum = "23",width = 20)
    private Long createTime;
    @Excel(name = "创建人", orderNum = "24",width = 20)
    private String createUser;

    public String getFreightFee() { return MoneyUtil.fenToYuan(freightFee, MoneyUtil.PATTERN_TWO); }
    public String getUnloadTime(){
        if(unloadTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(unloadTime), TimePatternConstant.DATE);
        }
        return "";
    }
    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getType() {
        //运单类型：1提车运单，2干线运单，3送车运单
        if(type == null){
            return "";
        }
        String str = null;
        switch (type){
            case 1:
                str = "提车运单";break;
            case 2:
                str = "干线运单";break;
            case 3:
                str = "送车运单";break;
        }
        return str;
    }
    public String getIsNew() {
        if(isNew == null){
            return "";
        }
        String str = null;
        switch (isNew){
            case 0:
                str = "否";break;
            case 1:
                str = "是";break;

        }
        return str;
    }
    public String getCarrierType(){
        if(carrierType == null){
            return "";
        }
        String str = null;
        switch (carrierType){
            case 1:
                str = "干线-个人承运商";break;
            case 2:
                str = "干线-企业承运商";break;
            case 3:
                str = "同城-业务员";break;
            case 4:
                str = "同城-代驾";break;
            case 5:
                str = "同城-拖车";break;
            case 6:
                str = "客户自己";break;
        }
        return str;
    }
}
