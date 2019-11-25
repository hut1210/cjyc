package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.ExistDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;

import java.util.List;

/**
 * <p>
 * 保存个人到承运商下司机记录 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-11-25
 */
public interface IExistDriverDao extends BaseMapper<ExistDriver> {

    /**
     * 获取所有已存在数据
     * @return
     */
    List<ExistDriverVo> findExistDriver();

}
