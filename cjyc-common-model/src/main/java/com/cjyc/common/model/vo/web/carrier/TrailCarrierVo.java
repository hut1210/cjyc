package com.cjyc.common.model.vo.web.carrier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class TrailCarrierVo implements Serializable {
    private static final long serialVersionUID = -4994289044268667924L;

    @ApiModelProperty("承运商id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("企业名称")
    private String businessName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("承运数")
    private Integer carryCarNum;

    @ApiModelProperty("非空车位")
    private Integer occupiedCarNum;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车   5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;

    @ApiModelProperty("运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;

    @ApiModelProperty("营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    public Long getCarrierId(){return carrierId == null ? 0:carrierId;}
    public Integer getType(){return type == null ? 0:type;}
    public String getName(){return StringUtils.isBlank(name) ? "":name;}
    public String getBusinessName(){return StringUtils.isBlank(businessName) ? "":businessName;}
    public String getPhone(){return StringUtils.isBlank(phone) ? "":phone;}
    public String getIdCard(){return StringUtils.isBlank(idCard) ? "":idCard;}
    public String getPlateNo(){return StringUtils.isBlank(plateNo) ? "":plateNo;}
    public Integer getCarryCarNum(){return carryCarNum == null ? 0:carryCarNum;}
    public Integer getOccupiedCarNum(){return occupiedCarNum == null ? 0:occupiedCarNum;}
    public Integer getRunningState(){return runningState == null ? 0:runningState;}
    public Integer getMode(){return mode == null ? 0 : mode;}

}