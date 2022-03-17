package com.pingguo.bloomtest.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRunTestRequest {
    private String host;
    private String path;
    private String method;
    private String header;
    private String request;
    private int requestType;
}
