package com.abmiues.Utils;

import java.util.Collection;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Administrator on 2018/3/1.
 */

public class EventCenter {
    private static EventCenter _instance;

    public static EventCenter Instace()
    {
        if(_instance==null)
        {
            _instance=new EventCenter();
        }
        return _instance;
    }
    public  MyEvent<String> OrderStateChange=new MyEvent<String>();

    public  class MyEvent<T>
    {
        private Collection listeners;
        public MyEvent()
        {
            listeners=new HashSet();
        }

        public void addListener(MyEventListener listener)
        {
            listeners.add(listener);
        }
        public void removeListener(MyEventListener listener)
        {
            listeners.remove(listener);
        }
        public void BoradCast(T data)
        {
            Iterator iter = listeners.iterator();
            while (iter.hasNext()) {
                MyEventListener listener = (MyEventListener) iter.next();
                listener.GetPush(data);
            }
        }
    }


}




