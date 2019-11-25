package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarStorageLogDao;
import com.cjyc.common.model.dto.web.waybill.storeListDto;
import com.cjyc.common.model.entity.CarStorageLog;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarStorageLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 出入库记录 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
@Service
public class CarStorageLogServiceImpl extends ServiceImpl<ICarStorageLogDao, CarStorageLog> implements ICarStorageLogService {

    @Resource
    private ICarStorageLogDao carStorageLogDao;

    @Override
    public ResultVo<PageVo<CarStorageLog>> inList(storeListDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize(), true);
        List<CarStorageLog> list = carStorageLogDao.findList(dto);
        PageInfo<CarStorageLog> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }
}
