package com.cjyc.foreign.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.foreign.api.entity.Line;
import org.apache.ibatis.annotations.Param;

public interface ITestDao extends BaseMapper<Line> {

    Line getLineById(@Param("id") Long id);
}
