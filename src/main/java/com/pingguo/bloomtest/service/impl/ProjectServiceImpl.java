package com.pingguo.bloomtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pingguo.bloomtest.common.BtException;
import com.pingguo.bloomtest.mapper.ProjectMapper;
import com.pingguo.bloomtest.pojo.Project;
import com.pingguo.bloomtest.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectMapper projectMapper;

    public void addProject(Project project) {
        if (StringUtils.isBlank(project.getProjectName())) {
            BtException.throwException("项目名称为空");
        }
        QueryWrapper<Project> wrapperProject = new QueryWrapper<>();
        wrapperProject.eq("projectName", project.getProjectName());
        if (projectMapper.selectCount(wrapperProject) != 0) {
            BtException.throwException("项目名称已存在");
        }
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());
        projectMapper.insert(project);
    }

    public void updateProject(Project project) {
        QueryWrapper<Project> wrapperProject = new QueryWrapper<>();
        wrapperProject.eq("id", project.getId());
        project.setUpdateTime(new Date());
        projectMapper.update(project, wrapperProject);
    }

    public IPage<Project> getProjectList(int currentPage, int pageSize, Project project) {
        // 创建分页对象，current为当前页数，size为每页最大记录数
        Page<Project> pageProject = new Page<>(currentPage, pageSize);
        QueryWrapper<Project> wrapperProject = new QueryWrapper<>();
        // 第一个参数为是否执行条件，为true则执行该条件
        wrapperProject.like(StringUtils.isNoneBlank(project.getProjectName()), "projectName", project.getProjectName());
        // 根据 项目id 查询
        wrapperProject.eq(project.getId() != null, "id", project.getId());
        // id 倒序
        wrapperProject.orderByDesc("id");
        // 调用分页查询方法，传入分页对象-pageProject，wrapper是构造条件对象
        return projectMapper.selectPage(pageProject, wrapperProject);
    }

    public List<Project> getProjectAll() {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        return projectMapper.selectList(queryWrapper);
    }
}
