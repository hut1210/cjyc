package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;

/**
 * @Description 物流信息接口
 * @Author Liu Xing Xiang
 * @Date 2020/4/3 11:26
 **/
public interface ICsLogisticsInformationService {
    /**
     * 功能描述: 查询物流信息
     * @author liuxingxiang
     * @date 2020/4/3
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.LogisticsInformationVo>
     */
    ResultVo<LogisticsInformationVo> getLogisticsInformation(LogisticsInformationDto reqDto);
}
