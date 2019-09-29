package com.cjyc.web.api.controller;

import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/index")
@Api(tags = "index",description = "web端基础接口,包含登录、登出、等")
public class IndexController {

    /**
     * 登录接口
     *
     * */
    @ApiOperation(value = "web端登录接口", notes = "登录操作时调用", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    public ResultVo login(String userName,String pwd){
        try {
            if(true){
                Map map = new HashMap();
                map.put("userName",userName);
                map.put("pwd",pwd);

                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"登录成功",map);
            }else{
                return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"登陆失败");
            }
        }catch (Exception e){
            return BaseResultUtil.getVo(ResultEnum.API_INVOKE_ERROR.getCode(), ResultEnum.API_INVOKE_ERROR.getMsg());
        }

    }

    /**
     * 登出接口
     *
     * */
    @ApiOperation(value = "web端登出接口", httpMethod = "POST")
    @RequestMapping(value = "/loginout", method = RequestMethod.POST)
    public String loginout(){
        try {

        }catch (Exception e){
        }
        return  null;
    }
}
