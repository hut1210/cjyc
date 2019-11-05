package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.CustomerContract;

import java.util.List;

public interface ICustomerContractService extends IService<CustomerContract> {

    /**
     * 批量保存大客户合同
     * @param list
     * @return
     */
    boolean saveBatch(List<CustomerContract> list);
}
