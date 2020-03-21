package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.PostalCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
public interface IPostalCodeDao extends BaseMapper<PostalCode> {

    /**
     * 根据关键字获取省/地区
     * @param keyword
     * @return
     */
    List<ProvinceVo> findPostal(@Param("keyword") String keyword);

}
