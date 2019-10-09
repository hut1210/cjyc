package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.CustomerVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:20
 *  @Description: 移动用户
 */ 
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CustomerServiceImpl implements ICustomerService{

    @Resource
    private ICustomerDao iCustomerDao;

    @Override
    public ResultVo saveCustomer(CustomerDto customerDto) {
        try{
            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            iCustomerDao.insert(customer);
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"新增移动用户处理异常");
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"新增移动用户成功");
    }

    @Override
    public ResultVo getAllCustomer(Integer pageNo,Integer pageSize) {
        PageInfo<Customer> pageInfo = null;
        try{
            if (null == pageNo) {
                pageNo=1;
            }
            if (null == pageSize) {
                pageSize=10;
            }
            PageHelper.startPage(pageNo, pageSize);
            List<Customer> customerList = iCustomerDao.selectList(new QueryWrapper<>());
            pageInfo = new PageInfo<>(customerList);
        }catch (Exception e){
            log.error("分页查询移动端用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"分页查询移动端用户处理异常");
        }
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),"分页查询移动端用户处理成功",pageInfo);
    }

    @Override
    public ResultVo showCustomerById(Long id) {
        Customer customer = null;
        try{
            if(null == id){
                return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"用户id不能为空");
            }
            customer = iCustomerDao.selectById(id);
            return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"根据用户id查看移动端用户成功",customer);
        }catch (Exception e){
            log.error("根据用户id查看移动端用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"根据用户id查看移动端用户出现异常");
        }
    }

    @Override
    public ResultVo updateCustomer(CustomerDto customerDto) {
        try{
            Customer customer = iCustomerDao.selectById(customerDto.getId());
            if(null != customer){
                customer.setName(customerDto.getName());
                customer.setPhone(customerDto.getPhone());
                customer.setIdCard(customerDto.getIdCard());
                customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
                customer.setIdCardBackImg(customerDto.getIdCardBackImg());
                iCustomerDao.updateById(customer);
            }
        }catch (Exception e){
            log.error("更新用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"更新移动端用户处理异常");
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"更新移动端用户成功");
    }

    @Override
    public ResultVo deleteCustomer(Long[] arrIds) {
        int num = 0;
        try{
            if( null != arrIds && arrIds.length != 0){
                List<Long> listIds = Arrays.asList(arrIds);
                num = iCustomerDao.deleteBatchIds(listIds);
                if(num > 0){
                    return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"删除移动端用户处理成功");
                }
            }
        }catch (Exception e){
            log.error("删除用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"删除移动端用户处理异常");
        }
        return null;
    }

    @Override
    public ResultVo findCustomer(JSONObject jsonObject) {
        PageInfo<CustomerVo> pageInfo = null;
        try{
            Integer pageNo = jsonObject.getInteger("pageNo");
            Integer pageSize = jsonObject.getInteger("pageSize");
            if (null == pageNo) {
                pageNo=1;
            }
            if (null == pageSize) {
                pageSize=10;
            }
            String phone = jsonObject.getString("phone");
            String name = jsonObject.getString("name");
            String idCard = jsonObject.getString("idCard");

            PageHelper.startPage(pageNo, pageSize);
            List<CustomerVo> customerVos = iCustomerDao.findCustomer(phone,name,idCard);
            pageInfo = new PageInfo<>(customerVos);
        }catch (Exception e){
            log.error("根据条件查询用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"根据条件查询用户处理出现异常");
        }
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),"根据条件分页查询移动端用户处理成功",pageInfo);
    }
}
