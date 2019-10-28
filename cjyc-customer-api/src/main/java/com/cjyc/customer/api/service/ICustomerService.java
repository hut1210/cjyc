package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 * 客户表（登录用户端APP用户） 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
public interface ICustomerService extends IService<Customer> {

    ResultVo fuzzyList(CustomerfuzzyListDto paramsDto);
}
