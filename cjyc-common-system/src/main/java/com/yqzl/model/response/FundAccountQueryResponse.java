package com.yqzl.model.response;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 交行账户信息查询数据封装对象
 *
 * @author RenPL 2020-3-15
 */
@Data
public class FundAccountQueryResponse {

    /**
     * 户名
     */
    private String acName;

    /**
     * 账号
     */
    private String acNo;

    /**
     * 币种
     */
    private String curCode;

    /**
     * 户名
     */
    private String data;

    /**
     * 余额
     */
    private String balanceAmt;

    /**
     * 可用余额
     */
    private String canUseBalanceAmt;

    /**
     * 开户日期
     */
    private String openAcNoDate;

    /**
     * 账户类型
     */
    private String acType;

    /**
     * 开户行
     */
    private String acBankName;

    /**
     * 错误信息
     */
    private String transInfo;

    /**
     * 成功标
     */
    private String transFlag;

    public FundAccountQueryResponse(String data) {
        this.data = data;
    }

    public String getAcName() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                acName = arr[10];
            }
        }
        return acName;
    }

    public String getAcNo() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                acNo = arr[11];
            }
        }
        return acNo;
    }

    public String getCurCode() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                curCode = arr[12];
            }
        }
        return curCode;
    }

    public String getData() {
        return data;
    }

    public String getBalanceAmt() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                balanceAmt = arr[13];
            }
        }
        return balanceAmt;
    }

    public String getCanUseBalanceAmt() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                canUseBalanceAmt = arr[14];
            }
        }
        return canUseBalanceAmt;
    }

    public String getOpenAcNoDate() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                openAcNoDate = arr[15];
            }
        }
        return openAcNoDate;
    }

    public String getAcType() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                acType = arr[16];
            }
        }
        return acType;
    }

    public String getAcBankName() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                acBankName = arr[17];
            }
        }
        return acBankName;
    }

    public String getTransInfo() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                transInfo = arr[18];
            }
        }
        return transInfo;
    }

    public String getTransFlag() {
        if (!StringUtils.isEmpty(data)) {
            String[] arr = data.split("\\|");
            if (arr != null && arr.length > 19) {
                transFlag = arr[19];
            }
        }
        return transFlag;
    }
}
