package com.cjyc.common.model.vo;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * 返回前端数据结构
 *
 * @author JPG
 */
@Builder
@Getter
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 2L;
    /**返回码*/
    private int code;
    /**返回提示信息*/
    private String message;
    /**
     * 返回内容：自定义对象、集合、List&统计信息{@link ListVo} 、分页&统计{@link PageVo}
     */
    private T data;

}
