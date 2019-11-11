package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesImportExcel;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesExportExcel;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.web.api.service.ICarSeriesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Description 品牌车系业务接口实现类
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:03
 **/
@Slf4j
@Service
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao,CarSeries> implements ICarSeriesService {

    @Resource
    private ICarSeriesDao carSeriesDao;

    @Override
    public boolean add(CarSeriesAddDto carSeriesAddDto) {
        List<CarSeries> list = new ArrayList<>(10);
        String dtoModel = carSeriesAddDto.getModel();
        if (dtoModel.contains("，")) {
            dtoModel = dtoModel.replaceAll("，", ",");
        }
        String[] models = dtoModel.split(",");
        for (String model : models) {
            CarSeries carSeries = new CarSeries();
            carSeries.setCarCode(StringUtil.getUUID());
            carSeries.setBrand(carSeriesAddDto.getBrand());
            carSeries.setModel(model);
            carSeries.setLogoImg(StringUtil.getCarLogoURL(carSeriesAddDto.getBrand()));
            carSeries.setCreateTime(System.currentTimeMillis());
            carSeries.setCreateUserId(carSeriesAddDto.getCreateUserId());
            list.add(carSeries);
        }
        return super.saveBatch(list);
    }

    @Override
    public boolean modify(CarSeriesUpdateDto carSeriesUpdateDto) {
        CarSeries carSeries = new CarSeries();
        BeanUtils.copyProperties(carSeriesUpdateDto,carSeries);
        carSeries.setUpdateTime(System.currentTimeMillis());
        return super.updateById(carSeries);
    }

    @Override
    public ResultVo queryPage(CarSeriesQueryDto carSeriesQueryDto) {
        BasePageUtil.initPage(carSeriesQueryDto);
        List<CarSeries> list = getCarSeriesList(carSeriesQueryDto);
        PageInfo<CarSeries> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    private List<CarSeries> getCarSeriesList(CarSeriesQueryDto carSeriesQueryDto) {
        PageHelper.startPage(carSeriesQueryDto.getCurrentPage(), carSeriesQueryDto.getPageSize(), true);
        LambdaQueryWrapper<CarSeries> queryWrapper = new QueryWrapper<CarSeries>().lambda()
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getBrand()),CarSeries::getBrand,carSeriesQueryDto.getBrand())
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getModel()),CarSeries::getModel,carSeriesQueryDto.getModel());
        return super.list(queryWrapper);
    }

    @Override
    public boolean importExcel(MultipartFile file,Long createUserId) {
        boolean result;
        try {
            List<CarSeriesImportExcel> seriesImportExcelList = ExcelUtil.importExcel(file, 1, 1, CarSeriesImportExcel.class);
            if (!CollectionUtils.isEmpty(seriesImportExcelList)) {
                List<CarSeries> list = new ArrayList<>(10);
                for (CarSeriesImportExcel carSeriesImportExcel : seriesImportExcelList) {
                    CarSeries carSeries = new CarSeries();
                    BeanUtils.copyProperties(carSeriesImportExcel,carSeries);
                    carSeries.setCarCode(StringUtil.getUUID());
                    carSeries.setLogoImg(StringUtil.getCarLogoURL(carSeries.getBrand()));
                    carSeries.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                    carSeries.setCreateUserId(createUserId);
                    list.add(carSeries);
                }
                result = super.saveBatch(list);
            } else {
                result = false;
            }
        } catch (Exception e) {
            log.error("导入品牌车系失败异常:{}",e);
            result = false;
        }
        return result;
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        CarSeriesQueryDto carSeriesQueryDto = getCarSeriesQueryDto(request);
        // 查询列表
        List<CarSeries> list = getCarSeriesList(carSeriesQueryDto);

        if (!CollectionUtils.isEmpty(list)) {
            // 生成导出数据
            List<CarSeriesExportExcel> exportExcelList = new ArrayList<>(10);
            for (CarSeries carSeries : list) {
                CarSeriesExportExcel carSeriesExportExcel = new CarSeriesExportExcel();
                BeanUtils.copyProperties(carSeries, carSeriesExportExcel);
                exportExcelList.add(carSeriesExportExcel);
            }
            String title = "品牌车系";
            String sheetName = "品牌车系";
            String fileName = "品牌车系表.xls";
            try {
                ExcelUtil.exportExcel(exportExcelList, title, sheetName, CarSeriesExportExcel.class, fileName, response);
            } catch (IOException e) {
                log.error("导出品牌车系异常:{}",e);
            }
        }
    }

    @Override
    public ResultVo<List<CarSeries>> queryAll() {
        List<CarSeries> list = super.list();
        // 去除重复的品牌
        if (!CollectionUtils.isEmpty(list)) {
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                    ->new TreeSet<>(Comparator.comparing(CarSeries::getBrand))), ArrayList::new));
        }
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<List<CarSeriesTree>> tree() {
        return BaseResultUtil.success(carSeriesDao.findTree());
    }

    private CarSeriesQueryDto getCarSeriesQueryDto(HttpServletRequest request) {
        CarSeriesQueryDto carSeriesQueryDto = new CarSeriesQueryDto();
        carSeriesQueryDto.setCurrentPage(Integer.valueOf(request.getParameter("currentPage")));
        carSeriesQueryDto.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
        carSeriesQueryDto.setBrand(request.getParameter("brand"));
        carSeriesQueryDto.setModel(request.getParameter("model"));
        return carSeriesQueryDto;
    }
}
