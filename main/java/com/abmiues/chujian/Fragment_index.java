package com.abmiues.chujian;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abmiues.chujian.abmiues.chujian.layer.Item_scrollad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/2/20.
 */
public class Fragment_index extends Fragment{
    LinearLayout contentView;
    SharedPreferences localdata;
    String ip;
    String searchstr="";
    EditText editsearch;
    int host;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_index,container,false);
        localdata=getActivity().getSharedPreferences("localdata",Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        contentView=(LinearLayout)view.findViewById(R.id.contentView);
        editsearch= (EditText) view.findViewById(R.id.editText);
        contentView.addView(new Item_scrollad(getActivity()));
        contentView.addView(drawType());
        ImageButton btnsearch= (ImageButton) view.findViewById(R.id.btn_search);
        host=localdata.getInt("host",80);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchstr=editsearch.getText().toString();
                if(searchstr.equals(""))
                    Toast.makeText(getActivity(),"请输入要商家名称",Toast.LENGTH_LONG).show();
                else
                new HttpRequestUtil("http://"+ip+":"+host+"/ChujianServer/user/searchseller", "name="+searchstr, new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {
                        if(data.equals(""))
                        {
                            Toast.makeText(getActivity(),"未搜索到相关信息",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            startActivity(new Intent(getActivity(), SearchSellerActivity.class).putExtra("data",data));
                        }
                    }
                },getActivity()).execute();
            }
        });
        new HttpRequestUtil("http://"+ip+":"+host+"/ChujianServer/user/getseller", "", new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                if(data.equals(""))
                {
                    Toast.makeText(getActivity(),"数据为空",Toast.LENGTH_LONG).show();
                }
                else
                {
                    drawSeller(data);
                }
            }
        },getActivity()).execute();
        return view;
    }

    /**
     * @param data //绘制商家
     * @return
     */
    public void drawSeller(final String data)
    {
        try {
            JSONArray jsondata=new JSONArray(data);
            for (int i=0;i<jsondata.length();i++)
            {
                final JSONObject jb=jsondata.getJSONObject(i);
                View view=drawSellerItem(jb);
                ImageView img= (ImageView) view.findViewById(R.id.img_seller);//商家头像
                TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
                ImageView img_video= (ImageView) view.findViewById(R.id.img_video);//直播状态
                TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
                TextView text_menu= (TextView) view.findViewById(R.id.text_menu);//菜系
                text_name.setText((String)jb.get("name"));
                GetImgByUrl.setUrlImg(img,"http://"+ip+"/ChujianServer/images/"+jb.get("sellerid")+"/icon.png");
                img_video.setImageResource((Integer) jb.get("state")==1? R.mipmap.live_orange:R.mipmap.live_black);
                text_comment.setText("店铺评分"+(Integer)jb.get("comment"));
                text_menu.setText("湘菜");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(),SellerDetailActivity.class).putExtra("sellerinfo",jb.toString()));
                    }
                });
                contentView.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public View drawSellerItem(JSONObject data)
    {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_seller, null);
        return view;
    }

    public View drawType()
    {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_type, null);
        return view;
    }
    public View drawScrollAd()//绘制广告滚动，暂时无用
    {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_scrollad, null);
        ViewPager pager= (ViewPager) view.findViewById(R.id.viewpaper_ad);
        View img=createpaper(getActivity());
        final ArrayList<View> viewList = new ArrayList<View>();
        viewList.add(img);
        PagerAdapter adapter=new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }
    public View createpaper(Context context)
    {
        ImageView img=new ImageView(context);
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(R.mipmap.ad1);
        return img;
    }

}
