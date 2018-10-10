package com.sscc.common;

public class CommonResult {
    private String msg;

    private String code;

    private Object data;

    public CommonResult() {
        super();
        this.msg = "SUCCESS";
        this.code = "0";
    }

    public CommonResult(Object data) {
        super();
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
