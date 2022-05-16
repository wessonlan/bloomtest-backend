package com.pingguo.bloomtest.service;

/**
 * @author weikang.sang
 * @date 10:30 2022/5/11
 */
public interface UserService {
    /**
     * 判断用户是否存在，存在则返回true，不存在则返回false
     * @param username 用户名
     * @param password 用户密码
     * @return true,false
     */
    boolean isUserExist(String username, String password);
}
