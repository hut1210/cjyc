package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.AppItemDto;
import com.cjyc.common.model.dto.sys.SysPictureDto;
import com.cjyc.common.model.vo.AppItemVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

public interface ICsAppService {
    /**
     * 查询APP端首页轮播图
     * @param dto
     * @return
     */
    ResultVo<AppItemVo> getSysPicture(AppItemDto dto);

    /**
     * 功能描述: 修改首页轮播图
     * @author liuxingxiang
     * @date 2019/11/21
     * @param sysPictureDto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo updateSysPicture(SysPictureDto sysPictureDto);
}
