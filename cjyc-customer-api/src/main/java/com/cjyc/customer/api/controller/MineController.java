package com.cjyc.customer.api.controller;

import com.cjyc.common.base.ResultEnum;
import com.cjyc.common.base.ResultVo;
import com.cjyc.common.entity.Customer;
import com.cjyc.common.service.ICustomerService;
import com.cjyc.customer.api.annotations.ApiVersion;
import com.cjyc.customer.api.annotations.OperationLogNav;
import com.cjyc.customer.api.until.ApiVersionContant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by leo on 2019/7/23.
 */
@Api(tags = "我的",description = "客户信息相关的接口")
@RequestMapping("/mine")
@RestController
public class MineController {

    @Autowired
    private ICustomerService customerService;

    /**
     * 测试分页
     * */
    @OperationLogNav
    @ApiOperation(value = "分页测试接口", notes = "分页测试", httpMethod = "POST")
    @RequestMapping(value = "/testPageList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "Integer", paramType = "query")
    })
    @ApiVersion(group = ApiVersionContant.CUSTOMER_APP_100)
    public ResultVo testPageList(Integer pageNum, Integer pageSize){
        PageInfo<Customer> customerPageInfo = customerService.pageList(pageNum,pageSize);
        return ResultVo.response(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerPageInfo);
    }

    /**
     * 查看客户个人信息
     * */
    @OperationLogNav
    @RequestMapping(value = "/getCustomerInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取客户个人信息接口", notes = "获取客户个人信息时调用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, dataType = "String", paramType = "query")
    })
    public ResultVo getCustomerInfo(String customerId){
        Object customerInfoVo = null;
        return ResultVo.response(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerInfoVo);
    }

    /**
     * 上传个人头像图片
     * */
    @OperationLogNav
    @RequestMapping(value = "/uploadPhotoImg", method = RequestMethod.POST)
    @ApiOperation(value = "上传个人头像接口", notes = "上传个人头像", httpMethod = "POST")
    @PostMapping(value="/uploadPhotoImg",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, dataType = "String", paramType = "query")
    })
    public ResultVo uploadPhotoImg(@ApiParam(value="图片文件",required=true)MultipartFile file){
        //todo 上传处理
        return ResultVo.response(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }
}
