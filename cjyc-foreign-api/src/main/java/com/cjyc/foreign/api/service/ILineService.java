package com.cjyc.foreign.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.req.LineReqDto;
import com.cjyc.foreign.api.dto.res.LineResDto;

/**
 * @Description 班线业务接口
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:05
 **/
public interface ILineService extends IService<Line> {
    /**
     * 功能描述: 根据城市获取班线价格
     * @author liuxingxiang
     * @date 2020/3/10
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.foreign.api.dto.res.LineResDto>
     */
    ResultVo<LineResDto> getLinePriceByCityCode(LineReqDto dto);
}
