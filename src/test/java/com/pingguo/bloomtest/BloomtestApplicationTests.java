package com.pingguo.bloomtest;

import cn.hutool.core.lang.Console;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.pingguo.bloomtest.dao.UserDAO;
import com.pingguo.bloomtest.pojo.User;
import com.pingguo.bloomtest.service.ApiModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BloomtestApplicationTests {



    //    get1
    @Test
    void get1() {
        String result1 = HttpUtil.get("http://localhost:8080/bloomtest/user/useInfo?token=admin-token");
        System.out.println(result1);
    }
    //    get2
    @Test
    void get2() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", 33);
        String result2 = HttpRequest.get("http://localhost:8080/bloomtest/apiDefinition/getApi")
                    .form(paramMap)
                    .execute()
                    .body();
        System.out.println(result2);
    }

    @Test
    void testPost() {
        String reqBody = "{\n" +
                "    \"projectName\": \"项目zzz1\",\n" +
                "    \"description\": \"测试新增项目\"\n" +
                "}";

        //链式构建请求
        String result3 = HttpRequest.post("http://localhost:8080/bloomtest/project/add")
//                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
//                .timeout(20000)//超时，毫秒
                .body(reqBody)
                .execute().body();
        Console.log(result3);
    }

    @Test
    void testRest() {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("projectId", 3);
        String result4 = HttpRequest
                .get("localhost:8080/bloomtest/module/list/3")
                .setRest(true)
                .execute()
                .body();
        System.out.println(result4);
    }

}
