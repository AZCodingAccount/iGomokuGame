package com.gobanggame.utils;

/*
 *
 * 存储用户id，使用ThreadLocal
 * */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 用户

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
    // 管理员

    public static Long getAdminCurrentId() {
        return threadLocal.get();
    }

    public static void setAdminCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static void removeId() {
        threadLocal.remove();
    }

}
