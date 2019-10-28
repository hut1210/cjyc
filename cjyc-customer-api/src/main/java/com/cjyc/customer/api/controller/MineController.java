package com.cjyc.customer.api.controller;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by leo on 2019/7/23.
 */
@Api(tags = "我的",description = "客户信息相关的接口")
@RequestMapping("/mine")
@RestController
public class MineController {

    /**
     * 查看客户个人信息
     * */
    @RequestMapping(value = "/getCustomerInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取客户个人信息接口", notes = "获取客户个人信息时调用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, dataType = "String", paramType = "query")
    })
    public ResultVo getCustomerInfo(String customerId){
        Object customerInfoVo = null;
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerInfoVo);
    }

    /**
     * 上传个人头像图片
     * */
    @RequestMapping(value = "/uploadPhotoImg", method = RequestMethod.POST)
    @ApiOperation(value = "上传个人头像接口", notes = "上传个人头像", httpMethod = "POST")
    @PostMapping(value="/uploadPhotoImg",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, dataType = "String", paramType = "query")
    })
    public ResultVo uploadPhotoImg(@ApiParam(value="图片文件",required=true)MultipartFile file){
        //todo 上传处理
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }
}
