package com.cjyc.common.model.dto.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/10/09 15:43
 */
@Data
public class WayBillCarrierDto {

    private long Id;//ID

    private String waybillNo;//运单编号

    private String state;//运单状态

    private Integer type;//类型 1提车运单，2送车运单，8干线运单

    private String carNo;//车辆编码

    private String vin;//vin码

    private String brand;//品牌

    private String  model;//型号

    private BigDecimal freightFee;//运费

    private Integer pickType;//提车方式

    private Integer back_type;//送车方式

    private String deliveryWay;//运载方式

    private long expectStartDate;//提车日期

    private String expectStartDateStr;//提车日期

    private long loadTime;//实际提车日期

    private String loadTimeStr;//实际提车日期

    private long unLoadTime;//实际提车日期

    private String unLoadTimeStr;//实际提车日期

    private long carrierId;//承运商Id

    private String carrierName;//承运商名字

    private String driverName;//司机名字

    private long driverId;//司机Id

    private String phone;//司机电话

    private String vehicleNo;//车牌号

    private String idCard;//身份证号

    private String storeName;//提送车业务中心

    private String startAddress;//出发地详细地址

    private String endAddress;//目的地详细地址

    private String orderNo;//订单编号

    private String pickContactName;//发车人

    private String pickContactPhone;//发车人联系方式

    private String backContactName;//收车人

    private String backContactPhone;//发车人联系方式

    private long createTime;//创建时间

    private String createTimeStr;//创建时间

    private String createUserName;//创建人

}
