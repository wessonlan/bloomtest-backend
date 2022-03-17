package com.pingguo.bloomtest.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


@Data
@TableName("api_module")
public class ApiModule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String name;

    private Long parentId;

    private Integer level;

    private Double pos;

    @TableField(fill = FieldFill.INSERT)        // 新增的时候填充数据
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增或修改的时候填充数据
    private Date updateTime;

}
