package com.pingguo.bloomtest.controller;

import com.pingguo.bloomtest.common.Result;
import com.pingguo.bloomtest.controller.request.EditNodeRequest;
import com.pingguo.bloomtest.pojo.ApiModule;
import com.pingguo.bloomtest.service.ApiModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("module")
public class ApiModuleController {

    @Autowired
    ApiModuleService apiModuleService;

    @GetMapping("/list/{projectId}")
    public Result getNodeByProjectId(@PathVariable Long projectId) {
        return Result.success(apiModuleService.getNodeTreeByProjectId(projectId));
    }

    @PostMapping("/add")
    public Result addNode(@RequestBody ApiModule node) {
        try {
            System.out.println(node);
            Long nodeId = apiModuleService.addNode(node);
            return Result.success(nodeId);
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @PostMapping("/rename")
    public Result rename(@RequestBody EditNodeRequest request) {
        try {
            apiModuleService.renameNode(request);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }

    @GetMapping("/delete/{id}")
    public Result deleteNode(@PathVariable Long id) {
        try {
            int result = apiModuleService.deleteNode(id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.fail(e.toString());
        }
    }
}
