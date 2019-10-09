package com.cjyc.salesman.api.fegin;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.entity.sys.SysDeptEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface IDeptService {

    /**
     * 保存组织机构
     * @author JPG
     * @since 2019/10/9 15:44
     * @param sysDeptEntity
     * @return
     */
    @PostMapping("/sys/dept/save")
    ResultData save(SysDeptEntity sysDeptEntity);


}
