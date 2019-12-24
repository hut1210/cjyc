package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.salesman.AddDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleNewDto;
import com.cjyc.common.model.dto.web.salesman.ResetStateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.vo.ResultVo;

/**
 * 业务员service
 */
public interface ISalesmanService extends IService<Admin> {
    /**
     * 保存或更新业务员信息
     * @param dto
     * @return
     */
    ResultVo saveAdmin(AddDto dto);

    /**
     * 给业务员分配角色
     * @param dto
     * @return
     */
    ResultVo assignRoles(AssignRoleDto dto);

    /**
     * 设置用户状态
     * @param dto
     * @return
     */
    ResultVo resetState(ResetStateDto dto);

    /**
     * 用户密码重置
     * @param id
     * @return
     */
    ResultVo resetPwd(Long id);

    /************************************韵车集成改版 st***********************************/
    ResultVo assignRolesNew(AssignRoleNewDto dto);
    /************************************韵车集成改版 ed***********************************/
}
