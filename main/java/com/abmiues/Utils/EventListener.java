package com.abmiues.Utils;

import java.util.EventListener;

/**
 * Created by Administrator on 2018/3/1.
 */

public interface EventListener<T> extends java.util.EventListener {
    public void GetPush(T data);
}
