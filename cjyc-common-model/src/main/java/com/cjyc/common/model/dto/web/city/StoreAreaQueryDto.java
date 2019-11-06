package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 业务中心区域查询参数类
 * @Author LiuXingXiang
 * @Date 2019/11/6 15:52
 **/
@Data
public class StoreAreaQueryDto implements Serializable {
    private static final long serialVersionUID = 9177744066955537372L;
    public interface AddAndRemoveCoveredArea{}
    public interface GetStoreAreaList{}
    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "区编码")
    private String areaCode;

    @ApiModelProperty(value = "业务中心ID")
    @NotNull(groups = {AddAndRemoveCoveredArea.class,GetStoreAreaList.class},message = "业务中心ID不能为空")
    private Long storeId;

    @ApiModelProperty(value = "区编码列表")
    @NotEmpty(groups = {AddAndRemoveCoveredArea.class},message = "区编码列表不能为空")
    private List<String> areaCodeList;
}
