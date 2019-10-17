package com.cjyc.common.service.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.Store;
import com.github.pagehelper.PageInfo;

/**
 *  @author: zj
 *  @Date: 2019/10/15 16:52
 *  @Description:业务中心信息
 */
public interface IStoreService {

    /**
     * 查询所有城市业务中心
     * @param dto
     * @return
     */
    PageInfo<Store>  getAllStore(BasePageDto dto);
}
