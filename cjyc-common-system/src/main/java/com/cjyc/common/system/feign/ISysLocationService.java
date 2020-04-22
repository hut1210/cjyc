package com.cjyc.common.system.feign;

import com.cjyc.common.system.dto.location.ResultData;
import com.cjyc.common.system.dto.location.UploadUserLocationReq;
import com.cjyc.common.system.feign.fallback.LocationServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description 位置服务平台接口
 * @Author Liu Xing Xiang
 * @Date 2020/4/9 10:09
 **/
@FeignClient(value = "cps-api", fallbackFactory = LocationServiceFallbackFactory.class)
public interface ISysLocationService {

    /**
     * 功能描述: 定位信息上传
     * @author liuxingxiang
     * @date 2020/4/9
     * @param
     * @return com.cjkj.common.model.ResultData
     */
    @PostMapping(value = "/yunche/uploadUserLocation")
    ResultData uploadUserLocation(@RequestBody UploadUserLocationReq uploadUserLocationReq);

    /**
     * 功能描述: 用户实时位置查询
     * @author liuxingxiang
     * @date 2020/4/9
     * @param obj
     * @return com.cjkj.common.model.ResultData
     */
    @PostMapping(value = "/yunche/getUserLocation")
    ResultData getUserLocation(@RequestBody Object obj);

    /**
     * 功能描述: 根据车牌号查询运输车实时位置
     * @author liuxingxiang
     * @date 2020/4/9
     * @param obj
     * @return com.cjkj.common.model.ResultData
     */
    @PostMapping(value = "/yunche/track")
    ResultData getLocationByPlateNo(@RequestBody Object obj);
}
