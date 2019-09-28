package com.cjyc.web.api.dto.req;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @auther litan
 * @description: com.cjyc.web.api.dto.req
 * @date:2019/9/28
 */
public class BaseReq implements Serializable{

    private static final long serialVersionUID = 6843636637246070777L;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            Class t = this.getClass();
            Field[] fields = t.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                sb.append("{");
                sb.append(field.getName());
                sb.append(":");
                if (field.getType() == Integer.class) {
                    sb.append(field.getInt(this));
                } else if (field.getType() == Long.class) {
                    sb.append(field.getLong(this));
                } else if (field.getType() == Boolean.class) {
                    sb.append(field.getBoolean(this));
                } else if (field.getType() == char.class) {
                    sb.append(field.getChar(this));
                } else if (field.getType() == Double.class) {
                    sb.append(field.getDouble(this));
                } else if (field.getType() == Float.class) {
                    sb.append(field.getFloat(this));
                } else
                    sb.append(field.get(this));
                sb.append("}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
