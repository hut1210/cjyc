package com.cjyc.common.model.vo.salesman.login;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 业务员vo
 */

@Data
@ApiModel
@Accessors(chain = true)
public class AdminVo implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "user_id(查询架构组数据时使用)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "编号")
    private String no;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "座机号")
    private String tel;

    @ApiModelProperty(value = "职位：1提送车业务员，2业务员，3调度员，4客服，81财务")
    private Integer type;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "性别: 0女，1男")
    private Integer sex;

    @ApiModelProperty(value = "上级ID")
    private Long leaderId;

    @ApiModelProperty(value = "所属业务中心ID")
    private Long storeId;

    @ApiModelProperty(value = "头像")
    private String photoImg;

    @JsonSerialize(using = DateLongSerizlizer.class)
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "创建人")
    private String createUser;
    @ApiModelProperty(value = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "审核人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long checkUserId;

    @JsonSerialize(using = DateLongSerizlizer.class)
    @ApiModelProperty(value = "入职时间")
    private Long hireTime;

    @JsonSerialize(using = DateLongSerizlizer.class)
    @ApiModelProperty(value = "离职时间")
    private Long leaveTime;

    @ApiModelProperty(value = "资金账户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    @ApiModelProperty(value = "1Web，2App")
    private String clientType;

    @ApiModelProperty(value = "业务范围类型:0无数据权限，1业务中心数据权限，2行政区域数据权限")
    private Integer bizScope;

    @ApiModelProperty(value = "业务范围描述信息")
    private String bizDesc;

    public Long getUserId() {
        return this.userId == null ?-1L: this.userId;
    }

    public String getNo() {
        return StringUtils.isEmpty(no)?"": this.no;
    }

    public String getName() {
        return StringUtils.isEmpty(name)?"": name;
    }

    public String getPhone() {
        return StringUtils.isEmpty(phone)?"": phone;
    }

    public String getTel() {
        return StringUtils.isEmpty(tel)?"": tel;
    }

    public Integer getType() {
        return type == null?-1: type;
    }

    public Integer getState() {
        return state == null?-1: state;
    }

    public String getIdNumber() {
        return StringUtils.isEmpty(idNumber)?"": idNumber;
    }

    public Integer getSex() {
        return sex == null?-1: sex;
    }

    public Long getLeaderId() {
        return leaderId == null?-1L: leaderId;
    }

    public Long getStoreId() {
        return storeId == null?-1L: storeId;
    }

    public String getPhotoImg() {
        return StringUtils.isEmpty(photoImg)?"": photoImg;
    }

    public Long getCreateTime() {
        return createTime == null?0L: createTime;
    }

    public String getCreateUser() {
        return StringUtils.isEmpty(createUser)?"": createUser;
    }

    public Long getCreateUserId() {
        return createUserId == null?-1L: createUserId;
    }

    public Long getCheckUserId() {
        return checkUserId == null?-1L: checkUserId;
    }

    public Long getHireTime() {
        return hireTime == null?0L: hireTime;
    }

    public Long getLeaveTime() {
        return leaveTime == null?0L: leaveTime;
    }

    public Long getAccountId() {
        return accountId == null?-1L: accountId;
    }

    public String getClientType() {
        return StringUtils.isEmpty(clientType)?"": clientType;
    }

    public Integer getBizScope() {
        return bizScope == null?-1: bizScope;
    }

    public String getBizDesc() {
        return StringUtils.isEmpty(bizDesc)?"": bizDesc;
    }
}
