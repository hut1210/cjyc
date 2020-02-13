package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IBankInfoDao;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.BankInfo;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CsBankInfoServiceImpl implements ICsBankInfoService {

    @Resource
    private IBankInfoDao bankInfoDao;

    /**
     * 根据银行名称获取银行编码
     * @param bankName
     * @return
     */
    @Override
    public BankInfo findBankCode(String bankName){
        return bankInfoDao.findBankCode(bankName);
    }

    @Override
    public ResultVo findBankInfo(BasePageDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<BankInfoVo> bankInvoVos = bankInfoDao.findAllBankInfo(dto);
        PageInfo<BankInfoVo> pageInfo = new PageInfo<>(bankInvoVos);
        return BaseResultUtil.success(pageInfo);
    }
}