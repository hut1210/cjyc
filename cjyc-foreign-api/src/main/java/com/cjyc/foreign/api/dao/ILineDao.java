package com.cjyc.foreign.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.foreign.api.dto.req.LineReqDto;
import com.cjyc.foreign.api.dto.res.LineResDto;
import com.cjyc.foreign.api.entity.Line;

/**
 * @Description 班线管理接口
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:12
 **/
public interface ILineDao extends BaseMapper<Line> {
    /**
     * 功能描述: 根据城市获取班线价格
     * @author liuxingxiang
     * @date 2020/3/10
     * @param dto
     * @return com.cjyc.foreign.api.dto.res.LineResDto
     */
    LineResDto getLinePriceByCity(LineReqDto dto);
}
