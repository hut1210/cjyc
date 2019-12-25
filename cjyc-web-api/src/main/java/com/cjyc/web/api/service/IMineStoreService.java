package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.mineStore.SetContactPersonDto;
import com.cjyc.common.model.dto.web.mineStore.StorageCarQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;

/**
 * 我的业务中心
 */
public interface IMineStoreService {

    /**
     * 查询我的业务员列表信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<MySalesmanVo>> listSalesman(ListMineSalesmanDto dto);

    /**
     * 设为联系人
     * @param dto
     * @return
     */
    ResultVo setContractPerson(SetContactPersonDto dto);

    /**
     * 功能描述: 分页查询在库列表
     * @author liuxingxiang
     * @date 2019/12/25
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo getStorageCarPage(StorageCarQueryDto dto);
}
