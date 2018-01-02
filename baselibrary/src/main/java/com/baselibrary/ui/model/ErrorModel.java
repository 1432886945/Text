package com.baselibrary.ui.model;

/**
 * 创建时间 : 2017/8/8
 * 创建人：yangyingqi
 * 公司：嘉善和盛网络有限公司
 * 备注：网络错误模板
 */
public class ErrorModel extends BaseModel {


    /**
     * msg : 分页参数不能为空;分页参数不能为空
     * error_code : -1
     */

    private String msg;
    private int error_code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
