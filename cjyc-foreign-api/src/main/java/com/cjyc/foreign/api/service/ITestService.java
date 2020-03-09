package com.cjyc.foreign.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.foreign.api.entity.Line;

public interface ITestService extends IService<Line> {
    Line getLineById(Long id);
}
