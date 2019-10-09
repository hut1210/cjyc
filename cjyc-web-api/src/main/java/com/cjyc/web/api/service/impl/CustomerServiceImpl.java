package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.utils.SnowflakeIdWorker;
import com.cjyc.common.model.dao.ICustomerContractDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.CustomerVo;
import com.cjyc.common.model.vo.KeyCustomerDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.BasePageVo;
import com.cjyc.web.api.dto.CustomerContractVo;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.dto.KeyCustomerVo;
import com.cjyc.web.api.exception.CommonException;
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

    // 短日期格式
    public static String DATE_FORMAT = "yyyy/MM/dd";

    @Resource
    private ICustomerDao iCustomerDao;

    @Resource
    private ICustomerContractDao iCustomerContractDao;

    @Override
    public ResultVo saveCustomer(CustomerDto customerDto) {
        try{
            Customer customer = new Customer();
            customer.setId(SnowflakeIdWorker.nextId());
            customer.setName(customerDto.getName());
            customer.setPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            customer.setType(1);
            iCustomerDao.insert(customer);
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            throw new CommonException(e.getMessage());
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
            List<Customer> customerList = iCustomerDao.selectList(new QueryWrapper<Customer>().eq("type",1));
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
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"更新移动端用户成功");
    }

    @Override
    public ResultVo deleteCustomer(Long[] arrIds) {
        int num ;
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
            throw new CommonException(e.getMessage());
        }
        return null;
    }

    @Override
    public ResultVo findCustomer(JSONObject jsonObject) {
        PageInfo<CustomerVo> pageInfo ;
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

    @Override
    public ResultVo saveKeyCustAndContract(KeyCustomerVo keyCustomerVo) {
        try{
            //新增大客户
            Customer customer = new Customer();
            Long id = SnowflakeIdWorker.nextId();
            customer.setId(id);
            customer.setName(keyCustomerVo.getName());
            customer.setAlias(keyCustomerVo.getAlias());
            customer.setContactMan(keyCustomerVo.getContactMan());
            customer.setPhone(keyCustomerVo.getPhone());
            customer.setContactAddress(keyCustomerVo.getContactAddress());
            customer.setCustomerNature(keyCustomerVo.getCustomerNature());
            customer.setCompanyNature(keyCustomerVo.getCompanyNature());
            customer.setType(2);
            iCustomerDao.insert(customer);

            //合同集合
            List<CustomerContractVo> customerConList = keyCustomerVo.getCustContraVos();
            if(customerConList != null && customerConList.size() > 0){
                for(CustomerContractVo vo : customerConList){
                    saveCustomerContract(id , vo);
                }
            }
        }catch (Exception e){
            log.error("新增大客户&合同出现异常",e);
            throw new CommonException("新增大客户&合同出现异常");
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"新增大客户&合同处理成功");
    }

    @Override
    public ResultVo deleteKeyCustomer(Long[] arrIds) {
        try{
            if( null != arrIds && arrIds.length > 0){
                List<Long> ids = Arrays.asList(arrIds);
                iCustomerDao.deleteBatchIds(ids);
                //循环删除大客户合同
                for(Long custid : ids){
                    iCustomerContractDao.deleteContractByCustomerId(custid);
                }
            }
        }catch (Exception e){
            log.error("删除大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"删除大客户处理成功");
    }

    @Override
    public ResultVo getAllKeyCustomer(BasePageVo pageVo) {
        PageInfo<KeyCustomerDto> pageInfo;
        try{
            Integer pageNo = pageVo.getPageNo();
            Integer pageSize = pageVo.getPageSize();
            if (null == pageNo) {
                pageNo=1;
            }
            if (null == pageSize) {
                pageSize=10;
            }
            PageHelper.startPage(pageNo, pageSize);
            List<KeyCustomerDto> customerList = iCustomerDao.getAllKeyCustomter();
            pageInfo = new PageInfo<>(customerList);
        }catch (Exception e){
            log.error("分页查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"分页查看大客户处理成功",pageInfo);
    }

    /**
     * 新增大客户合同
     * @param id  大客户id
     * @param vo  合同
     */
    private void saveCustomerContract(Long id ,CustomerContractVo vo){
        try{
            CustomerContract custCont = new CustomerContract();
            custCont.setId(SnowflakeIdWorker.nextId());
            custCont.setCustomerId(id);
            custCont.setContractNo(vo.getContractNo());
            custCont.setContactNature(vo.getContactNature());
            custCont.setContractLife(LocalDateTimeUtil.convertToLong(vo.getContractLife(),DATE_FORMAT));
            custCont.setProjectName(vo.getProjectName());
            custCont.setProjectLevel(vo.getProjectLevel());
            custCont.setMajorProduct(vo.getMajorProduct());
            custCont.setProjectNature(vo.getProjectNature());
            custCont.setDateOfProSign(LocalDateTimeUtil.convertToLong(vo.getDateOfProSign(),DATE_FORMAT));
            custCont.setOneOffContract(vo.getOneOffContract());
            custCont.setProTraVolume(vo.getProTraVolume());
            custCont.setAvgMthTraVolume(vo.getAvgMthTraVolume());
            custCont.setBusiCover(vo.getBusiCover());
            custCont.setFixedRoute(vo.getFixedRoute());
            custCont.setProjectDeper(vo.getProjectDeper());
            custCont.setProjectLeader(vo.getProjectLeader());
            custCont.setLeaderPhone(vo.getLeaderPhone());
            custCont.setProjectStatus(vo.getProjectStatus());
            custCont.setProjectTeamPer(vo.getProjectTeamPer());
            custCont.setProjectEstabTime(LocalDateTimeUtil.convertToLong(vo.getProjectEstabTime(),DATE_FORMAT));
            custCont.setMajorKpi(vo.getMajorKpi());
            iCustomerContractDao.insert(custCont);
        }catch (Exception e){
            log.error("新增合同出现异常",e);
            throw new CommonException("新增合同出现异常");
        }
    }
}
