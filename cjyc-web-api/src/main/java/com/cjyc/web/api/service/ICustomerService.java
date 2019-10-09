package com.cjyc.web.api.service;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.CustomerDto;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  {

    /**
     *  新增移动端用户
     * @param customerDto
     * @return
     */
    ResultVo saveCustomer(CustomerDto customerDto);

    /**
     * 分页查询移动端用户
     * @param pageNo  当前页
     * @param pageSize 每页大小
     * @return
     */
    ResultVo getAllCustomer(Integer pageNo,Integer pageSize);

    /**
     * 根据用户id查看移动端用户
     * @param id
     * @return
     */
    ResultVo showCustomerById(Long id);

    /**
     * 更新移动端用户
     * @param customerDto
     * @return
     */
    ResultVo updateCustomer(CustomerDto customerDto);

    /**
     * 根据id删除移动端用户
     * @param arrIds
     * @return
     */
    ResultVo deleteCustomer(Long[] arrIds);

    /**
     * 根据条件查询移动端用户
     * @param jsonObject
     * @return
     */
    ResultVo findCustomer(JSONObject jsonObject);

}
