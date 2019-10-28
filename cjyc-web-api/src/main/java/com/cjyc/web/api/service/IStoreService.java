package com.cjyc.web.api.service;

import com.cjyc.common.model.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 韵车业务中心信息表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
public interface IStoreService extends IService<Store> {

    List<Store> getByAreaCode(String areaCode);
}
