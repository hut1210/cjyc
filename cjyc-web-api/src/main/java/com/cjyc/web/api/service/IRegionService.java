package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.city.RegionAddDto;
import com.cjyc.common.model.dto.web.city.RegionQueryDto;
import com.cjyc.common.model.dto.web.city.RegionUpdateDto;
import com.cjyc.common.model.vo.ResultVo;

/**
 * @Description 大区管理接口
 * @Author LiuXingXiang
 * @Date 2019/11/7 11:46
 **/
public interface IRegionService {
    /**
     * 功能描述: 查询大区信息列表
     * @author liuxingxiang
     * @date 2019/11/7
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo getRegionPage(RegionQueryDto dto);

    /**
     * 功能描述: 新增大区
     * @author liuxingxiang
     * @date 2019/11/7
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo addRegion(RegionAddDto dto) throws Exception;

    /**
     * 功能描述: 修改大区
     * @author liuxingxiang
     * @date 2019/11/7
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo modifyRegion(RegionUpdateDto dto) throws Exception;
}
