package com.pingguo.bloomtest.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pingguo.bloomtest.common.BtException;
import com.pingguo.bloomtest.entity.request.ApiRunTestRequest;
import com.pingguo.bloomtest.mapper.ApiDefinitionMapper;
import com.pingguo.bloomtest.mapper.ApiModuleMapper;
import com.pingguo.bloomtest.entity.ApiDefinition;
import com.pingguo.bloomtest.entity.ApiModule;
import com.pingguo.bloomtest.service.ApiDefinitionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiDefinitionServiceImpl implements ApiDefinitionService {
    @Autowired
    ApiDefinitionMapper apiDefinitionMapper;
    @Autowired
    ApiModuleMapper apiModuleMapper;

    public IPage<ApiDefinition> list(Long projectId, Long moduleId, int currentPage, int pageSize) {
        // 查询项目id下所有模块
        QueryWrapper<ApiModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectId", projectId);
        List<ApiModule> apiModules = apiModuleMapper.selectList(queryWrapper);
        // 调用递归查询，childrenIds存放查询到的模块 id
        List<Long> childrenIds = new ArrayList<>();
        List<Long> ids = moduleRecursion(childrenIds, apiModules, moduleId);
        // 添加上入参的模块id
        ids.add(moduleId);
        // 查询模块id下的api
        Page<ApiDefinition> pageApiDefinition = new Page<>(currentPage, pageSize);

        QueryWrapper<ApiDefinition> queryApiWrapper = new QueryWrapper<>();
        queryApiWrapper.in("moduleId", ids)
                       .orderByDesc("id");
        return apiDefinitionMapper.selectPage(pageApiDefinition, queryApiWrapper);
    }

    private List<Long> moduleRecursion(List<Long> children, List<ApiModule> modules, Long pid) {
        for (ApiModule apiModule : modules) {
            //遍历出父id等于pid，add进子节点集合
            if (apiModule.getParentId().equals(pid)) {
                // 递归遍历下一级
                moduleRecursion(children, modules, apiModule.getId());
                children.add(apiModule.getId());
            }
        }
        return children;
    }

    public void add(ApiDefinition request) {
        if (StringUtils.isEmpty(request.getProjectId().toString())) {
            BtException.throwException("项目id为空");
        }
        if (StringUtils.isEmpty(request.getModuleId().toString())) {
            BtException.throwException("模块id为空");
        }
        if (StringUtils.isEmpty(request.getName())) {
            BtException.throwException("接口名称为空");
        }
        request.setCreateTime(new Date());
        request.setUpdateTime(new Date());
        apiDefinitionMapper.insert(request);
    }

    public ApiDefinition getApi(Long id) {
        return apiDefinitionMapper.selectById(id);
    }

    public void update(ApiDefinition request) {
        QueryWrapper<ApiDefinition> wrapper = new QueryWrapper<>();
        wrapper.eq("id", request.getId());
        request.setUpdateTime(new Date());
        apiDefinitionMapper.update(request, wrapper);
    }

//    public JSONObject apiTestRun(ApiRunTestRequest request) {
//        // url 拼接
//        String url = request.getHost() + request.getPath();
//        // 判断不同请求参数类型：0 query参数，1 rest参数， 2使用 body参数
//        int queryType = request.getRequestType();
//        if (queryType == 0) {
//            // query 参数
//            HashMap<String, Object> paramMap = new HashMap<>();
//            JSONArray jsonArray = JSONArray.parseArray(request.getRequest());
//            for (int i=0; i<jsonArray.size(); i++) {
//                paramMap.put(jsonArray.getJSONObject(i).get("queryKey").toString(), jsonArray.getJSONObject(i).get("value"));
//            }
//            if (request.getMethod().equals("get")) {
//                String result = HttpRequest.get(url)
//                        .form(paramMap)
//                        .execute()
//                        .body();
//                return JSONObject.parseObject(result);
//            }
//            if (request.getMethod().equals("post")) {
//                String result = HttpRequest.post(url)
//                        .form(paramMap)
//                        .execute()
//                        .body();
//                return JSONObject.parseObject(result);
//            }
//        } else if (queryType == 1) {
//            // rest参数
//            HashMap<String, Object> paramMap = new HashMap<>();
//            JSONArray jsonArray = JSONArray.parseArray(request.getRequest());
//            for (int i=0; i<jsonArray.size(); i++) {
//                paramMap.put(jsonArray.getJSONObject(i).get("restKey").toString(), jsonArray.getJSONObject(i).get("value"));
//            }
//            // 去掉path后面的参数，还原path
//            List<String> list = Arrays.asList(request.getPath().split("/\\{"));
//            String orginPath = list.get(0);
//            // 解析path中的参数，确认参数拼接顺序
//            List<String> resultFindAll = ReUtil.findAll("(?<=\\{)(.+?)(?=\\})", request.getPath(), 0);
//            String appendParamPath = "";
//            for (String i : resultFindAll) {
//                appendParamPath = appendParamPath.concat("/" + paramMap.get(i));
//            }
//            // 发送请求
//            if (request.getMethod().equals("get")) {
//                String result = HttpRequest
//                        .get(request.getHost() + orginPath + appendParamPath)
//                        .execute()
//                        .body();
//                return JSONObject.parseObject(result);
//            }
//            if (request.getMethod().equals("post")) {
//                String result = HttpRequest
//                        .post(request.getHost() + orginPath + appendParamPath)
//                        .execute()
//                        .body();
//                return JSONObject.parseObject(result);
//            }
//
//        } else if (queryType == 2) {
//            // 请求体
//                if (request.getMethod().equals("post")) {
//                    String reqBody = request.getRequest();
//                    String result = HttpRequest.post(url)
//                            .body(reqBody)
//                            .execute().body();
//                    return JSONObject.parseObject(result);
//                }
//            // 请求体
//            if (request.getMethod().equals("get")) {
//                String reqBody = request.getRequest();
//                String result = HttpRequest.get(url)
//                        .body(reqBody)
//                        .execute().body();
//                return JSONObject.parseObject(result);
//            }
//        }
//        return null;
//    }

    /**
     * 判断发送请求的入参是否有效参数
     * @param jsonArray 传入类型 JSONArray 对象
     * @return 入参未空-false，存在入参-true
     */
    private boolean checkJSONArray(JSONArray jsonArray) {
        return !StrUtil.isBlank(jsonArray.getJSONObject(0).getStr("name"));
    }
    /**
     * 判断发送请求的入参是否有效参数
     * @param jsonObject 传入类型 JSONObject 对象
     * @return 入参未空-false，存在入参-true
     */
    private boolean checkJSONObject(JSONObject jsonObject) {
        return !jsonObject.isEmpty();
    }

    @Override
    public JSONObject apiTestRunNew(ApiDefinition request) {
        // header
        JSONArray headers = JSONUtil.parseArray(request.getHeadersKeyValue());

        JSONObject reqParamInfo = JSONUtil.parseObj(request.getReqParamInfo());
        // param参数
        JSONArray params = reqParamInfo.getJSONArray("paramKeyValue");
        // rest参数
        JSONArray rest = reqParamInfo.getJSONArray("restKeyValue");
        // body参数
        JSONObject reqBody = reqParamInfo.getJSONObject("body");

        // 不存在任何参数

        // 只有 param 参数

        // 只有 rest参数

        // 只有 body 参数

        // 同时 param + rest

        // 同时 param + body

        // 同时 rest + body

        // 同时 param + rest + body

        return null;
    }

}
