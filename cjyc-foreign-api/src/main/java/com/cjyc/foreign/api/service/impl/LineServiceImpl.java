package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dao.ILineDao;
import com.cjyc.foreign.api.dto.req.LineReqDto;
import com.cjyc.foreign.api.dto.res.LineResDto;
import com.cjyc.foreign.api.entity.Line;
import com.cjyc.foreign.api.service.ILineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 班线业务接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:05
 **/
@Service
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {
    @Autowired
    private ILineDao lineDao;

    @Override
    public ResultVo<LineResDto> getLinePriceByCity(LineReqDto dto) {
        LineResDto res = lineDao.getLinePriceByCity(dto);
        return BaseResultUtil.success(res);
    }
}
