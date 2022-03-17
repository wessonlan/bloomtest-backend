package com.pingguo.bloomtest.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("User2")
public class User {
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String username;
    private String password;

    @TableField(fill = FieldFill.INSERT)        // 新增的时候填充数据
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增或修改的时候填充数据
    private Date updateTime;
}
