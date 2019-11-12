package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.entity.CustomerLine;
import com.cjyc.common.model.dao.ICustomerLineDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.cjyc.customer.api.service.ICustomerLineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-01
 */
@Service
@Slf4j
public class CustomerLineServiceImpl extends ServiceImpl<ICustomerLineDao, CustomerLine> implements ICustomerLineService {

    @Resource
    private ICustomerLineDao customerLineDao;

    @Override
    public ResultVo queryLinePage(CommonDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerLineVo> lineVos = customerLineDao.findCustomerLine(dto.getLoginId());
        PageInfo<CustomerLineVo> pageInfo =  new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>(Collections.EMPTY_LIST):pageInfo);
    }
}
