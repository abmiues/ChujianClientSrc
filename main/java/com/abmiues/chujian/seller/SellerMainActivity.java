package com.abmiues.chujian.seller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import com.abmiues.Utils.EventCenter;
import com.abmiues.Utils.MyEventListener;
import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.R;
import com.abmiues.chujian.pojo.Order;
import com.abmiues.chujian.user.Fragment_person;
import com.abmiues.push.PushReciver;
import com.google.gson.Gson;

import java.util.HashMap;
public class SellerMainActivity extends AppCompatActivity {
    TextView textdata;
    Fragment current_fragment;//保存当前正在显示的界面
    Fragment fragmentStore;
    FragmentSellerLivevideo fragment_livevideo;
    FragmentSellerOrder fragment_order;
    Fragment fragment_person;
    int current_btn;
    TextView btn_index;
    TextView btn_livevideo;
    TextView btn_order;
    TextView btn_person;
    int [] relase={R.mipmap.index_black,R.mipmap.live_black,R.mipmap.order_black,R.mipmap.person_black};
    int [] press={R.mipmap.index_orange,R.mipmap.live_orange,R.mipmap.order_orange,R.mipmap.person_orange};
    HashMap<Integer,TextView> btnlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        textdata=(TextView)findViewById(R.id.textdata);
        btn_index=(TextView)findViewById(R.id.text_index) ;
        btn_livevideo=(TextView)findViewById(R.id.text_livevideo) ;
        btn_order=(TextView)findViewById(R.id.text_order) ;
        btn_person=(TextView)findViewById(R.id.text_person) ;
        btnlist=new HashMap<Integer,TextView>();
        btnlist.put(0,btn_index);
        btnlist.put(1,btn_livevideo);
        btnlist.put(2,btn_order);
        btnlist.put(3,btn_person);
        setDefaultFragment();
        bottomClick(btn_index);
        bottomClick(btn_livevideo);
        bottomClick(btn_order);
        bottomClick(btn_person);
        EventCenter.Instace().OrderStateChange.addListener(orderStateChange);
        bindService(new Intent(SellerMainActivity.this, PushReciver.class), GlobleValue.get_serviceConnection(), Service.BIND_AUTO_CREATE);//绑定PushService
        //textdata.setText(localdata.getString("camera",""));
    }
    public void bottomClick(TextView text)
    {
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                FragmentTransaction transaction =fm.beginTransaction();
                switch (view.getId())
                {
                    case R.id.text_index:
                        if(fragmentStore!=current_fragment)
                        {
                            transaction.hide(current_fragment).show(fragmentStore).commit();
                            current_fragment=fragmentStore;
                            changebtnimg(btnlist.get(current_btn),relase[current_btn]);
                            current_btn=0;
                            changebtnimg(btnlist.get(current_btn),press[current_btn]);
                        }
                        break;
                    case R.id.text_livevideo:
                        if(fragment_livevideo!=current_fragment)
                        {
                            transaction.hide(current_fragment).show(fragment_livevideo).commit();
                            current_fragment=fragment_livevideo;
                            changebtnimg(btnlist.get(current_btn),relase[current_btn]);
                            current_btn=1;
                            changebtnimg(btnlist.get(current_btn),press[current_btn]);
                        }
                        break;
                    case R.id.text_order:
                        if(fragment_order!=current_fragment)
                        {
                            if(fragment_order!=null)
                                transaction.remove(fragment_order);
                            fragment_order=new FragmentSellerOrder();
                            transaction.add(R.id.fragment_content,fragment_order);
                            transaction.hide(current_fragment).show(fragment_order).commit();
                            current_fragment=fragment_order;
                            changebtnimg(btnlist.get(current_btn),relase[current_btn]);
                            current_btn=2;
                            changebtnimg(btnlist.get(current_btn),press[current_btn]);
                        }
                        break;
                    case R.id.text_person:
                        if(fragment_person!=current_fragment)
                        {
                            transaction.hide(current_fragment).show(fragment_person).commit();
                            current_fragment=fragment_person;
                            changebtnimg(btnlist.get(current_btn),relase[current_btn]);
                            current_btn=3;
                            changebtnimg(btnlist.get(current_btn),press[current_btn]);
                        }
                        break;
                }
            }
        });
    }
    public void setDefaultFragment()
    {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction transaction =fm.beginTransaction();
        fragmentStore=new FragmentStroe();
        fragment_person=new Fragment_person();
        fragment_order=new FragmentSellerOrder();
        fragment_livevideo=new FragmentSellerLivevideo();
        transaction.add(R.id.fragment_content,fragment_livevideo).hide(fragment_livevideo);
        transaction.add(R.id.fragment_content,fragment_order).hide(fragment_order);
        transaction.add(R.id.fragment_content,fragment_person).hide(fragment_person);
        transaction.add(R.id.fragment_content,fragmentStore).commit();
        current_fragment=fragmentStore;
        current_btn=0;
        changebtnimg(btn_index,R.mipmap.index_orange);
    }
    public void changebtnimg(TextView textView,int resource)
    {
        Drawable drawable = ContextCompat.getDrawable(SellerMainActivity.this,resource);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(GlobleValue.get_serviceConnection());
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(resultCode==111)//接收到添加摄像头的activity关闭操作,刷新摄像头列表
            fragment_livevideo.Refresh();
        super.onActivityResult(requestCode, resultCode,  data);
    }

    MyEventListener<String> orderStateChange=new MyEventListener<String>() {
        @Override
        public void GetPush(String data) {
            Order order=new Gson().fromJson(data,Order.class);
            // Intent jumpIntent=new Intent(SellerMainActivity.this, OrderDetail.class);
            // PendingIntent pendingIntent=PendingIntent.getActivity(SellerMainActivity.this,0,jumpIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            fragment_order.createOrderlist();
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(SellerMainActivity.this);
            builder.setSmallIcon(R.mipmap.logo)
                    .setContentText("订单已确认")
                    .setContentText(order.getUsername()+"用户已确认订单")
                    .setAutoCancel(true);
            // .setContentIntent(pendingIntent);
            notificationManager.notify(1,builder.build());
        }
    };

}
