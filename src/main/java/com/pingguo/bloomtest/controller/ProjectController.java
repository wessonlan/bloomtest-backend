package com.pingguo.bloomtest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pingguo.bloomtest.common.Result;
import com.pingguo.bloomtest.pojo.Project;
import com.pingguo.bloomtest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/add")
    public Result addProject(@RequestBody Project project) {
        try {
            projectService.addProject(project);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @GetMapping("/getAdd")
    public Result getAddProject(@RequestBody Project project) {
        try {
            projectService.addProject(project);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @PostMapping("/list/{currentPage}/{pageSize}")
    public Result getProjectList(@PathVariable int currentPage,
                                 @PathVariable int pageSize,
                                 @RequestBody Project project) {
        IPage<Project> IPageProject = projectService.getProjectList(currentPage, pageSize, project);
        return Result.success(IPageProject);

    }

    @GetMapping("/list/all")
    public Result getProjectAll() {
        return Result.success(projectService.getProjectAll());
    }

    @PostMapping("/update")
    public Result updateProject(@RequestBody Project project) {
        try {
            projectService.updateProject(project);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }
}
