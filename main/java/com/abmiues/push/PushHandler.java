package com.abmiues.push;

import com.abmiues.Utils.EventCenter;

/**
 * Created by Administrator on 2018/3/1.
 */

public class PushHandler {

    public static void Push(String func,String data)
    {
        if(func.equals("orderStateChange"))
            EventCenter.Instace().OrderStateChange.BoradCast(data);
        else if(func.equals("connect"))//链接成功后的回调
            ;
        else if(func.equals("heartbeat"))//心跳回调,暂未实现
            ;
    }

}
