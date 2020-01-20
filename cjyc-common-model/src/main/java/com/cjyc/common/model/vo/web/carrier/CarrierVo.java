package com.cjyc.common.model.vo.web.carrier;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarrierVo implements Serializable {

    private static final long serialVersionUID = 1152187218002996582L;
    @ApiModelProperty("承运商id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("承运商部门id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long deptId;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("公司联系人")
    private String linkman;

    @ApiModelProperty("公司联系人手机号")
    private String linkmanPhone;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人身份证号")
    private String legalIdCard;

    @ApiModelProperty("结算类型 0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("账期时间/天")
    private Integer settlePeriod;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车   5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty("卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty("持卡人名称")
    private String cardName;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("营业执照正面")
    private String busLicenseFrontImg;

    @ApiModelProperty("营业执照反面")
    private String busLicenseBackImg;

    @ApiModelProperty("道路运输许可证正面照片")
    private String transportLicenseFrontImg;

    @ApiModelProperty("道路运输许可证反面照片")
    private String transportLicenseBackImg;

    @ApiModelProperty("银行开户证明正面")
    private String bankOpenFrontImg;

    @ApiModelProperty("银行开户证明反面")
    private String bankOpenBackImg;

    @ApiModelProperty("总运输台数")
    private Integer carNum;

    @ApiModelProperty("总收入")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalIncome;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，5冻结， 7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty("最后操作时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long operateTime;

    @ApiModelProperty("最后操作人")
    private String operateName;
}