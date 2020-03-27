package com.cjyc.web.api.service.impl;

import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dto.web.payBank.PayBankDto;
import com.cjyc.common.model.dto.web.payBank.PayBankImportExcel;
import com.cjyc.common.model.entity.PayBank;
import com.cjyc.common.model.dao.IPayBankDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.payBank.PayBankVo;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.web.api.service.IPayBankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
@Service
@Slf4j
public class PayBankServiceImpl extends ServiceImpl<IPayBankDao, PayBank> implements IPayBankService {

    @Resource
    private IPayBankDao payBankDao;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean importPayBankExcel(MultipartFile file, Long loginId) {
        boolean result;
        try{
            List<PayBankImportExcel> payBankImportList = ExcelUtil.importExcel(file, 0, 1, PayBankImportExcel.class);
            if(!CollectionUtils.isEmpty(payBankImportList)){
                for(PayBankImportExcel payBankImport : payBankImportList){
                    PayBank ppb = new PayBank();
                    ppb.setBankCode(payBankImport.getBankCode());
                    ppb.setPayBankNo(payBankImport.getPayBankNo());
                    ppb.setSubBankName(payBankImport.getSubBankName());
                    ppb.setAreaCode(payBankImport.getPayBankNo().substring(3,7));
                    ppb.setCreateUserId(loginId);
                    ppb.setCreateTime(System.currentTimeMillis());
                    payBankDao.insert(ppb);
                }
                result = true;
            }else{
                result = false;
            }
        }catch (Exception e){
            log.error("导入对公支付行号失败异常:{}", e);
            result = false;
        }
        return result;
    }

    @Override
    public ResultVo<PageVo<PayBankVo>> findPayBankInfo(boolean isSearchCache,PayBankDto dto) {
        List<PayBankVo> payBankInfoList = null;
        PageInfo<PayBankVo> pageInfo = null;
        if(isSearchCache){
            //放入缓存
            String key = RedisKeys.getPayBankInfoKey(dto);
            pageInfo = JsonUtils.jsonToPojo(redisUtils.get(key), PageInfo.class);
            if(pageInfo == null){
                PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
                payBankInfoList = payBankDao.findPayBankInfo(dto);
                pageInfo = new PageInfo<>(payBankInfoList);
                redisUtils.set(key, JsonUtils.objectToJson(pageInfo));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }else{
            PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
            payBankInfoList = payBankDao.findPayBankInfo(dto);
            pageInfo = new PageInfo<>(payBankInfoList);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public PayBank findPayBank(String subBankName) {
        PayBank payBank = payBankDao.findPayBank(subBankName);
        return payBank;
    }
}
