package com.cjyc.web.api.service.impl;

import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dto.web.publicPayBank.PayBankDto;
import com.cjyc.common.model.dto.web.publicPayBank.PayBankImportExcel;
import com.cjyc.common.model.entity.PublicPayBank;
import com.cjyc.common.model.dao.IPublicPayBankDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.postal.ProvinceVo;
import com.cjyc.common.model.vo.web.publicPay.PayBankVo;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.web.api.service.IPublicPayBankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class PublicPayBankServiceImpl extends ServiceImpl<IPublicPayBankDao, PublicPayBank> implements IPublicPayBankService {

    @Resource
    private IPublicPayBankDao payBankDao;
    @Resource
    private RedisUtils redisUtils;

    private Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public boolean importPayBankExcel(MultipartFile file, Long loginId) {
        boolean result;
        try{
            List<PayBankImportExcel> payBankImportList = ExcelUtil.importExcel(file, 0, 1, PayBankImportExcel.class);
            if(!CollectionUtils.isEmpty(payBankImportList)){
                for(PayBankImportExcel payBankImport : payBankImportList){
                    PublicPayBank ppb = new PublicPayBank();
                    ppb.setBankCode(payBankImport.getBankCode());
                    ppb.setPayBankNo(payBankImport.getPayBankNo());
                    ppb.setSubBankName(payBankImport.getSubBankName());
                    ppb.setAreaCode(payBankImport.getPayBankNo().substring(3,7));
                    ppb.setCreateUserId(loginId);
                    ppb.setCreateTime(NOW);
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
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<PayBankVo> payBankInfoList = null;
        if(isSearchCache){
            //放入缓存
            String key = RedisKeys.getPayBankInfoKey(dto);
            String payBankInfoStr = redisUtils.hget(key,key);
            payBankInfoList = JsonUtils.jsonToList(payBankInfoStr, PayBankVo.class);
            if(CollectionUtils.isEmpty(payBankInfoList)){
                payBankInfoList = payBankDao.findPayBankInfo(dto);
                redisUtils.hset(key, key, JsonUtils.objectToJson(payBankInfoList));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }else{
            payBankInfoList = payBankDao.findPayBankInfo(dto);
        }
        PageInfo<PayBankVo> pageInfo = new PageInfo<>(payBankInfoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public PublicPayBank findPayBank(String subBankName) {
        PublicPayBank payBank = payBankDao.findPayBank(subBankName);
        return payBank;
    }
}
