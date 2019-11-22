package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.mineStore.SetContactPersonDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;

import java.util.List;

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
}
