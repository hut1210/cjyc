package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.BankCardBind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.customerInfo.CustomerCardInfoVo;
import com.cjyc.common.model.vo.driver.mine.BankCardVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 银行卡绑定信息表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IBankCardBindDao extends BaseMapper<BankCardBind> {

    /**
     * 根据用户id删除银行卡信息
     * @param carrierId
     * @return
     */
    int removeBandCarBind(@Param("carrierId") Long carrierId);

    /**
     * 根据承运商id获取绑定银行卡信息
     * @param carrierId
     * @return
     */
    List<BankCardVo> findBinkCardInfo(@Param("carrierId") Long carrierId);

    /**
     *  根据承运商id获取绑定有效银行卡张数
     * @param carrierId
     * @return
     */
    int findBankCardInfoNum(@Param("carrierId") Long carrierId);

    /**
     * 获取用户银行卡信息
     * @param loginId
     * @return
     */
    CustomerCardInfoVo findCustomerBankCardInfo(@Param("loginId") Long loginId);

}
