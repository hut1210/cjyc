package com.cjyc.common.system.feign.fallback;

import com.cjyc.common.system.dto.location.ResultData;
import com.cjyc.common.system.dto.location.UploadUserLocationReq;
import com.cjyc.common.system.feign.ISysLocationService;
import feign.hystrix.FallbackFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @Description 位置服务降级工厂
 * @Author Liu Xing Xiang
 * @Date 2020/4/9 10:22
 **/
@Component
@Log4j2
public class LocationServiceFallbackFactory implements FallbackFactory<ISysLocationService> {
    @Override
    public ISysLocationService create(Throwable throwable) {
        return new ISysLocationService() {
            @Override
            public ResultData uploadUserLocation(UploadUserLocationReq uploadUserLocationReq) {
                log.error("定位信息上传服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }

            @Override
            public ResultData getUserLocation(Object obj) {
                log.error("用户实时位置查询服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }

            @Override
            public ResultData getLocationByPlateNo(Object obj) {
                log.error("根据车牌号查询运输车实时位置服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }
        };
    }
}
