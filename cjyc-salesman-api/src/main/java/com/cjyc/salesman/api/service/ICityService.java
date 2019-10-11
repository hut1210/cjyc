package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.City;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 韵车城市信息表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-09-30
 */
public interface ICityService extends IService<City> {


    City findById(@Param("cityCode") String cityCode);

}
