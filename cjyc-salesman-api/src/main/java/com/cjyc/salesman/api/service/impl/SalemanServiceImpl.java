package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Saleman;
import com.cjyc.common.model.dao.ISalemanDao;
import com.cjyc.salesman.api.service.ISalemanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务员表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class SalemanServiceImpl extends ServiceImpl<ISalemanDao, Saleman> implements ISalemanService {

    @Resource
    private ISalemanDao salemanDao;

    @Override
    public Saleman getByphone(String phone) {
        return salemanDao.findByPhone(phone);
    }
}
