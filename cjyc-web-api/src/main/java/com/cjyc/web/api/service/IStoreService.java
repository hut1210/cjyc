package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 韵车业务中心信息表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
public interface IStoreService extends IService<Store> {

    List<Store> getByAreaCode(String areaCode);

    /**
     * 分页查询
     * @param storeQueryDto
     * @return
     */
    ResultVo queryPage(StoreQueryDto storeQueryDto);

    /**
     * 新增
     * @param storeAddDto
     * @return
     */
    boolean add(StoreAddDto storeAddDto);

    /**
     * 修改
     * @param storeUpdateDto
     * @return
     */
    boolean modify(StoreUpdateDto storeUpdateDto);

    /**
     * 导出Excel
     * @param request
     * @param response
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response);
}
