package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.user.DriverListVo;

import java.util.List;

/**
 * <p>
 * 司机信息表（登录司机端APP用户） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IDriverDao extends BaseMapper<Driver> {

    /**
     * 查询司机列表
     * @author JPG
     * @since 2019/10/16 17:32
     * @param paramsDto
     */
    List<DriverListVo> findList(DriverListDto paramsDto);
}
