package com.cjyc.web.api.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddDeptReq;
import com.cjkj.usercenter.dto.common.AddDeptResp;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * sys组织机构接口
 * @author JPG
 */
@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysDeptService {

    /**
     * 保存组织机构
     * @author JPG
     * @since 2019/10/21 9:38
     * @param addDeptReq 参数
     */
    @PostMapping("/feign/uc/addDept")
    ResultData<AddDeptResp> save(@RequestBody AddDeptReq addDeptReq);

    /**
     * 修改组织机构
     * @author JPG
     * @since 2019/10/21 9:38
     * @param addDeptReq 参数
     */
    @PostMapping("/feign/uc/updateDept")
    ResultData update(@RequestBody AddDeptReq addDeptReq);

    /**
     * 修改组织机构
     * @author JPG
     * @param deptId 组织机构ID
     */
    @PostMapping("/feign/uc/deleteDept/{deptId}")
    ResultData update(@PathVariable String deptId);

    /**
     * 查询机构信息
     * @author JPG
     * @since 2019/10/21 9:38
     * @param deptId 组织机构ID
     */
    @GetMapping("/feign/uc/getDept/{deptId}")
    ResultData<SelectDeptResp> getDept(@PathVariable String deptId);

    /**
     * 查询当前机构直接子列表信息
     * @param deptId
     * @return
     */
    @GetMapping("/feign/uc/getSingleLevelDeptList/{deptId}")
    ResultData<List<SelectDeptResp>> getSingleLevelDeptList(@PathVariable Long deptId);

}
