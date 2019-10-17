package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.service.service.IStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class StoreServiceImpl implements IStoreService {

    @Resource
    IStoreDao iStoreDao;

    @Override
    public PageInfo<Store> getAllStore(BasePageDto dto) {
        PageInfo<Store> pageInfo = new PageInfo<>();
        try{
            List<Store> stores = iStoreDao.selectList(new QueryWrapper<>());
            if(stores != null && stores.size() > 0){
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(stores);
            }
            return pageInfo;
        }catch (Exception e){
            log.info("查询业务中心出现异常");
        }
        return null;
    }
}