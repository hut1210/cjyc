package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesImportExcel;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ExcelUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesExportExcel;
import com.cjyc.web.api.service.ICarSeriesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 品牌车系业务接口实现类
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:03
 **/
@Slf4j
@Service
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao,CarSeries> implements ICarSeriesService {
    @Override
    public ResultVo add(CarSeriesAddDto carSeriesAddDto) {
        // 唯一性校验
        List<CarSeries> list = super.list(new QueryWrapper<CarSeries>().lambda()
                .eq(CarSeries::getBrand, carSeriesAddDto.getBrand()).eq(CarSeries::getModel, carSeriesAddDto.getModel()));
        if(!CollectionUtils.isEmpty(list))
            return BaseResultUtil.getVo(ResultEnum.EXIST_CARSERIES.getCode(),ResultEnum.EXIST_CARSERIES.getMsg());

        // 保存数据
        CarSeries carSeries = getCarSeries(carSeriesAddDto);
        boolean result = super.save(carSeries);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    private CarSeries getCarSeries(CarSeriesAddDto carSeriesAddDto) {
        CarSeries carSeries = new CarSeries();
        carSeries.setCarCode(StringUtil.getUUID());
        carSeries.setBrand(carSeriesAddDto.getBrand());
        carSeries.setModel(carSeriesAddDto.getModel());
        carSeries.setPinInitial(StringUtil.getFirstPinYinHeadChar(carSeriesAddDto.getBrand()));
        carSeries.setLogoImg(StringUtil.getCarLogoURL(carSeriesAddDto.getBrand()));
        carSeries.setCreateTime(System.currentTimeMillis());
        carSeries.setCreateUserId(carSeriesAddDto.getCreateUserId());
        return carSeries;
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
        List<CarSeries> list = getCarSeriesList(carSeriesQueryDto);
        PageInfo<CarSeries> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    private List<CarSeries> getCarSeriesList(CarSeriesQueryDto carSeriesQueryDto) {
        PageHelper.startPage(carSeriesQueryDto.getCurrentPage(), carSeriesQueryDto.getPageSize());
        LambdaQueryWrapper<CarSeries> queryWrapper = new QueryWrapper<CarSeries>().lambda()
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getBrand()),CarSeries::getBrand,carSeriesQueryDto.getBrand())
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getModel()),CarSeries::getModel,carSeriesQueryDto.getModel());
        return super.list(queryWrapper);
    }

    @Override
    public ResultVo importExcel(MultipartFile file,Long createUserId) {
        ResultVo resultVo = null;
        try {
            List<CarSeriesImportExcel> seriesImportExcelList = ExcelUtil.importExcel(file,
                    1, 1, CarSeriesImportExcel.class,true);
            if (!CollectionUtils.isEmpty(seriesImportExcelList)) {
                List<CarSeries> list = new ArrayList<>(10);
                for (CarSeriesImportExcel carSeriesImportExcel : seriesImportExcelList) {
                    // 唯一性校验
                    String brand = carSeriesImportExcel.getBrand();
                    String model = carSeriesImportExcel.getModel();
                    List<CarSeries> resultList = super.list(new QueryWrapper<CarSeries>().lambda()
                            .eq(CarSeries::getBrand, brand).eq(CarSeries::getModel, model));
                    if(!CollectionUtils.isEmpty(resultList))
                        return BaseResultUtil.fail(brand + "-" + model + "已经存在,不能重复添加");
                    CarSeries carSeries = getCarSeries(createUserId, carSeriesImportExcel);
                    list.add(carSeries);
                }
                boolean saveBatch = super.saveBatch(list);
                resultVo = saveBatch ? BaseResultUtil.success() : BaseResultUtil.fail();
            } else {
                log.error("===导入数据为空===");
                resultVo = BaseResultUtil.fail("导入数据不能为空");
            }
        } catch (Exception e) {
            log.error("导入品牌车系异常:",e);
            resultVo = BaseResultUtil.fail("导入失败");
        }

        return resultVo;
    }

    private CarSeries getCarSeries(Long createUserId, CarSeriesImportExcel carSeriesImportExcel) {
        CarSeries carSeries = new CarSeries();
        String brand = carSeriesImportExcel.getBrand();
        carSeries.setBrand(brand);
        carSeries.setModel(carSeriesImportExcel.getModel());
        carSeries.setCarCode(StringUtil.getUUID());
        carSeries.setPinInitial(StringUtil.getFirstPinYinHeadChar(brand));
        carSeries.setLogoImg(StringUtil.getCarLogoURL(brand));
        carSeries.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        carSeries.setCreateUserId(createUserId);
        return carSeries;
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
                log.error("导出品牌车系异常:",e);
            }
        }
    }

    @Override
    public ResultVo queryAll() {
        List<CarSeries> list = super.list(new QueryWrapper<CarSeries>().lambda().groupBy(CarSeries::getBrand).select(CarSeries::getBrand));
        List<String> brandList = new ArrayList<>(100);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(e->brandList.add(e.getBrand()));
        }
        return BaseResultUtil.success(brandList);
    }

    @Override
    public ResultVo getModelByBrand(String brand) {
        List<CarSeries> list = super.list(new QueryWrapper<CarSeries>().lambda().eq(CarSeries::getBrand, brand).select(CarSeries::getModel));
        List<String> modelList = new ArrayList<>(20);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(e->modelList.add(e.getModel()));
        }
        return BaseResultUtil.success(modelList);
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
