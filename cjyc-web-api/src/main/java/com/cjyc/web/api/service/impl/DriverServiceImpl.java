package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dao.ISalemanDao;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.service.IDriverService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl implements IDriverService {
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ISalemanDao salemanDao;

    /**
     * 查询司机列表
     *
     * @author JPG
     * @since 2019/10/16 16:15
     */
    @Override
    public ResultVo<PageVo<DriverListVo>> lineWaitDispatchCarCountList(DriverListDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<DriverListVo> list = driverDao.findList(paramsDto);
        return null;
    }
}
