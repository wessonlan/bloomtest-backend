package com.pingguo.bloomtest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pingguo.bloomtest.common.Result;
import com.pingguo.bloomtest.controller.request.ApiRunTestRequest;
import com.pingguo.bloomtest.pojo.ApiDefinition;
import com.pingguo.bloomtest.service.ApiDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("apiDefinition")
public class ApiDefinitionController {
    @Autowired
    ApiDefinitionService apiDefinitionService;

    @GetMapping("/list/{projectId}/{moduleId}/{currentPage}/{pageSize}")
    public Result list(@PathVariable Long projectId,
                       @PathVariable Long moduleId,
                       @PathVariable int currentPage,
                       @PathVariable int pageSize) {
        IPage<ApiDefinition> IPageProject = apiDefinitionService.list(projectId, moduleId, currentPage, pageSize);
        return Result.success(IPageProject);
    }

    @PostMapping("/add")
    public Result add(@RequestBody ApiDefinition request) {
        try {
            apiDefinitionService.add(request);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @GetMapping("/getApi")
    public Result getApiById(Long id) {
        return Result.success(apiDefinitionService.getApi(id));
    }

    @PostMapping("/postApi")
    public Result postApiById(Long id) {
        return Result.success(apiDefinitionService.getApi(id));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ApiDefinition request) {
        try {
            apiDefinitionService.update(request);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @PostMapping("/apiTestRun")
    public Result apiTestRun(@RequestBody ApiRunTestRequest apiRunTestRequest) {
        return Result.success(apiDefinitionService.apiTestRun(apiRunTestRequest));
    }
}
