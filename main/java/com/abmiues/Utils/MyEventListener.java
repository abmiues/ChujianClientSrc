package com.abmiues.Utils;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface MyEventListener<T> extends java.util.EventListener {
    public void GetPush(T data);
}
