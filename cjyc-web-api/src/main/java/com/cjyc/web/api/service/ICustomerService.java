package com.cjyc.web.api.service;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.CustomerDto;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService {

    /**
     *  新增移动端用户
     * @param customerDto
     * @return
     */
    ResultVo saveCustomer(CustomerDto customerDto);
}
