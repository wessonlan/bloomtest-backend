package com.pingguo.bloomtest.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pingguo.bloomtest.pojo.ApiModule;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApiModuleDAO extends BaseMapper<ApiModule> {
}
