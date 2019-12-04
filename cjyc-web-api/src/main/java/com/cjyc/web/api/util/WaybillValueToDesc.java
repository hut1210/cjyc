package com.cjyc.web.api.util;

/**
 * 运单值 显示内容转换
 */
public class WaybillValueToDesc {
    private WaybillValueToDesc() {

    }

    /**
     * 运单状态值转为文字描述
     * @param state
     * @return
     */
    public static String convertStateToDesc(Integer state) {
        //运单车辆状态：0待指派，2已指派，5待装车，15待装车确认，45已装车，
        // 70已卸车，90确认交车, 100确认收车, 105待重连，120已重连
        switch (state) {
            case 0:
                return "待指派";
            case 2:
                return "已指派";
            case 5:
                return "待装车";
            case 15:
                return "待装车确认";
            case 45:
                return "已装车";
            case 70:
                return "已卸车";
            case 90:
                return "确认交车";
            case 100:
                return "确认收车";
            case 105:
                return "待重连";
            case 120:
                return "已重连";
            default:
                return null;
        }
    }

    /**
     * 运单类型 文本转换
     * @param type
     * @return
     */
    public static String convertTypeToDesc(Integer type) {
        //运单类型：1提车运单，2干线运单，3送车运单
        String typeDesc;
        switch (type) {
            case 1:
                return "提车运单";
            case 2:
                return "干线运单";
            case 3:
                return "送车运单";
            default:
                return null;
        }
    }

    /**
     * 是否新车 文本转换
     * @param isNewFlag
     * @return
     */
    public static String convertIsNewDesc(Integer isNewFlag) {
        switch (isNewFlag) {
            case 0:
                return "否";
            case 1:
                return "是";
            default:
                return null;
        }
    }
}
