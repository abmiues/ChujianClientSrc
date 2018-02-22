package com.abmiues.chujian;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
SharedPreferences localdata;
    TextView textdata;
    Fragment current_fragment;//保存当前正在显示的界面
    Fragment fragment_index;
    Fragment fragment_livevideo;
    Fragment fragment_order;
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
        setContentView(R.layout.activity_main);
        textdata=(TextView)findViewById(R.id.textdata);
        localdata=getSharedPreferences("localdata", Context.MODE_PRIVATE);
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
                        if(fragment_index!=current_fragment)
                        {
                            transaction.hide(current_fragment).show(fragment_index).commit();
                            current_fragment=fragment_index;
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
                            fragment_order=new Fragment_order();
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
        fragment_index=new Fragment_index();
        fragment_person=new Fragment_person();
        fragment_order=new Fragment_order();
        fragment_livevideo=new Fragment_livevideo();
        transaction.add(R.id.fragment_content,fragment_livevideo).hide(fragment_livevideo);
        transaction.add(R.id.fragment_content,fragment_order).hide(fragment_order);
        transaction.add(R.id.fragment_content,fragment_person).hide(fragment_person);
        transaction.add(R.id.fragment_content,fragment_index).commit();
        current_fragment=fragment_index;
        current_btn=0;
        changebtnimg(btn_index,R.mipmap.index_orange);
    }
    public void changebtnimg(TextView textView,int resource)
    {
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this,resource);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        textView.setCompoundDrawables(null,drawable,null,null);
    }
}
