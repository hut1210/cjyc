package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.foreign.api.dao.ILineDao;
import com.cjyc.foreign.api.entity.Line;
import com.cjyc.foreign.api.service.ILineService;
import org.springframework.stereotype.Service;

/**
 * @Description 班线业务接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:05
 **/
@Service
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {

}
