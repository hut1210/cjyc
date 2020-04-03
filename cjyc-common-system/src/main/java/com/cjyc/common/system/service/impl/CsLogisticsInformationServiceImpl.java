package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dto.LogisticsInformationDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.LogisticsInformationVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import com.cjyc.common.system.service.ICsLogisticsInformationService;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description 物流信息接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/4/3 11:27
 **/
@Slf4j
@Service
public class CsLogisticsInformationServiceImpl implements ICsLogisticsInformationService {
    @Resource
    private ICsOrderCarLogService csOrderCarLogService;

    @Override
    public ResultVo<LogisticsInformationVo> getLogisticsInformation(LogisticsInformationDto reqDto) {
        LogisticsInformationVo logisticsInfoVo = new LogisticsInformationVo();
        // 查询车辆运输日志
        ResultVo<OutterLogVo> orderCarLog = csOrderCarLogService.getOrderCarLog(reqDto.getOrderCarNo());
        BeanUtils.copyProperties(orderCarLog.getData(),logisticsInfoVo);
        // 查询车辆实时位置

        logisticsInfoVo.setLocation("");
        return BaseResultUtil.success(logisticsInfoVo);
    }
}
