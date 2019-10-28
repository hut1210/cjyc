package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.customer.CustomerShareDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.service
 * @date:2019/10/28
 */
public interface ICustomerShareService {
    ResultVo<PageVo<Map<String,Object>>> getShareList(CustomerShareDto reqDto);
}
