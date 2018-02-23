package com.abmiues.Utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.abmiues.push.PushReciver;

/**
 * Created by Administrator on 2018/2/22.
 */

public class GlobleValue {
    private static String _ip;
    private static int _host;
    private static SharedPreferences _globleData;
    private static String _userFuncHead="";
    private static String _userid="";
    private static PushReciver _pushReciver;
    private static ServiceConnection _serviceConnection;

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

    public static PushReciver get_pushReciver() {
        return _pushReciver;
    }

    public static void set_pushReciver(PushReciver _pushReciver) {
        GlobleValue._pushReciver = _pushReciver;
    }

    /**
     * 创建ServiceConnetion,用于绑定PushService
     * @return
     */
    public static ServiceConnection get_serviceConnection() {
        if(_serviceConnection==null)
            _serviceConnection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    PushReciver.LocalBinder binder= (PushReciver.LocalBinder) iBinder;
                    PushReciver pushReciver=binder.getService();
                    set_pushReciver(pushReciver);
                    pushReciver.Connect(get_userid());
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    set_pushReciver(null);
                }
            };
        return _serviceConnection;
    }

}
