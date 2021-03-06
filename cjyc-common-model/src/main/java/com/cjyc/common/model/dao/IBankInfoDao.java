package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.entity.BankInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 银行信息对照表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IBankInfoDao extends BaseMapper<BankInfo> {

    /**
     * 根据银行名称获取银行信息
     * @param bankName
     * @return
     */
    BankInfo findBankCode(@Param("bankName") String bankName);

    /**
     * 获取银行信息
     * @return
     */
    List<BankInfoVo> findAppBankInfo(BankInfoDto dto);

    /**
     * 获取银行信息
     * @return
     */
    List<BankInfoVo> findWebBankInfo(KeywordDto dto);
}
