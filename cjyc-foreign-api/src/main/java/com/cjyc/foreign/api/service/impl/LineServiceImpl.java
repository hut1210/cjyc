package com.cjyc.foreign.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dao.ILineDao;
import com.cjyc.foreign.api.dto.req.LineReqDto;
import com.cjyc.foreign.api.dto.res.LineResDto;
import com.cjyc.foreign.api.entity.Line;
import com.cjyc.foreign.api.service.ILineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description 班线业务接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:05
 **/
@Slf4j
@Service
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {
    @Resource
    private ILineDao lineDao;

    @Override
    public ResultVo<LineResDto> getLinePriceByCity(LineReqDto dto) {
        log.info("===>查询报价，请求参数：{}", JSON.toJSONString(dto));
        LineResDto res = lineDao.getLinePriceByCity(dto);
        ResultVo<LineResDto> resultVo = BaseResultUtil.success(res);
        log.info("<===查询报价，返回参数：{}", JSON.toJSONString(resultVo));
        return resultVo;
    }
}
