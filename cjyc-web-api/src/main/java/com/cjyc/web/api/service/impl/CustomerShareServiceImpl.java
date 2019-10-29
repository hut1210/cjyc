package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dto.web.customer.CustomerShareDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerShareService;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class CustomerShareServiceImpl implements ICustomerShareService {
    @Override
    public ResultVo<PageVo<Map<String, Object>>> getShareList(CustomerShareDto reqDto) {
        return null;
    }
}