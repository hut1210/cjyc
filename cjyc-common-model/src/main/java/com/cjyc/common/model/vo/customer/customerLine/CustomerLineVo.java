package com.cjyc.common.model.vo.customer.customerLine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class CustomerLineVo implements Serializable {
    private static final long serialVersionUID = 2120381343345734965L;

    @ApiModelProperty("线路id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("起始地址")
    private String startAdress;

    @ApiModelProperty("起始地联系人")
    private String startContact;

    @ApiModelProperty("起始地联系人电话")
    private String startContactPhone;

    @ApiModelProperty("目的地地址")
    private String endAdress;

    @ApiModelProperty("目的地联系人")
    private String endContact;

    @ApiModelProperty("目的地联系人电话")
    private String endContactPhone;

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }
        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if(obj instanceof CustomerLineVo){
            CustomerLineVo other = (CustomerLineVo) obj;
            //需要比较的字段相等，则这两个对象相等
            if(equalsStr(this.startAdress, other.startAdress)
                    && equalsStr(this.startContact, other.startContact)
                    && equalsStr(this.startContactPhone, other.startContactPhone)
                    && equalsStr(this.endAdress, other.endAdress)
                    && equalsStr(this.endContact, other.endContact)
                    && equalsStr(this.endContactPhone, other.endContactPhone)
            ){
                return true;
            }
        }
        return false;
    }
    private boolean equalsStr(String str1, String str2){
        if(StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)){
            return true;
        }
        if(!StringUtils.isEmpty(str1) && str1.equals(str2)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (startAdress == null ? 0 : startAdress.hashCode());
        result = 31 * result + (startContact == null ? 0 : startContact.hashCode());
        result = 31 * result + (startContactPhone == null ? 0 : startContactPhone.hashCode());
        result = 31 * result + (endAdress == null ? 0 : endAdress.hashCode());
        result = 31 * result + (endContact == null ? 0 : endContact.hashCode());
        result = 31 * result + (endContactPhone == null ? 0 : endContactPhone.hashCode());
        return result;
    }

    public String getStartAdress(){return StringUtils.isBlank(startAdress) ? "":startAdress; }
    public String getStartContact(){return StringUtils.isBlank(startContact) ? "":startContact; }
    public String getStartContactPhone(){return StringUtils.isBlank(startContactPhone) ? "":startContactPhone; }
    public String getEndAdress(){return StringUtils.isBlank(endAdress) ? "":endAdress; }
    public String getEndContact(){return StringUtils.isBlank(endContact) ? "":endContact; }
    public String getEndContactPhone(){return StringUtils.isBlank(endContactPhone) ? "":endContactPhone; }
}