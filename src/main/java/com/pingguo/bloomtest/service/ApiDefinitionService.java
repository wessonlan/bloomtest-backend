package com.pingguo.bloomtest.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pingguo.bloomtest.controller.request.ApiRunTestRequest;
import com.pingguo.bloomtest.pojo.ApiDefinition;

/**
 * @author weikang.sang
 * @date 10:43 2022/5/11
 */
public interface ApiDefinitionService {
    /**
     * 查询接口列表
     * @param projectId
     * @param moduleId
     * @param currentPage
     * @param pageSize
     * @return
     */
    IPage<ApiDefinition> list(Long projectId, Long moduleId, int currentPage, int pageSize);

    /**
     * 新增接口
     * @param request
     */
    void add(ApiDefinition request);

    /**
     * 查询单个接口
     * @param id
     * @return
     */
    ApiDefinition getApi(Long id);

    /**
     * 更新接口
     * @param request
     */
    void update(ApiDefinition request);

    /**
     * 发送接口请求
     * @param request
     * @return
     */
    JSONObject apiTestRun(ApiRunTestRequest request);
}
