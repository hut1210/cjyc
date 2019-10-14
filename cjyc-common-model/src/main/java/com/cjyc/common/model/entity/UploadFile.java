package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_upload_file")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件链接
     */
    private String fileUrl;

    /**
     * 服务器地址
     */
    private String serverUrl;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件归属 0：用户端banner
     */
    private Integer fileBelong;

    /**
     * 文件实际大小
     */
    private Integer fileSize;

    /**
     * 文件最大限制
     */
    private Integer limitSize;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 文件保存截止时间
     */
    private LocalDateTime endingTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0-可用
     */
    private Integer state;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 图片文件缩略图地址
     */
    private String thumbnailUrl;


}
