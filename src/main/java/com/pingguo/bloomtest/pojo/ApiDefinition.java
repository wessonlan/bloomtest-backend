package com.pingguo.bloomtest.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("api_definition")
public class ApiDefinition {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String name;
    private String method;
    private String path;
    private String host;
    private String description;
    private String moduleId;
    private int requestType;

    private String apiHeader;

    private String request;

    private String response;

    private String createUser;
    @TableField(fill = FieldFill.INSERT)        // 新增的时候填充数据
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增或修改的时候填充数据
    private Date updateTime;
}
