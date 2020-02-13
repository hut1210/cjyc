package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IBankInfoDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.entity.BankInfo;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.cjyc.common.system.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CsBankInfoServiceImpl implements ICsBankInfoService {

    @Resource
    private IBankInfoDao bankInfoDao;
    @Resource
    private RedisUtils redisUtils;

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
    public ResultVo findAppBankInfo(BankInfoDto dto) {
        String key = RedisKeys.getBankInfoKey(dto.getKeyword());
        PageInfo<BankInfoVo> pageInfo = JsonUtils.jsonToPojo(redisUtils.get(key), PageInfo.class);
        if(pageInfo == null){
            PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
            List<BankInfoVo> bankInvoVos = bankInfoDao.findAppBankInfo(dto);
            pageInfo = new PageInfo<>(bankInvoVos);
            redisUtils.set(key, JsonUtils.objectToJson(pageInfo));
            redisUtils.expire(key, 1, TimeUnit.DAYS);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo findWebBankInfo(KeywordDto dto) {
        List<BankInfoVo> bankInfoVos = null;
        String key = RedisKeys.getBankInfoKey(dto.getKeyword());
        bankInfoVos = JsonUtils.jsonToList(redisUtils.get(key), BankInfoVo.class);
        if(CollectionUtils.isEmpty(bankInfoVos)){
            bankInfoVos = bankInfoDao.findWebBankInfo(dto);
            redisUtils.set(key, JsonUtils.objectToJson(bankInfoVos));
            redisUtils.expire(key, 1, TimeUnit.DAYS);
        }
        return BaseResultUtil.success(bankInfoVos);
    }
}