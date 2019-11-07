package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 承运商信息表（个人也算承运商）
 * </p>
 *
 * @author JPG
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier")
@ApiModel(value="Carrier对象", description="承运商信息表（个人也算承运商）")
public class Carrier implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long deptId;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty(value = "法人姓名")
    private String legalName;

    @ApiModelProperty(value = "法人身份证号")
    private String legalIdCard;

    @ApiModelProperty(value = "公司联系人")
    private String linkman;

    @ApiModelProperty(value = "公司联系人手机号")
    private String linkmanPhone;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线   4：拖车   5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;

    @ApiModelProperty(value = "营业执照正面")
    private String busLicenseFrontImg;

    @ApiModelProperty(value = "营业执照反面")
    private String busLicenseBackImg;

    @ApiModelProperty(value = "道路运输许可证正面照片")
    private String transportLicenseFrontImg;

    @ApiModelProperty(value = "道路运输许可证反面照片")
    private String transportLicenseBackImg;

    @ApiModelProperty(value = "银行开户证明正面")
    private String bankOpenFrontImg;

    @ApiModelProperty(value = "银行开户证明反面")
    private String bankOpenBackImg;

    @ApiModelProperty(value = "管理员数量")
    private Integer adminNum;

    @ApiModelProperty(value = "默认管理员ID")
    private Long defaultAdminId;

    @ApiModelProperty(value = "司机数量")
    private Integer driverNum;

    @ApiModelProperty(value = "运营中的司机数量")
    private Integer runningDriverNum;

    @ApiModelProperty(value = "结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty(value = "账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "结算公司：0韵车，1otm，2掌控")
    private Integer settleCorporation;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4取消，5冻结， 7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "营运状态：0营运中，1停运中")
    private Integer businessState;

    @ApiModelProperty(value = "是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty(value = "创建者id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "操作人id")
    private Long operateUserId;

    @ApiModelProperty(value = "操作时间")
    private Long operateTime;

    @ApiModelProperty(value = "操作人")
    private String operateName;


}
