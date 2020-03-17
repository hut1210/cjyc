package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.publicPayBank.PayBankDto;
import com.cjyc.common.model.entity.PublicPayBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.publicPay.PayBankVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
public interface IPublicPayBankService extends IService<PublicPayBank> {

    /**
     * 导入对公支付行号
     * @param file
     * @param loginId
     * @return
     */
    boolean importPayBankExcel(MultipartFile file, Long loginId);

    /**
     * 获取对公支付银行信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<PayBankVo>> findPayBankInfo(PayBankDto dto);

}
