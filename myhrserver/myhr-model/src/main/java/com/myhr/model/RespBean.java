package com.myhr.model;

import lombok.Data;

/**
 * 统一接口返回封装（对应 vhr 的 RespBean）
 * 约定：status = 200 表示成功，300 表示失败
 */
@Data
public class RespBean {

    private Integer status;
    private String msg;
    private Object obj;

    public static RespBean ok(String msg) {
        return build(200, msg, null);
    }

    public static RespBean ok(String msg, Object obj) {
        return build(200, msg, obj);
    }

    public static RespBean error(String msg) {
        return build(300, msg, null);
    }

    public static RespBean error(String msg, Object obj) {
        return build(300, msg, obj);
    }

    private static RespBean build(Integer status, String msg, Object obj) {
        RespBean respBean = new RespBean();
        respBean.setStatus(status);
        respBean.setMsg(msg);
        respBean.setObj(obj);
        return respBean;
    }
}
