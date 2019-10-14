package com.cjyc.salesman.api.service;

import com.cjyc.common.model.entity.Saleman;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业务员表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
public interface ISalemanService extends IService<Saleman> {
    /**
     * 根据手机号查询业务员
     * @author JPG
     * @since 2019/10/14 9:36
     * @param phone
     */
    Saleman getByphone(String phone);

}
