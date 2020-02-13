package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.entity.BankInfo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;

/**
 * 银行卡信息
 */
public interface ICsBankInfoService {

    /**
     * 根据银行名称获取银行编码
     * @param bankName
     * @return
     */
    BankInfo findBankCode(String bankName);

    /**
     * 获取全部银行信息
     * @return
     */
    ResultVo findAppBankInfo(BankInfoDto dto);

    /**
     * 获取全部银行信息
     * @return
     */
    ResultVo findWebBankInfo(KeywordDto dto);
}
