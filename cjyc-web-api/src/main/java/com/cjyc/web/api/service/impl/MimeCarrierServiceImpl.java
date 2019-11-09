package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.web.api.service.IMimeCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MimeCarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements IMimeCarrierService {
}