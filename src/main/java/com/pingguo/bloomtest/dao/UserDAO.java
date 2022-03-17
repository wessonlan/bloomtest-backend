package com.pingguo.bloomtest.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pingguo.bloomtest.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends BaseMapper<User> {

}
