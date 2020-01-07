package com.Pingxx.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/01/02 10:21
 **/
@Data
public class PingxxMetaData {

    private String channel;

    private String orderNo; //平台订单编号

    private String chargeType;

    private String clientType;	//客户端类型 customer:用户端	driver:司机端    user:业务员端  web:后台

    private String pingAppId;

    private List<String> orderCarIds;

    private String taskId;

    private List<String>  taskCarIdList;

    /**当前app登陆人的Id*/
    private String loginId;
    /**当前app登陆人的名称*/
    private String loginName;
    /**当前app登陆人的类型*/
    private String loginType;

    private Long waybillId;
}
