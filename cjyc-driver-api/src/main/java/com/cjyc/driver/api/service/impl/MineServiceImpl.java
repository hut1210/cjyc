package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IBankCardBindDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
import com.cjyc.driver.api.service.IMineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MineServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IMineService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Resource
    private IBankCardBindDao bankCardBindDao;

    @Override
    public ResultVo findBinkCardInfo(Long loginId) {
        List<BinkCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(loginId);
        return BaseResultUtil.success(CollectionUtils.isEmpty(bankCardVos) ? Collections.EMPTY_LIST:bankCardVos);
    }
}
