package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 品牌车系业务接口
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:01
 **/
public interface ICarSeriesService extends IService<CarSeries> {

    /**
     * 新增
     * @param carSeriesAddDto
     * @return
     */
    boolean add(CarSeriesAddDto carSeriesAddDto);

    /**
     * 修改
     * @param carSeriesUpdateDto
     * @return
     */
    boolean modify(CarSeriesUpdateDto carSeriesUpdateDto);

    /**
     * 分页查询
     * @param carSeriesQueryDto
     * @return
     */
    ResultVo queryPage(CarSeriesQueryDto carSeriesQueryDto);

    /**
     * 导入Excel文件
     * @param file
     * @return
     */
    boolean importExcel(MultipartFile file,Long createUserId);

    /**
     * 导出Excel表格
     * @param request
     * @param response
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询所有品牌
     * @return
     */
    ResultVo<List<CarSeries>> queryAll();

    ResultVo<List<CarSeriesTree>> tree();

}
