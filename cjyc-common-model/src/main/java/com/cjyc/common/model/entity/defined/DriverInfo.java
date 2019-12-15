package com.cjyc.common.model.entity.defined;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DriverInfo {
    private boolean isReAllotDriver;
    private boolean isOneDriver;

    private Long driverId;
    private String driverName;
    private String driverPhone;

}
