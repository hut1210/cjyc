package com.cjyc.customer.api.controller;

import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICustomerContactService;
import com.cjyc.common.service.service.ICustomerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther litan
 * @description: 客户联系人控制器
 * @date:2019/10/9
 */
@RestController
@RequestMapping("/customerContact")
@Api(tags = "customerContact",description = "客户联系人相关的接口")
public class CustomerContactController {

    @Autowired
    private ICustomerContactService customerContactService;

    /**
     * 客户端获取联系人接口
     * */
    @ApiOperation(value = "客户端获取联系人接口", notes = "客户端获取联系人接口", httpMethod = "POST")
    @RequestMapping(value = "/getContacts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id",  required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", defaultValue = "20", dataType = "Integer", paramType = "query")
    })
    public ResultVo getContacts(Long customerId, Integer page, Integer pageSize) {
        PageInfo<CustomerContact> pageInfo = customerContactService.getContactPage(customerId,page,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

}
