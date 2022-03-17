package com.pingguo.bloomtest.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pingguo.bloomtest.common.BtException;
import com.pingguo.bloomtest.controller.request.EditNodeRequest;
import com.pingguo.bloomtest.dao.ApiModuleDAO;
import com.pingguo.bloomtest.pojo.ApiDefinition;
import com.pingguo.bloomtest.pojo.ApiModule;
import com.pingguo.bloomtest.pojo.Project;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiModuleService {
    @Autowired
    ApiModuleDAO apiModuleDAO;

    public List<Tree<String>> getNodeTreeByProjectId(Long projectId) {
        this.getDefaultNode(projectId);
        // 根据 projectId 查询所有节点
        QueryWrapper<ApiModule> wrapperApiModule = new QueryWrapper<>();
        List<ApiModule> apiModules = apiModuleDAO.selectList(wrapperApiModule.eq("projectId", projectId));
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 ，即返回列表里对象的字段名
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setWeightKey("pos");
        treeNodeConfig.setParentIdKey("parentId");
        treeNodeConfig.setChildrenKey("children");
        // 最大递归深度
//        treeNodeConfig.setDeep(5);
        treeNodeConfig.setNameKey("name");
        //转换器
        List<Tree<String>> treeNodes = TreeUtil.build(apiModules, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId().toString());
                    tree.setParentId(treeNode.getParentId().toString());
                    tree.setWeight(treeNode.getPos());
                    tree.setName(treeNode.getName());
                    // 扩展属性 ...
                    tree.putExtra("projectId", treeNode.getProjectId());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("label", treeNode.getName());
                    tree.putExtra("createTime", treeNode.getCreateTime());
                    tree.putExtra("updateTime", treeNode.getUpdateTime());

                });
        return treeNodes;

    }

    private void getDefaultNode(Long projectId) {
        QueryWrapper<ApiModule> wrapperApiModule = new QueryWrapper<>();
        wrapperApiModule.eq("projectId", projectId)
                        .eq("pos", 1.0);
        // 判断当前项目下是否有默认模块，没有则添加默认模块
        if (apiModuleDAO.selectCount(wrapperApiModule) == 0) {
            ApiModule apiModule = new ApiModule();
            apiModule.setName("默认");
            apiModule.setPos(1.0);
            apiModule.setLevel(1);
            apiModule.setParentId(0L);
            apiModule.setCreateTime(new Date());
            apiModule.setUpdateTime(new Date());
            apiModule.setProjectId(projectId);
            apiModuleDAO.insert(apiModule);
        }
    }

    public Long addNode(ApiModule node) {
        node.setCreateTime(new Date());
        node.setUpdateTime(new Date());
        double pos = getNextLevelPos(node.getProjectId(), node.getLevel(), node.getId());
        node.setPos(pos);
        node.setLevel(node.getLevel() + 1);
        node.setParentId(node.getId());
        node.setName(node.getName());
        apiModuleDAO.insert(node);
        return node.getId();
    }

    public void renameNode(EditNodeRequest request) {
        // 根据传入的id查询出数据
        ApiModule apiModule = apiModuleDAO.selectById(request.getId());
        // 更新对象属性值，保存
        apiModule.setId(request.getId());
        apiModule.setName(request.getName());
        apiModule.setUpdateTime(new Date());
        apiModuleDAO.updateById(apiModule);
    }

    public int deleteNode(Long id) {
        QueryWrapper<ApiModule> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id)
               .or()
               .eq("parentId", id);
        return apiModuleDAO.delete(wrapper);
    }

    private double getNextLevelPos(Long projectId, int level, Long nodeId) {
        // 查询项目下，同parentId下，所有节点
        QueryWrapper<ApiModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectId", projectId)
                    .eq("level", level + 1)
                    .eq("parentId", nodeId)
                    .orderByDesc("pos");
        List<ApiModule> apiModules = apiModuleDAO.selectList(queryWrapper);
        if (!CollectionUtil.isEmpty(apiModules)) {
            // 不为空，获取最新的同级结点 pos 再加 1，作为下一个
            return apiModules.get(0).getPos() + 1;
        } else {
            // 否则就是当前父节点里的第一个子结点，pos 直接为 1
            return 1;
        }
    }

}
