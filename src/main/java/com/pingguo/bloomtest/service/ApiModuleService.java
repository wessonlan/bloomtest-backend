package com.pingguo.bloomtest.service;

import cn.hutool.core.lang.tree.Tree;
import com.pingguo.bloomtest.controller.request.EditNodeRequest;
import com.pingguo.bloomtest.pojo.ApiModule;

import java.util.List;

/**
 * @author weikang.sang
 * @date 10:40 2022/5/11
 */
public interface ApiModuleService {

    /**
     * 获取项目下的模块树结构
     * @param projectId
     * @return
     */
    List<Tree<String>> getNodeTreeByProjectId(Long projectId);
    /**
     * 新增模块节点
     * @param node
     * @return
     */
    Long addNode(ApiModule node);

    /**
     * 重命名节点名称
     * @param request
     */
    void renameNode(EditNodeRequest request);

    /**
     * 删除节点
     * @param id
     * @return
     */
    int deleteNode(Long id);
}
