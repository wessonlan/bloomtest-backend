package com.pingguo.bloomtest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pingguo.bloomtest.dao.UserDAO;
import com.pingguo.bloomtest.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    /**
     * 判断用户是否存在，存在则返回true，不存在则返回false
     * @param username 用户名
     * @param password 用户密码
     * @return true,false
     */
    public boolean isUserExist(String username, String password) {
        //创建对象，泛型里加上实体对象
        QueryWrapper<User> wrapperUser = new QueryWrapper<>();
        // 先创建一个 hashmap，然后把多个条件put进去，再调用allEq
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        // 设置查询的条件
        wrapperUser.allEq(map);
        // 调用方法查询一个返回记录
        int count = userDAO.selectCount(wrapperUser);
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }
}
