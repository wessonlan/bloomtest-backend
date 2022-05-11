package com.pingguo.bloomtest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pingguo.bloomtest.pojo.Project;

import java.util.List;

/**
 * @author weikang.sang
 * @date 10:32 2022/5/11
 */
public interface ProjectService {
    /**
     * 新增项目
     * @param project
     */
    void addProject(Project project);

    /**
     * 更新项目
     * @param project
     */
    void updateProject(Project project);

    /**
     * 查询项目列表，带分页
     * @param currentPage
     * @param pageSize
     * @param project
     * @return
     */
    IPage<Project> getProjectList(int currentPage, int pageSize, Project project);

    /**
     * 查询所有项目
     * @return
     */
    List<Project> getProjectAll();
}
