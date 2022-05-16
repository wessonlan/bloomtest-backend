package com.pingguo.bloomtest.entity.request;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiDefinitionRequest {
    private String apiName;
    private Long moduleId;
    private Long projectId;
    private String host;
    private String path;
    private String method;
    private JSONArray headers;
    private JSONObject request;
    private String response;
}
