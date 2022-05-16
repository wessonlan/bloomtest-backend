package com.pingguo.bloomtest.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pingguo.bloomtest.common.BtException;
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

    /**
     * 处理 header，返回 map
     * @param jsonArray header
     * @return map
     */
    private Map<String, String> handleHeader2Map(JSONArray jsonArray) {
        Map<String, String> headerMap = new HashMap<>();
        if (checkJSONArray(jsonArray)) {
            for (Object o: jsonArray) {
                headerMap.put(JSONUtil.parseObj(o).getStr("name"),
                        JSONUtil.parseObj(o).getStr("value"));
            }
        }
        return headerMap;
    }

    /**
     * 处理入参，返回 map
     * @param jsonArray param参数和rest参数
     * @return map
     */
    private Map<String, Object> handleParams2Map(JSONArray jsonArray) {
        Map<String, Object> headerMap = new HashMap<>();
        if (checkJSONArray(jsonArray)) {
            for (Object o: jsonArray) {
                headerMap.put(JSONUtil.parseObj(o).getStr("name"),
                        JSONUtil.parseObj(o).get("value"));
            }
        }
        return headerMap;
    }

    /**
     * 处理 rest 参数时，请求最终的url
     * @param path 请求对象中的原参数 path
     * @param rest rest参数
     * @return 拼接了 rest 参数的最终 path
     */
    private String handleRestPath(String path, JSONArray rest) {
        List<String> list = Arrays.asList(path.split("/\\{"));
        String originPath = list.get(0);
        // 解析path中的参数，确认参数拼接顺序
        List<String> resultFindAll = ReUtil.findAll("(?<=\\{)(.+?)(?=\\})", path, 0);
        String appendParamPath = "";
        for (String i : resultFindAll) {
            appendParamPath = appendParamPath.concat("/" + handleParams2Map(rest).get(i));
        }
        return originPath + appendParamPath;
    }

    /**
     * 处理 rest 参数时，请求最终的url
     * @param path 请求对象中的原参数 path
     * @param params params 参数
     * @return 拼接了 params 参数的最终 path
     */
    private String handleParamsPath(String path, JSONArray params) {
        String appendParamPath = "?";
        for (Object i : params) {
            String key = JSONUtil.parseObj(i).getStr("name");
            String value = JSONUtil.parseObj(i).getStr("value");
            appendParamPath = appendParamPath.concat("" + key + "=" + value + "&");
        }
        // 去掉最后一个字符 &
        return path + appendParamPath.substring(0, appendParamPath.length()-1);
    }

    @Override
    public JSONObject apiTestRun(ApiDefinition request) {
        // method
        String requestMethod = request.getMethod();
        // header
        JSONArray headers = JSONUtil.parseArray(request.getHeadersKeyValue());
        // 获取入参
        JSONObject reqParamInfo = JSONUtil.parseObj(request.getReqParamInfo());
        JSONArray params = reqParamInfo.getJSONArray("paramKeyValue");
        JSONArray rest = reqParamInfo.getJSONArray("restKeyValue");
        JSONObject reqBody = reqParamInfo.getJSONObject("body");
        // get 方法
        if ("get".equals(requestMethod)) {
            if (!checkJSONArray(params) & !checkJSONArray(rest) & !checkJSONObject(reqBody)) {
                // 无参数
                String requestUrl = request.getHost() + request.getPath();
//                return HttpRequest.get(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .timeout(50000)
//                        .execute();
            }
            else if (checkJSONArray(params) & !checkJSONArray(rest) & !checkJSONObject(reqBody)) {
                // 只有 param 参数
                String requestUrl = request.getHost() + request.getPath();
                HttpResponse httpResponse = HttpRequest.get(requestUrl)
                        .addHeaders(handleHeader2Map(headers))
                        .form(handleParams2Map(params))
                        .timeout(50000)
                        .execute();
                JSONObject result = new JSONObject();
                result.append("body", httpResponse.body())
                        .append("cookies", httpResponse.getCookies())
                        .append("responseStatus", httpResponse.getStatus())
                        .append("responseHeaders", httpResponse.headers());
                return result;
            }
//            else if (!checkJSONArray(params) & checkJSONArray(rest) & !checkJSONObject(reqBody)) {
//                // 只有 rest 参数
//                String requestUrl = request.getHost() + handleRestPath(request.getPath(), rest);
//                return HttpRequest.get(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .timeout(50000)
//                        .execute();
//            } else if (!checkJSONArray(params) & !checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 只有 body 参数
//                String requestUrl = request.getHost() + request.getPath();
//                return HttpRequest.get(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute();
//            }  else if (checkJSONArray(params) & !checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 同时 param + body
//                String requestUrl = request.getHost() + handleParamsPath(request.getPath(), params);
//                return HttpRequest.get(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute();
//            } else if (!checkJSONArray(params) & checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 同时 rest + body
//                String requestUrl = request.getHost() + handleRestPath(request.getPath(), rest);
//                return HttpRequest.get(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute();
//            }
        // post方法
        }
//        else if (requestMethod.equals("post")) {
//            if (!checkJSONArray(params) & !checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 只有 body 参数
//                String requestUrl = request.getHost() + request.getPath();
//                return HttpRequest.post(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute().body();
//            } else if (checkJSONArray(params) & !checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 同时 param + body
//                String requestUrl = request.getHost() + handleParamsPath(request.getPath(), params);
//                return HttpRequest.post(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute().body();
//
//            } else if (!checkJSONArray(params) & checkJSONArray(rest) & checkJSONObject(reqBody)) {
//                // 同时 rest + body
//                String requestUrl = request.getHost() + handleRestPath(request.getPath(), rest);
//                return HttpRequest.post(requestUrl)
//                        .addHeaders(handleHeader2Map(headers))
//                        .body(reqBody.toString())
//                        .timeout(50000)
//                        .execute().body();
//            }
//        }
        return null;
    }

}
