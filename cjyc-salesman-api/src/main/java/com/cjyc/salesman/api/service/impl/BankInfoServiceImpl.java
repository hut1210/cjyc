package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.BankInfo;
import com.cjyc.common.model.dao.IBankInfoDao;
import com.cjyc.salesman.api.service.IBankInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银行信息对照表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class BankInfoServiceImpl extends ServiceImpl<IBankInfoDao, BankInfo> implements IBankInfoService {

}
