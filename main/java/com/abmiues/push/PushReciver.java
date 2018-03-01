package com.abmiues.push;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class PushReciver extends Service {
    private LocalBinder binder = new LocalBinder();
    private ClientSocket _socket;
    private HandlerThread thread;
    private Message sendMsg;
    private Handler mhandler;
    private Handler threadHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        sendMsg=new Message();
        thread=new HandlerThread("handler-thread");
        thread.start();
        mhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    JSONObject jsonObject=new JSONObject(msg.obj.toString());
                    String func= jsonObject.getString("func");
                    String data=jsonObject.getString("data");
                    PushHandler.Push(func,data);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"收到非json数据",Toast.LENGTH_SHORT).show();
                }

            }
        };
        threadHandler=new Handler(thread.getLooper())
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:OnConnect(msg.obj.toString());break;
                    case 1:OnSend(msg.obj.toString());break;
                    case 2:OnDisConnect();break;
                }
            }
        };
    }

    public void Connect(String uid)
    {
        sendMsg=threadHandler.obtainMessage();
        sendMsg.what=0;
        sendMsg.obj=uid;
        threadHandler.sendMessage(sendMsg);
    }

    public void DisConnect()
    {
        sendMsg=threadHandler.obtainMessage();
        sendMsg.what=2;
        threadHandler.sendMessage(sendMsg);
    }

    public void Send(String funcName,String data)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("func", funcName);
            jsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Send(jsonObject);
    }
    public void Send(JSONObject data)
    {
        sendMsg=threadHandler.obtainMessage();
        sendMsg.what=1;
        sendMsg.obj=data;
        threadHandler.sendMessage(sendMsg);
    }
    //--------只能在子线程调用---------
    private void OnSend(String data)
    {
        if(_socket==null)
            System.out.println("Socket 未初始化");
        _socket.Send(data);
    }
    private void OnConnect(String uid)
    {
        _socket=new ClientSocket(mhandler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("func", "connect");
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OnSend(jsonObject.toString());
    }
    private void OnDisConnect()
    {
        _socket.DisConnect();
    }
    //--------只能在子线程调用---------


    public class LocalBinder extends Binder {
        public PushReciver getService() {
            return PushReciver.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
