package com.yqzl.service.impl;

import com.yqzl.model.Body;
import com.yqzl.model.Head;
import com.yqzl.model.OutTransfer;
import com.yqzl.service.IYqzlService;
import com.yqzl.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/29 14:17
 */
@Service
@Slf4j
public class YqzlServiceImpl implements IYqzlService {


    @Override
    public void transfer() {
        Head head =new Head("210201","","","","","","");

        StringBuilder msg = new StringBuilder(head.getHead(head));

        OutTransfer outTransfer =  new OutTransfer("","","","","",
                "","","",new BigDecimal(0),"","","","");
        msg.append(Body.getOutTransferBody(outTransfer));

        SocketUtil.doSocket(msg.toString());
    }
}
