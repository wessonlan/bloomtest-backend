package com.pingguo.bloomtest.mapper;

import com.pingguo.bloomtest.pojo.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserDemoMapper {
    // 查询
    User getUserById(Integer id);
    // 查询 使用多个参数
    User getUserByIdAndUsername(Integer id, String username);

    List<User> getUserByUsernameLike(String username);

    Map<String, Object> getUserByIdReturnMap(Integer id);
    @MapKey("id")
    Map<Integer, User> getUserByUsernameReturnMap(String username);

    User getUserByMap(Map<String, Object> map);
    // 新增
    boolean addUser(User user);
    // 修改
    boolean updateUser(User user);
    // 删除
    boolean deleteUser(Long id);
}
