package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.BankCardBind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
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
     * @param userId
     * @return
     */
    int removeBandCarBind(@Param("userId") Long userId);

    /**
     * 根据司机id获取绑定银行卡信息
     * @param driverId
     * @return
     */
    List<BinkCardVo> findBinkCardInfo(@Param("driverId") Long driverId);

}
