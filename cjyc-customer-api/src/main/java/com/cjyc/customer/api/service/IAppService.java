package com.cjyc.customer.api.service;

import com.cjyc.common.model.entity.Dictionary;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * Created by leo on 2019/7/25.
 */
public interface IAppService {

    /**
     * 查询APP端首页轮播图
     * @param systemPicture
     * @return
     */
    ResultVo<List<Dictionary>> getSysPicture(String systemPicture);
}
