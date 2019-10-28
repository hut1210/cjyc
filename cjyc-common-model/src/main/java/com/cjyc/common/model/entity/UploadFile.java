package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_upload_file")
@ApiModel(value="UploadFile对象", description="")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文件链接")
    private String fileUrl;

    @ApiModelProperty(value = "服务器地址")
    private String serverUrl;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件归属 0：用户端banner")
    private Integer fileBelong;

    @ApiModelProperty(value = "文件实际大小")
    private Integer fileSize;

    @ApiModelProperty(value = "文件最大限制")
    private Integer limitSize;

    @ApiModelProperty(value = "上传时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "文件保存截止时间")
    private LocalDateTime endingTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 0-可用")
    private Integer state;

    @ApiModelProperty(value = "原始文件名")
    private String originalName;

    @ApiModelProperty(value = "图片文件缩略图地址")
    private String thumbnailUrl;


}
