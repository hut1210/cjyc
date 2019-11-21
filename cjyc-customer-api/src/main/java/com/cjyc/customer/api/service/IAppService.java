package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.sys.SysPictureDto;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

/**
 * Created by leo on 2019/7/25.
 */
public interface IAppService {

    /**
     * 查询APP端首页轮播图
     * @param systemPicture
     * @return
     */
    ResultVo<List<String>> getSysPicture(String systemPicture);

    /**
     * 功能描述: 修改首页轮播图
     * @author liuxingxiang
     * @date 2019/11/21
     * @param sysPictureDto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo updateSysPicture(SysPictureDto sysPictureDto);
}
