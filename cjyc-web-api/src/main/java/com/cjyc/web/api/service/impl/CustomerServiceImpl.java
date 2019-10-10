package com.cjyc.web.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.utils.SnowflakeIdWorker;
import com.cjyc.common.model.dao.ICustomerContractDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.web.CustomerContractDto;
import com.cjyc.common.model.dto.web.CustomerDto;
import com.cjyc.common.model.dto.web.ListKeyCustomerDto;
import com.cjyc.common.model.dto.web.ShowKeyCustomerDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.BasePageVo;
import com.cjyc.common.model.vo.web.*;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    public boolean saveCustomer(CustomerVo customerDto) {
        try{
            Customer customer = new Customer();
            customer.setId(SnowflakeIdWorker.nextId());
            customer.setName(customerDto.getName());
            customer.setPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            customer.setType(1);
            return iCustomerDao.insert(customer)> 0 ? true : false;
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public PageInfo<Customer> getAllCustomer(BasePageVo basePageVo) {
        PageInfo<Customer> pageInfo = null;
        try{
            if(basePageVo.getCurrentPage() == null || basePageVo.getCurrentPage() < 1){
                basePageVo.setCurrentPage(1);
            }
            if(basePageVo.getPageSize() == null || basePageVo.getPageSize() < 1){
                basePageVo.setPageSize(10);
            }
            PageHelper.startPage(basePageVo.getCurrentPage(), basePageVo.getPageSize());
            List<Customer> customerList = iCustomerDao.selectList(new QueryWrapper<Customer>().eq("type",1));
            pageInfo = new PageInfo<>(customerList);
        }catch (Exception e){
            log.error("分页查询移动端用户出现异常",e);
        }
        return pageInfo;
    }

    @Override
    public Customer showCustomerById(Long id) {
        Customer customer = null;
        try{
            if(null != id){
                customer = iCustomerDao.selectById(id);
            }
        }catch (Exception e){
            log.error("根据用户id查看移动端用户出现异常",e);
        }
        return customer;
    }

    @Override
    public boolean updateCustomer(CustomerVo customerDto) {
        try{
            Customer customer = iCustomerDao.selectById(customerDto.getId());
            if(null != customer){
                customer.setName(customerDto.getName());
                customer.setPhone(customerDto.getPhone());
                customer.setIdCard(customerDto.getIdCard());
                customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
                customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            }
           return iCustomerDao.updateById(customer) > 0 ? true : false;
        }catch (Exception e){
            log.error("更新用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public boolean deleteCustomer(Long[] arrIds) {
        List<Long> listIds = null;
        try{
            if( null != arrIds && arrIds.length != 0){
                listIds = Arrays.asList(arrIds);
            }
            return iCustomerDao.deleteBatchIds(listIds) > 0 ? true:false;
        }catch (Exception e){
            log.error("删除用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public PageInfo<CustomerDto> findCustomer(SelectCustomerVo customerVo) {
        PageInfo<CustomerDto> pageInfo = null;
        try{
            if(customerVo.getCurrentPage() == null || customerVo.getCurrentPage() < 1){
                customerVo.setCurrentPage(1);
            }
            if(customerVo.getPageSize() == null || customerVo.getPageSize() < 1){
                customerVo.setPageSize(10);
            }
            PageHelper.startPage(customerVo.getCurrentPage(), customerVo.getPageSize());
            List<CustomerDto> customerVos = iCustomerDao.findCustomer(customerVo);
            pageInfo = new PageInfo<>(customerVos);
        }catch (Exception e){
            log.error("根据条件查询用户出现异常",e);
        }
        return pageInfo;
    }

    @Override
    public boolean saveKeyCustAndContract(KeyCustomerVo keyCustomerVo) {
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
            int num = iCustomerDao.insert(customer);
            if(num > 0){
                //合同集合
                int no = 0;
                List<CustomerContractVo> customerConList = keyCustomerVo.getCustContraVos();
                if(customerConList != null && customerConList.size() > 0){
                    for(CustomerContractVo vo : customerConList){
                        int i = saveCustomerContract(id , vo);
                        if(i > 0){
                            no ++;
                        }
                    }
                    if(no == customerConList.size()){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            log.error("新增大客户&合同出现异常",e);
            throw new CommonException("新增大客户&合同出现异常");
        }
        return false;
    }

    @Override
    public boolean deleteKeyCustomer(Long[] arrIds) {
        int num ;
        int no = 0;
        try{
            if( null != arrIds && arrIds.length > 0){
                List<Long> ids = Arrays.asList(arrIds);
                num = iCustomerDao.deleteBatchIds(ids);
                if(num > 0){
                    //循环删除大客户合同
                    for(Long custid : ids){
                        int i = iCustomerContractDao.deleteContractByCustomerId(custid);
                        if(i > 0){
                            no ++;
                        }
                    }
                    if(no == arrIds.length){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            log.error("删除大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<ListKeyCustomerDto> getAllKeyCustomer(BasePageVo pageVo) {
        PageInfo<ListKeyCustomerDto> pageInfo = null;
        try{
            if(pageVo.getCurrentPage() == null || pageVo.getCurrentPage() < 1){
                pageVo.setCurrentPage(1);
            }
            if(pageVo.getPageSize() == null || pageVo.getPageSize() < 1){
                pageVo.setPageSize(10);
            }
            PageHelper.startPage(pageVo.getCurrentPage(), pageVo.getPageSize());
            List<ListKeyCustomerDto> customerList = iCustomerDao.getAllKeyCustomter();
            pageInfo = new PageInfo<>(customerList);
        }catch (Exception e){
            log.error("分页查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public ShowKeyCustomerDto showKeyCustomerById(Long id) {
        ShowKeyCustomerDto sKeyCustomerDto = null;
        try{
            if(null != id){
                //根据主键id查询大客户
                Customer customer = iCustomerDao.selectById(id);
                //根据customer_id查询大客户的合同
                List<CustomerContractDto> contractDtos = iCustomerContractDao.getCustContractByCustId(id);
                sKeyCustomerDto = new ShowKeyCustomerDto();
                sKeyCustomerDto.setId(customer.getId());
                sKeyCustomerDto.setName(customer.getName());
                sKeyCustomerDto.setAlias(customer.getAlias());
                sKeyCustomerDto.setContactMan(customer.getContactMan());
                sKeyCustomerDto.setPhone(customer.getPhone());
                sKeyCustomerDto.setContactAddress(customer.getContactAddress());
                sKeyCustomerDto.setCompanyNature(customer.getCompanyNature());
                sKeyCustomerDto.setCompanyNature(customer.getCompanyNature());

                if(contractDtos != null && contractDtos.size() > 0){
                    for(CustomerContractDto dto : contractDtos){
                        dto.setContractLife(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getContractLife()),DATE_FORMAT));
                        dto.setDateOfProSign(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getDateOfProSign()),DATE_FORMAT));
                        if(StringUtils.isNotBlank(dto.getProjectEstabTime())){
                            dto.setProjectEstabTime(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getProjectEstabTime()),DATE_FORMAT));
                        }
                    }
                }
                sKeyCustomerDto.setCustContraVos(contractDtos);
            }
        }catch (Exception e){
            log.error("查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return sKeyCustomerDto;
    }

    @Override
    public boolean updateKeyCustomer(KeyCustomerVo keyCustomerVo) {
        try{
            Customer customer = iCustomerDao.selectById(keyCustomerVo.getId());
            if(null != customer){
                customer.setName(keyCustomerVo.getName());
                customer.setAlias(keyCustomerVo.getAlias());
                customer.setContactMan(keyCustomerVo.getContactMan());
                customer.setPhone(keyCustomerVo.getPhone());
                customer.setContactAddress(keyCustomerVo.getContactAddress());
                customer.setCustomerNature(keyCustomerVo.getCustomerNature());
                customer.setCompanyNature(keyCustomerVo.getCompanyNature());
                int num = iCustomerDao.updateById(customer);
                if(num > 0){
                    int no = 0;
                    List<CustomerContractVo> contractVos = keyCustomerVo.getCustContraVos();
                    if(null != contractVos && contractVos.size() > 0){
                        for(CustomerContractVo vo : contractVos){
                            int i = updateCustomerContractById(vo);
                            if(i > 0){
                                no ++;
                            }
                        }
                        if(no == contractVos.size()){
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("更新大客户&合同出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<ListKeyCustomerDto> findKeyCustomer(SelectKeyCustomerVo keyCustomerVo) {
        PageInfo<ListKeyCustomerDto> pageInfo = null;
        try{
            if(keyCustomerVo.getCurrentPage() == null || keyCustomerVo.getCurrentPage() < 1){
                keyCustomerVo.setCurrentPage(1);
            }
            if(keyCustomerVo.getPageSize() == null || keyCustomerVo.getPageSize() < 1){
                keyCustomerVo.setPageSize(10);
            }
            PageHelper.startPage(keyCustomerVo.getCurrentPage(), keyCustomerVo.getPageSize());
            List<ListKeyCustomerDto> keyCustomerList = iCustomerDao.findKeyCustomter(keyCustomerVo);
            pageInfo = new PageInfo<>(keyCustomerList);
        }catch (Exception e){
            log.error("根据条件查询大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }

    /**
     * 新增大客户合同
     * @param id  大客户id
     * @param vo  合同
     */
    private int saveCustomerContract(Long id ,CustomerContractVo vo){
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
            return iCustomerContractDao.insert(custCont);
        }catch (Exception e){
            log.error("新增合同出现异常",e);
            throw new CommonException("新增合同出现异常");
        }
    }

    private int updateCustomerContractById(CustomerContractVo vo){
        try{
            CustomerContract contract = iCustomerContractDao.selectById(vo.getId());
            if(null != contract){
                contract.setContractNo(vo.getContractNo());
                contract.setContactNature(vo.getContactNature());
                contract.setSettlePeriod(vo.getSettlePeriod());
                contract.setContractLife(LocalDateTimeUtil.convertToLong(vo.getProjectEstabTime(),DATE_FORMAT));
                contract.setProjectName(vo.getProjectName());
                contract.setProjectLevel(vo.getProjectLevel());
                contract.setMajorProduct(vo.getMajorProduct());
                contract.setProjectNature(vo.getProjectNature());
                contract.setDateOfProSign(LocalDateTimeUtil.convertToLong(vo.getDateOfProSign(),DATE_FORMAT));
                contract.setOneOffContract(vo.getOneOffContract());
                contract.setProTraVolume(vo.getProTraVolume());
                contract.setAvgMthTraVolume(vo.getAvgMthTraVolume());
                contract.setBusiCover(vo.getBusiCover());
                contract.setFixedRoute(vo.getFixedRoute());
                contract.setProjectDeper(vo.getProjectDeper());
                contract.setProjectLeader(vo.getProjectLeader());
                contract.setLeaderPhone(vo.getLeaderPhone());
                contract.setProjectStatus(vo.getProjectStatus());
                contract.setProjectTeamPer(vo.getProjectTeamPer());
                contract.setProjectEstabTime(LocalDateTimeUtil.convertToLong(vo.getProjectEstabTime(),DATE_FORMAT));
                contract.setMajorKpi(vo.getMajorKpi());
                return iCustomerContractDao.updateById(contract);
            }
        }catch (Exception e){
            log.error("更新合同出现异常",e);
            throw new CommonException("更新合同出现异常");
        }
        return 0;
    }

}
