package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户公用业务
 * @author JPG
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CsCustomerServiceImpl implements ICsCustomerService {

    private static final String CUSTOMER_FIXED_PWD = YmlProperty.get("cjkj.customer.password");
    private static final String CUSTOMER_FIXED_DEPTID = YmlProperty.get("cjkj.dept_customer_id");

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private StringRedisUtil redisUtil;

    @Override
    public Customer getByUserId(Long userId, boolean isSearchCache) {
        return customerDao.findByUserId(userId);
    }

    /**
     * 根据手机号查询客户
     *
     * @param customerPhone
     * @param isCache
     * @author JPG
     * @since 2019/11/5 9:13
     */
    @Override
    public Customer getByPhone(String customerPhone, boolean isCache) {
        return customerDao.findByPhone(customerPhone);
    }

    @Override
    public Customer save(Customer customer) {
        //添加架构组数据
        AddUserReq addUserReq = new AddUserReq();
        addUserReq.setAccount(customer.getContactPhone());
        addUserReq.setPassword(CUSTOMER_FIXED_PWD);
        addUserReq.setDeptId(Long.valueOf(CUSTOMER_FIXED_DEPTID));
        addUserReq.setMobile(customer.getContactPhone());
        addUserReq.setName(customer.getName());
        ResultData<AddUserResp> resultData = sysUserService.save(addUserReq);

        if(resultData == null || resultData.getData() == null || resultData.getData().getUserId() == null){
            throw new ServerException(resultData == null ? "添加用户失败" : resultData.getMsg());
        }
        customer.setUserId(resultData.getData().getUserId());
        customerDao.insert(customer);
        return customer;
    }
}
