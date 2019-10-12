package com.cjyc.common.service.service;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service
 * @date:2019/10/12
 */
public interface ICarSeriesService {

    List<Map<String,Object>> getAllList();

    List<Map<String,Object>> getByKeyword(String keyword);
}
