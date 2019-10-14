package com.cjyc.salesman.api.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @auther litan
 * @description: 订单入参实体类
 * @date:2019/10/8
 */
@Data
public class OrderDto implements Serializable {

    public interface SimpleOrderVali {
    }
    public interface DetailOrderVali {
    }

    private int saveType;//0-保存（草稿） 1-下单
    private int isSimple;//0-详单 1-简单
    private String no;
    private String customerId;
    private String customerName;

    @NotBlank(groups = {SimpleOrderVali.class,DetailOrderVali.class},message = "始发城市code不能为空")
    private String startProvince;

    private String startProvinceName;
    @NotBlank(groups = {DetailOrderVali.class},message = "始发城市code不能为空")
    private String startCity;
    private String startCityCode;
    private String startArea;
    private String startAreaCode;
    private String startLng;
    private String startLat;
    private String endProvince;
    private String endProvinceName;
    private String endCity;
    private String endCityCode;
    private String endArea;
    private String endAreaCode;
    private String endLng;
    private String endLat;
    private String expectStartDate;
    private int carNum;
    private String lineId;
    private int pickType;
    private String pickContactName;
    private String getPickContactPhone;
    private int backType;
    private String backContactName;
    private String backPickContactPhone;
    private int source;
    private int createTime;
    private String createUserName;
    private int createUserType;
    private int checkTime;
    private String checkSalesmanName;
    private int checkSalesmanId;
    private int state;
    private String remark;
    private int invoiceFlag;
    private int invoiceType;
    private int pickFee;
    private int trunkFee;
    private int backFee;
    private int insuranceFee;
    private int depositFee;
    private int agencyFee;
    private int totalFee;
    private int feeShareType;//车辆均摊费用（提车费/送车费/中转费）方式：0均分余数散列（默认），1不均分
    private int customerContractId;//合同ID
    private int customerPayType;//客户付款方式：0时付（默认），1账期
    private int wlPayState;//客户支付尾款状态：0未支付，1部分支付，2支付完成
    private int wlPayTime;//上次客户支付尾款时间
    private int offlinePayFlag;//线下收款标识：默认0（不允许），
    private List<OrderCarDto> orderCarDtoList;


}
