package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运单表(业务员调度单)
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_waybill")
public class Waybill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 运单编号
     */
    private String waybillNo;

    /**
     * 运单类型：1提车运单，2送车运单，8干线运单
     */
    private Integer type;

    /**
     * 调度类型：1自己处理，2人工调度
     */
    private Integer dispatchType;

    /**
     * 指导线路
     */
    private String guideLine;

    /**
     * 推荐线路
     */
    private String recommendLine;

    /**
     * 承运商ID
     */
    private Long carrierId;

    /**
     * 车数量
     */
    private Integer carNum;

    /**
     * 运单状态：
0待分配承运商（竞抢），
15待承运商承接任务，
30运输中，
100已完成，
102已撤回，
103已拒接，
111超时关闭
     */
    private Integer state;

    /**
     * 运单总运费
     */
    private BigDecimal freightFee;

    /**
     * 运费支付状态
     */
    private Integer freightPayState;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 运费支付时间
     */
    private String freightPayTime;

    /**
     * 运费支付流水单号
     */
    private String freightPayBillno;

    /**
     * 调度人
     */
    private String createUser;

    /**
     * 调度人ID
     */
    private Long createUserId;


}
