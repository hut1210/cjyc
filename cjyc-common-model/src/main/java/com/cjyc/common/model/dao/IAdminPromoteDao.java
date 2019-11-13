package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.promote.AdminPromoteQueryDto;
import com.cjyc.common.model.entity.AdminPromote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.promote.AdminPromoteVo;

import java.util.List;

/**
 * <p>
 * 业务员推广表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
public interface IAdminPromoteDao extends BaseMapper<AdminPromote> {

    /**
     * 功能描述: 分页查询分享列表
     * @author liuxingxiang
     * @date 2019/11/13
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.web.promote.AdminPromoteVo>
     */
    List<AdminPromoteVo> selectPage(AdminPromoteQueryDto dto);
}
