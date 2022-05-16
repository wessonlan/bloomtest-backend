package com.pingguo.bloomtest.controller;

import com.pingguo.bloomtest.common.Result;
import com.pingguo.bloomtest.entity.request.UserRequest;
import com.pingguo.bloomtest.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public Result login(@RequestBody UserRequest user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        if (userServiceImpl.isUserExist(username, password)) {
            Map<String, Object> userToken = new HashMap<>(4);
            userToken.put("token","admin-token");
            return Result.success(userToken);
        } else {
            return Result.fail("用户名或密码错误");
        }
    }


    @GetMapping("/useInfo")
    public Result useInfo(HttpServletRequest request) throws Exception {
        String token = request.getParameter("token");
        Map<String, Object> result = new HashMap<>(8);
        ArrayList roles = new ArrayList<>();
        String allowableToken = "admin-token";
        if (token.equals(allowableToken)) {
            roles.add("admin");
            result.put("roles", roles);
            result.put("introduction", "我是超级管理员");
            result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }
        return Result.success(result);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/logout")
    public Result logout() throws Exception {
        return Result.success();
    }
}
