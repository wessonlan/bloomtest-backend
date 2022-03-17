package com.pingguo.bloomtest.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Project {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectName;

    private String description;

    @TableField(fill = FieldFill.INSERT)        // 新增的时候填充数据
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增或修改的时候填充数据
    private Date updateTime;

    private String createUser;

}
