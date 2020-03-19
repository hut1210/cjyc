package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.payBank.PayBankDto;
import com.cjyc.common.model.entity.PayBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.payBank.PayBankVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
public interface IPayBankService extends IService<PayBank> {

    /**
     * 导入对公支付行号
     * @param file
     * @param loginId
     * @return
     */
    boolean importPayBankExcel(MultipartFile file, Long loginId);

    /**
     * 获取对公支付银行信息
     * @param isSearchCache 缓存
     * @param dto
     * @return
     */
    ResultVo<PageVo<PayBankVo>> findPayBankInfo(boolean isSearchCache,PayBankDto dto);

    /**
     * 根据银行名称获取对公银行信息
     * @param subBankName
     * @return
     */
    PayBank findPayBank(String subBankName);



}
