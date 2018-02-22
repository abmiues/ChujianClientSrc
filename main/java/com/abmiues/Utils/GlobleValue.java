package com.abmiues.Utils;

import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/2/22.
 */

public class GlobleValue {
    private static String _ip;
    private static int _host;
    private static SharedPreferences _globleData;
    private static String _userFuncHead="";
    private static String _userid="";
    public static String get_ip() {
        return _ip;
    }

    public static void set_ip(String _ip) {
        GlobleValue._ip = _ip;
    }

    public static int get_host() {
        return _host;
    }

    public static void set_host(int _host) {
        GlobleValue._host = _host;
    }

    public static SharedPreferences get_globleData() {
        return _globleData;
    }

    public static void set_globleData(SharedPreferences _globleData) {
        GlobleValue._globleData = _globleData;
    }

    /**
     * 获取用户请求的方法头
     * @return
     */
    public static String get_userFuncHead() {
        return _userFuncHead;
    }
    /**
     * 设置用户请求的方法头
     * @return
     */
    public static void set_userFuncHead(String _userFuncHead) {
        GlobleValue._userFuncHead = _userFuncHead;
    }

    public static String get_userid() {
        return _userid;
    }

    public static void set_userid(String _userid) {
        GlobleValue._userid = _userid;
    }
}
