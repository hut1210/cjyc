package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.dao.IStoreDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.web.api.service.IStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 韵车业务中心信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
@Service
public class StoreServiceImpl extends ServiceImpl<IStoreDao, Store> implements IStoreService {
    @Resource
    private IStoreDao storeDao;


    @Override
    public List<Store> getByAreaCode(String areaCode) {
        return storeDao.findByAreaCode(areaCode);
    }
}
