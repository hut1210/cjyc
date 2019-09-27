package com.cjyc.customer.api.controller;

import com.cjkj.common.utils.JsonUtil;
import com.cjyc.customer.api.annotations.HeaderIgnoreNav;
import com.cjyc.customer.api.annotations.OperationLogNav;
import com.cjyc.customer.api.config.RetCodeEnum;
import com.cjyc.customer.api.config.RetResult;
import com.cjyc.customer.api.service.IAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/app")
@Api(tags = "app",description = "app相关的接口,包含登录、登出、通知等")
public class AppController {

    @Autowired
    private IAppService appService;

    /**
     * 发送客户端短信
     *
     * */
    @ApiOperation(value = "发送短信验证码接口", notes = "发送短信验证码时调用", httpMethod = "POST")
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "发送手机号", required = true, dataType = "String", paramType = "query"),
    })
    @HeaderIgnoreNav
    public String sendMessage(String phone) throws Exception{

        boolean success = appService.sendMessage(phone);

        return success ? JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),"短信已发送"))
                : JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.FAIL.getCode(),"短信发送失败"));
    }

    /**
     * 客户端登录\注册接口
     *
     * */
    @ApiOperation(value = "客户登录|注册接口", notes = "app登录、注册操作时调用", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "客户登录手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "messageCode", value = "客户登录短信验证码", required = true, dataType = "String", paramType = "query")
    })
    @HeaderIgnoreNav
    public String login(String phone,String messageCode){
        try {
            //校验验证码
            if(appService.checkMsgCode(phone,messageCode)){

                Map map = appService.login(phone);

                return JsonUtil.toJson(RetResult.buildResponse(
                        RetCodeEnum.SUCCESS.getCode(),"登录成功",map));
            }else{
                return JsonUtil.toJson(RetResult.buildResponse(
                        RetCodeEnum.FAIL.getCode(),"验证码错误"));
            }
        }catch (Exception e){
            return JsonUtil.toJson(RetResult.buildResponse(
                    RetCodeEnum.API_INVOKE_ERROR.getCode(),RetCodeEnum.API_INVOKE_ERROR.getMsg()));
        }

    }

    /**
     * 客户端获取通知接口
     *
     * */
    @ApiOperation(value = "获取通知接口", notes = "app登录后调用", httpMethod = "POST")
    @RequestMapping(value = "/notice", method = RequestMethod.POST)
    @OperationLogNav
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "客户登录手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "客户token", required = true, dataType = "String", paramType = "query")
    })
    public String notice(String phone, String token){
        List<Map<String,Object>> noticeList = null;
        //todo 查询通知列表
        return JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),"获取成功",noticeList));
    }

    /**
     * 客户端登出接口
     *
     * */
    @ApiOperation(value = "客户登出接口", notes = "app登出操作时调用", httpMethod = "POST")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerCode", value = "客户code", required = true, dataType = "String", paramType = "query"),
    })
    @HeaderIgnoreNav
    public String logout(String customerCode, @RequestHeader String token){
        appService.logout(customerCode,token);
        return JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),"登出成功"));
    }

    /**
     * 检查更新
     *
     * */
    @ApiOperation(value = "检查更新接口", notes = "app登录后调用", httpMethod = "POST")
    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST)
    @OperationLogNav
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "当前客户端app版本号", required = true, dataType = "String", paramType = "query"),
    })
    public String checkVersion(String phone, String versionCode){
        List<Map<String,Object>> versionInfoList = null;
        //todo 查询app更新信息
        return JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),"获取成功",versionInfoList));
    }

    /**
     * 获取客户端隐私协议
     *
     * */
    @ApiOperation(value = "获取客户端隐私协议", notes = "点击协议链接时调用",httpMethod = "POST")
    @RequestMapping(value = "/getAgreementPage",method = RequestMethod.POST)
    @HeaderIgnoreNav
    public String getAgreementPage(){
        //todo 隐私协议h5地址
        String agreementHtml = "";
        return JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),"获取成功",agreementHtml));
    }
}
