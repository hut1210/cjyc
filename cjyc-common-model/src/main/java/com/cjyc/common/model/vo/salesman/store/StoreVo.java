package com.cjyc.common.model.vo.salesman.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 业务中心列表返回对象
 */
@Data
public class StoreVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "storeId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long storeId;

    @ApiModelProperty(value = "业务中心名称")
    private String name;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "区编码")
    private String areaCode;

    @ApiModelProperty(value = "具体地址")
    private String detailAddr;

    @ApiModelProperty(value = "业务中心经度")
    private String lng;

    @ApiModelProperty(value = "业务中心纬度")
    private String lat;

    @ApiModelProperty(value = "状态：0待审核，2已开通，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "联系人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long contactAdminId;

    @ApiModelProperty(value = "创建人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "操作人")
    private String operationName;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "全地址")
    private String fullAddress;

    @ApiModelProperty(value = "删除标志 0正常，1删除")
    private Integer isDelete;

    public Long getId() {
        return id;
    }

    public String getName() {
        return StringUtils.isEmpty(name)?"" : name;
    }

    public String getProvince() {
        return StringUtils.isEmpty(province)?"" : province;
    }

    public String getProvinceCode() {
        return StringUtils.isEmpty(provinceCode)?"" : provinceCode;
    }

    public String getCity() {
        return StringUtils.isEmpty(city)?"" : city;
    }

    public String getCityCode() {
        return StringUtils.isEmpty(cityCode)?"" : cityCode;
    }

    public String getArea() {
        return StringUtils.isEmpty(area)?"" : area;
    }

    public String getAreaCode() {
        return StringUtils.isEmpty(areaCode)?"" : areaCode;
    }

    public String getDetailAddr() {
        return StringUtils.isEmpty(detailAddr)?"" : detailAddr;
    }

    public String getLng() {
        return StringUtils.isEmpty(lng)?"" : lng;
    }

    public String getLat() {
        return StringUtils.isEmpty(lat)?"" : lat;
    }

    public Integer getState() {
        return state == null?-1 : state;
    }

    public Long getContactAdminId() {
        return contactAdminId == null?-1L : contactAdminId;
    }

    public Long getCreateUserId() {
        return createUserId == null?-1L : createUserId;
    }

    public Long getCreateTime() {
        return createTime == null?0L : createTime;
    }

    public String getOperationName() {
        return StringUtils.isEmpty(operationName)?"" : operationName;
    }

    public Long getUpdateTime() {
        return updateTime == null?0L : updateTime;
    }

    public String getRemark() {
        return StringUtils.isEmpty(remark)?"" : remark;
    }

    public String getFullAddress() {
        return StringUtils.isEmpty(fullAddress)?"" : fullAddress;
    }

    public Integer getIsDelete() {
        return isDelete == null?-1: isDelete;
    }

    public Long getStoreId() {
        return this.getId();
    }
}
