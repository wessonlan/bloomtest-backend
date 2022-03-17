package com.pingguo.bloomtest.common;

import lombok.Data;


@Data
public class Result {
    private static final int SUCCESS_CODE = 20000;
    private static final int FAIL_CODE = 20005;
    public static final int ALREADY_EXIST_CODE = 30001;

    private int code;
    private String message;
    private Object data;

    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(SUCCESS_CODE, "成功", null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS_CODE, "成功", data);
    }

    public static Result fail(String message) {
        return new Result(FAIL_CODE, message,null);
    }

    /**
     * 传入失败状态码，返回Result结果
     * @param code 失败状态码
     * @param message 文本提示
     * @return Result结果
     */
    public static Result fail(int code, String message) {
        return new Result(code, message, null);
    }
}
