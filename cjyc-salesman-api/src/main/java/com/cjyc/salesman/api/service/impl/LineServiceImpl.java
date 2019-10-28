package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.salesman.api.service.ILineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班线管理 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class LineServiceImpl extends ServiceImpl<ILineDao, Line> implements ILineService {

}
