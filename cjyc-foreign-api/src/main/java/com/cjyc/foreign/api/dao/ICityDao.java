package com.cjyc.foreign.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.foreign.api.entity.City;
import org.springframework.stereotype.Repository;

/**
 * @Description 城市持久层接口
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 14:55
 **/
@Repository("com.cjyc.foreign.api.dao.ICityDao")
public interface ICityDao extends BaseMapper<City> {

}

