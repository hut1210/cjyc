package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.utils.SnowflakeIdWorker;
import com.cjyc.common.model.constant.PatternConstant;
import com.cjyc.common.model.dao.ICustomerContractDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.web.*;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.vo.web.CustomerContractVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
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

    @Resource
    private ICustomerContractDao iCustomerContractDao;

    @Override
    public boolean saveCustomer(CustomerDto customerDto) {
        try{
            Customer customer = new Customer();
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
    public PageInfo<Customer> getAllCustomer(BasePageDto basePageDto) {
        PageInfo<Customer> pageInfo = null;
        try{
            if(basePageDto.getCurrentPage() == null || basePageDto.getCurrentPage() < 1){
                basePageDto.setCurrentPage(1);
            }
            if(basePageDto.getPageSize() == null || basePageDto.getPageSize() < 1){
                basePageDto.setPageSize(10);
            }
            PageHelper.startPage(basePageDto.getCurrentPage(), basePageDto.getPageSize());
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
            if(id != null){
                customer = iCustomerDao.selectById(id);
            }
        }catch (Exception e){
            log.error("根据用户id查看移动端用户出现异常",e);
        }
        return customer;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        try{
            Customer customer = iCustomerDao.selectById(customerDto.getId());
            if(null != customer){
                customer.setName(customerDto.getName());
                customer.setPhone(customerDto.getPhone());
                customer.setIdCard(customerDto.getIdCard());
                customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
                customer.setIdCardBackImg(customerDto.getIdCardBackImg());
                return iCustomerDao.updateById(customer) > 0 ? true : false;
            }
        }catch (Exception e){
            log.error("更新用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delCustomerByIds(List<Long> ids) {
        try{
            if(ids != null && ids.size() > 0){
                return iCustomerDao.deleteBatchIds(ids) > 0 ? true:false;
            }
        }catch (Exception e){
            log.error("删除用户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public PageInfo<CustomerVo> findCustomer(SelectCustomerDto customerDto) {
        PageInfo<CustomerVo> pageInfo = null;
        try{
            if(customerDto.getCurrentPage() == null || customerDto.getCurrentPage() < 1){
                customerDto.setCurrentPage(1);
            }
            if(customerDto.getPageSize() == null || customerDto.getPageSize() < 1){
                customerDto.setPageSize(10);
            }
            PageHelper.startPage(customerDto.getCurrentPage(), customerDto.getPageSize());
            List<CustomerVo> customerVos = iCustomerDao.findCustomer(customerDto);
            pageInfo = new PageInfo<>(customerVos);
        }catch (Exception e){
            log.error("根据条件查询用户出现异常",e);
        }
        return pageInfo;
    }

    @Override
    public boolean saveKeyCustAndContract(KeyCustomerDto keyCustomerDto) {
        try{
            //新增大客户
            Customer customer = new Customer();
            customer.setName(keyCustomerDto.getName());
            customer.setAlias(keyCustomerDto.getAlias());
            customer.setContactMan(keyCustomerDto.getContactMan());
            customer.setPhone(keyCustomerDto.getPhone());
            customer.setContactAddress(keyCustomerDto.getContactAddress());
            customer.setCustomerNature(keyCustomerDto.getCustomerNature());
            customer.setCompanyNature(keyCustomerDto.getCompanyNature());
            customer.setType(2);
            int num = iCustomerDao.insert(customer);
            if(num > 0){
                //合同集合
                int no = 0;
                List<CustomerContractDto> customerConList = keyCustomerDto.getCustContraVos();
                if(customerConList != null && customerConList.size() > 0){
                    for(CustomerContractDto vo : customerConList){
                        int i = saveCustomerContract(customer.getId() , vo);
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
    public boolean delKeyCustomerByIds(List<Long> ids) {
        int num ;
        int no = 0;
        try{
            if( null != ids && ids.size() > 0){
                num = iCustomerDao.deleteBatchIds(ids);
                if(num > 0){
                    //循环删除大客户合同
                    for(Long custid : ids){
                        int i = iCustomerContractDao.deleteContractByCustomerId(custid);
                        if(i > 0){
                            no ++;
                        }
                    }
                    if(no == ids.size()){
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
    public PageInfo<ListKeyCustomerVo> getAllKeyCustomer(BasePageDto pageDto) {
        PageInfo<ListKeyCustomerVo> pageInfo = null;
        try{
            if(pageDto.getCurrentPage() == null || pageDto.getCurrentPage() < 1){
                pageDto.setCurrentPage(1);
            }
            if(pageDto.getPageSize() == null || pageDto.getPageSize() < 1){
                pageDto.setPageSize(10);
            }
            PageHelper.startPage(pageDto.getCurrentPage(), pageDto.getPageSize());
            List<ListKeyCustomerVo> customerList = iCustomerDao.getAllKeyCustomter();
            pageInfo = new PageInfo<>(customerList);
        }catch (Exception e){
            log.error("分页查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public ShowKeyCustomerVo showKeyCustomerById(Long id) {
        ShowKeyCustomerVo sKeyCustomerDto = null;
        try{
            if(id != null){
                //根据主键id查询大客户
                Customer customer = iCustomerDao.selectById(id);
                if(customer == null){
                    return sKeyCustomerDto;
                }
                //根据customer_id查询大客户的合同
                List<CustomerContractVo> contractDtos = iCustomerContractDao.getCustContractByCustId(id);
                sKeyCustomerDto = new ShowKeyCustomerVo();
                sKeyCustomerDto.setId(customer.getId());
                sKeyCustomerDto.setName(customer.getName());
                sKeyCustomerDto.setAlias(customer.getAlias());
                sKeyCustomerDto.setContactMan(customer.getContactMan());
                sKeyCustomerDto.setPhone(customer.getPhone());
                sKeyCustomerDto.setContactAddress(customer.getContactAddress());
                sKeyCustomerDto.setCompanyNature(customer.getCompanyNature());
                sKeyCustomerDto.setCompanyNature(customer.getCompanyNature());

                if(contractDtos != null && contractDtos.size() > 0){
                    for(CustomerContractVo dto : contractDtos){
                        dto.setContractLife(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getContractLife()), PatternConstant.SIMPLE_DATE_FORMAT));
                        dto.setDateOfProSign(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getDateOfProSign()),PatternConstant.SIMPLE_DATE_FORMAT));
                        if(StringUtils.isNotBlank(dto.getProjectEstabTime())){
                            dto.setProjectEstabTime(LocalDateTimeUtil.convertToString(Long.valueOf(dto.getProjectEstabTime()),PatternConstant.SIMPLE_DATE_FORMAT));
                        }
                    }
                    sKeyCustomerDto.setCustContraVos(contractDtos);
                }
            }
        }catch (Exception e){
            log.error("查看大客户出现异常",e);
            throw new CommonException(e.getMessage());
        }
        return sKeyCustomerDto;
    }

    @Override
    public boolean updateKeyCustomer(KeyCustomerDto keyCustomerDto) {
        try{
            Customer customer = iCustomerDao.selectById(keyCustomerDto.getId());
            if(null != customer){
                customer.setName(keyCustomerDto.getName());
                customer.setAlias(keyCustomerDto.getAlias());
                customer.setContactMan(keyCustomerDto.getContactMan());
                customer.setPhone(keyCustomerDto.getPhone());
                customer.setContactAddress(keyCustomerDto.getContactAddress());
                customer.setCustomerNature(keyCustomerDto.getCustomerNature());
                customer.setCompanyNature(keyCustomerDto.getCompanyNature());
                int num = iCustomerDao.updateById(customer);
                if(num > 0){
                    int no = 0;
                    List<CustomerContractDto> contractVos = keyCustomerDto.getCustContraVos();
                    if(null != contractVos && contractVos.size() > 0){
                        for(CustomerContractDto vo : contractVos){
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
    public PageInfo<ListKeyCustomerVo> findKeyCustomer(SelectKeyCustomerDto keyCustomerDto) {
        PageInfo<ListKeyCustomerVo> pageInfo = null;
        try{
            if(keyCustomerDto.getCurrentPage() == null || keyCustomerDto.getCurrentPage() < 1){
                keyCustomerDto.setCurrentPage(1);
            }
            if(keyCustomerDto.getPageSize() == null || keyCustomerDto.getPageSize() < 1){
                keyCustomerDto.setPageSize(10);
            }
            PageHelper.startPage(keyCustomerDto.getCurrentPage(), keyCustomerDto.getPageSize());
            List<ListKeyCustomerVo> keyCustomerList = iCustomerDao.findKeyCustomter(keyCustomerDto);
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
     * @param dto  合同
     */
    private int saveCustomerContract(Long id , CustomerContractDto dto){
        try{
            CustomerContract custCont = new CustomerContract();
            custCont.setId(SnowflakeIdWorker.nextId());
            custCont.setCustomerId(id);
            custCont.setContractNo(dto.getContractNo());
            custCont.setContactNature(dto.getContactNature());
            custCont.setContractLife(LocalDateTimeUtil.convertToLong(dto.getContractLife(),PatternConstant.SIMPLE_DATE_FORMAT));
            custCont.setProjectName(dto.getProjectName());
            custCont.setProjectLevel(dto.getProjectLevel());
            custCont.setMajorProduct(dto.getMajorProduct());
            custCont.setProjectNature(dto.getProjectNature());
            custCont.setDateOfProSign(LocalDateTimeUtil.convertToLong(dto.getDateOfProSign(),PatternConstant.SIMPLE_DATE_FORMAT));
            custCont.setOneOffContract(dto.getOneOffContract());
            custCont.setProTraVolume(dto.getProTraVolume());
            custCont.setAvgMthTraVolume(dto.getAvgMthTraVolume());
            custCont.setBusiCover(dto.getBusiCover());
            custCont.setFixedRoute(dto.getFixedRoute());
            custCont.setProjectDeper(dto.getProjectDeper());
            custCont.setProjectLeader(dto.getProjectLeader());
            custCont.setLeaderPhone(dto.getLeaderPhone());
            custCont.setProjectStatus(dto.getProjectStatus());
            custCont.setProjectTeamPer(dto.getProjectTeamPer());
            custCont.setProjectEstabTime(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(),PatternConstant.SIMPLE_DATE_FORMAT));
            custCont.setMajorKpi(dto.getMajorKpi());
            return iCustomerContractDao.insert(custCont);
        }catch (Exception e){
            log.error("新增合同出现异常",e);
            throw new CommonException("新增合同出现异常");
        }
    }

    private int updateCustomerContractById(CustomerContractDto dto){
        try{
            CustomerContract contract = iCustomerContractDao.selectById(dto.getId());
            if(null != contract){
                contract.setContractNo(dto.getContractNo());
                contract.setContactNature(dto.getContactNature());
                contract.setSettlePeriod(dto.getSettlePeriod());
                contract.setContractLife(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(),PatternConstant.SIMPLE_DATE_FORMAT));
                contract.setProjectName(dto.getProjectName());
                contract.setProjectLevel(dto.getProjectLevel());
                contract.setMajorProduct(dto.getMajorProduct());
                contract.setProjectNature(dto.getProjectNature());
                contract.setDateOfProSign(LocalDateTimeUtil.convertToLong(dto.getDateOfProSign(),PatternConstant.SIMPLE_DATE_FORMAT));
                contract.setOneOffContract(dto.getOneOffContract());
                contract.setProTraVolume(dto.getProTraVolume());
                contract.setAvgMthTraVolume(dto.getAvgMthTraVolume());
                contract.setBusiCover(dto.getBusiCover());
                contract.setFixedRoute(dto.getFixedRoute());
                contract.setProjectDeper(dto.getProjectDeper());
                contract.setProjectLeader(dto.getProjectLeader());
                contract.setLeaderPhone(dto.getLeaderPhone());
                contract.setProjectStatus(dto.getProjectStatus());
                contract.setProjectTeamPer(dto.getProjectTeamPer());
                contract.setProjectEstabTime(LocalDateTimeUtil.convertToLong(dto.getProjectEstabTime(),PatternConstant.SIMPLE_DATE_FORMAT));
                contract.setMajorKpi(dto.getMajorKpi());
                return iCustomerContractDao.updateById(contract);
            }
        }catch (Exception e){
            log.error("更新合同出现异常",e);
            throw new CommonException("更新合同出现异常");
        }
        return 0;
    }

}
