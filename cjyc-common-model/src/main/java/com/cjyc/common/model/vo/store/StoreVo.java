package com.cjyc.common.model.vo.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 业务中心列表返回VO
 * @Author Liu Xing Xiang
 * @Date 2019/11/18 14:13
 **/
@Data
public class StoreVo implements Serializable {
    private static final long serialVersionUID = 2104666354394337355L;
    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "状态：0待审核，2已开通，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "操作人")
    private String operationName;

    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "归属大区名称")
    private String regionName;

    @ApiModelProperty(value = "管家范围")
    private Integer areaCount;
}
