package com.cjyc.common.model.vo.salesman.login;

import com.cjyc.common.model.dto.sys.SysRoleDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Saleman对象", description="业务员表")
public class SalemanLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
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

    @ApiModelProperty(value = "状态：0审核中，2在职，4取消审核，7已驳回，9已离职")
    private Integer state;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "性别: 0女，1男")
    private Integer sex;

    @ApiModelProperty(value = "上级ID")
    private Long leaderId;

    @ApiModelProperty(value = "所属业务中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @ApiModelProperty(value = "头像")
    private String photoImg;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "审核人")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long checkUserId;

    @ApiModelProperty(value = "入职时间")
    private Long hireTime;

    @ApiModelProperty(value = "离职时间")
    private Long leaveTime;

    /**
     * -----------------------------
     */

    @ApiModelProperty(value = "业务员角色列表")
    private List<SysRoleDto> roleList;

}

