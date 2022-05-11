package com.pingguo.bloomtest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pingguo.bloomtest.common.Result;
import com.pingguo.bloomtest.entity.request.ApiRunTestRequest;
import com.pingguo.bloomtest.entity.ApiDefinition;
import com.pingguo.bloomtest.service.impl.ApiDefinitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("apiDefinition")
public class ApiDefinitionController {
    @Autowired
    ApiDefinitionServiceImpl apiDefinitionServiceImpl;

    @GetMapping("/list/{projectId}/{moduleId}/{currentPage}/{pageSize}")
    public Result list(@PathVariable Long projectId,
                       @PathVariable Long moduleId,
                       @PathVariable int currentPage,
                       @PathVariable int pageSize) {
        IPage<ApiDefinition> IPageProject = apiDefinitionServiceImpl.list(projectId, moduleId, currentPage, pageSize);
        return Result.success(IPageProject);
    }

    @PostMapping("/add")
    public Result add(@RequestBody ApiDefinition request) {
        try {
            apiDefinitionServiceImpl.add(request);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @GetMapping("/getApi")
    public Result getApiById(Long id) {
        return Result.success(apiDefinitionServiceImpl.getApi(id));
    }

    @PostMapping("/postApi")
    public Result postApiById(Long id) {
        return Result.success(apiDefinitionServiceImpl.getApi(id));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ApiDefinition request) {
        try {
            apiDefinitionServiceImpl.update(request);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @PostMapping("/apiTestRun")
    public Result apiTestRun(@RequestBody ApiRunTestRequest apiRunTestRequest) {
        return Result.success(apiDefinitionServiceImpl.apiTestRun(apiRunTestRequest));
    }
}
