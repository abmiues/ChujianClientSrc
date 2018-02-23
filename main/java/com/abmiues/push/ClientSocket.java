package com.abmiues.push;
import android.os.Handler;
import android.os.Message;

import com.abmiues.Utils.GlobleValue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ClientSocket {

    ByteArray sendbuf=new ByteArray(32*1024);
    ByteArray _recvBuff=new ByteArray(32*1024);
    Handler _mhandler;
    Message msg;
    byte[] _buff=new byte[32*1024];

    String str="hello world!";
    int length=0;
    private Socket socket=null;
    private DataOutputStream outPut;
    private DataInputStream inPut;
    public ClientSocket(Handler mhandler){
        _mhandler=mhandler;
        msg=new Message();
        try {
            socket=new Socket(GlobleValue.get_ip(),8050);
            outPut=new DataOutputStream(socket.getOutputStream());
            inPut=new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开启接收数据线程
        new ClientThread().start();
    }

    public void Send(String data)
    {
        try {
            sendbuf.Clear();
            sendbuf.WriteInt(1);//随便写入一个整形占前4个字节，用于存放包内容长度
            sendbuf.WriteStringUInt(data.getBytes("UTF-8"));//写入包内容
            sendbuf.WriteLen(sendbuf.GetLen()-4);//写入包内容长度。
            outPut.write(sendbuf.GetBuff(),0,sendbuf.GetLen());
        } catch (UnsupportedEncodingException e1) {
            System.out.println("不支持的编码："+data);
        }
        catch (IOException e) {
            System.out.println("发送失败："+data);
        }
    }

    public void recv(ByteArray recvBuff)
    {
        recvBuff.GetLen();
        int av =recvBuff.GetAvailable();
        while (av>=4) {
            int packagelen=recvBuff.ReadInt();
            if(av>=packagelen)
            {
                String data=recvBuff.ReadStringUInt();
                msg=_mhandler.obtainMessage();
                msg.obj=data;
                _mhandler.sendMessage(msg);
                System.out.println("收到数据："+data);
                av=recvBuff.GetAvailable();
            }
            else//buff中数据不足，恢复指针位置，进行下次recv
            {
                recvBuff.Offset(-4);
                break;
            }
        }
    }

    public void DisConnect()
    {
        try {
            outPut.close();
            inPut.close();
            socket.close();
        } catch (IOException e1) {
            System.out.println("socket关闭失败");
        }
    }
    //定义读取服务器数据的线程
    private class ClientThread extends Thread{
        public void run(){
            int length=0;
            try{
                while((length=inPut.read(_buff))>0){
                    if(_recvBuff.IsFull())//缓存满了，动态扩展
                        _recvBuff.AutoExpand(true);
                    _recvBuff.WriteBytes(_buff,length);
                    recv(_recvBuff);
                }
            }
            catch(IOException e) {
                System.out.println("服务端强制关闭连接");
                DisConnect();
                this.interrupt();
                return;
            }
        }
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

}