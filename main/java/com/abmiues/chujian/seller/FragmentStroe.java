package com.abmiues.chujian.seller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.GetImgByUrl;
import com.abmiues.chujian.HttpRequestUtil;
import com.abmiues.chujian.HttpSendCallback;
import com.abmiues.chujian.R;
import com.abmiues.chujian.pojo.F_comment;
import com.abmiues.chujian.pojo.Food;
import com.abmiues.chujian.pojo.S_comment;
import com.abmiues.chujian.pojo.Seller;
import com.abmiues.chujian.user.FoodDetailActivity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;


public class FragmentStroe extends Fragment {

    /**
     * 显示菜单的容器
     */
    LinearLayout contentView;
    String cardata;
    TextView text_priceall;
    double priceall;
    String menudata="";
    String commentdata="";

    /**
     * 没有菜单时显示
     */
    TextView text_nonedata;

    /**
     * 存放seller信息
     */
    Seller sellerInfo;

    /**
     * 存放评论信息
     */
    List<F_comment> commentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_stroe, container, false);
        contentView= (LinearLayout)view.findViewById(R.id.contentView);
        text_nonedata= (TextView) view.findViewById(R.id.nonedata);
        final String sellerinfo=getActivity().getIntent().getStringExtra("sellerinfo");
        ImageButton imgbtn_livevideo= (ImageButton) view.findViewById(R.id.imgbtn_livevideo);
        ImageButton imgbtn_back= (ImageButton) view.findViewById(R.id.imgbtn_back);
        Button btn_edit= (Button) view.findViewById(R.id.btn_edit);
        Button btn_getmenu= (Button) view.findViewById(R.id.btn_getmenu);
        Button btn_getdetail= (Button) view.findViewById(R.id.btn_getdetail);
        Button btn_getcomment= (Button) view.findViewById(R.id.btn_getcomment);
        text_priceall= (TextView) view.findViewById(R.id.text_priceall);
        TextView text_comment= (TextView) view.findViewById(R.id.text_comment);
        ImageView img_title= (ImageView) view.findViewById(R.id.img_title);//商家标题图片
        cardata= GlobleValue.get_globleData().getString("cardata","");
        btn_getmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.removeAllViews();
                createMenu();
            }
        });
        btn_getdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.removeAllViews();
                createTitle(sellerinfo);
            }
        });
        btn_getcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentView.removeAllViews();
                createComment();
            }
        });

        imgbtn_livevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(SellerDetailActivity.this,WebUi.class).putExtra("url","http://open.ys7.com/openlive/ebbb77823054487a837e2908f92bb3ee.m3u8").putExtra("title",sellername));
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        HttpRequestUtil.Send("getuserinfo", "", new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                sellerInfo= (Seller) JSONObject.toBean(new JSONObject().fromObject(data),Seller.class);
                getmenudata();
                getcommentdata();
            }
        });



        return view;
    }


    public void createComment()
    {
        String data=commentdata;
        if(data.equals(""))
        {
            text_nonedata.setVisibility(View.VISIBLE);
            text_nonedata.setText("暂无评论");
        }
        else
        {
            text_nonedata.setVisibility(View.GONE);
            List<S_comment> commentList= (List<S_comment>) JSONArray.toCollection(JSONArray.fromObject(data),S_comment.class);
            if(commentList.size()==0)
            {
                text_nonedata.setVisibility(View.VISIBLE);
                text_nonedata.setText("暂无评论");
            }
            for (int i=0;i<commentList.size();i++)
            {
                S_comment s_comment=commentList.get(i);
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_food_comment, null);
                TextView text_time=(TextView)view.findViewById(R.id.text_time);
                TextView text_name= (TextView) view.findViewById(R.id.name);
                TextView text_content= (TextView) view.findViewById(R.id.text_content);
                TextView text_comment= (TextView) view.findViewById(R.id.comment);
                text_time.setText(s_comment.getTime());
                text_name.setText(s_comment.getUsername());
                text_content.setText(s_comment.getContent());
                text_comment.setText("评分:"+s_comment.getScore());
                contentView.addView(view);
            }
        }
    }

    public void createTitle(String info)
    {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sellerinfo, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_seller);//商家头像
        TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
        TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
        TextView text_menu= (TextView) view.findViewById(R.id.text_menu);//菜系
        TextView text_tel=(TextView)view.findViewById(R.id.text_tel);
        TextView text_addr=(TextView)view.findViewById(R.id.text_addr);
        text_tel.setText("联系方式："+sellerInfo.getTel());
        text_name.setText(sellerInfo.getName());
        GetImgByUrl.setUrlImg(img,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerInfo.getSellerid()+"/icon.png",0.8);
        text_menu.setText("湘菜");
        contentView.addView(view);
    }

    public void getmenudata()
    {
        HttpRequestUtil.Send("getmenu","sellerid="+sellerInfo.getSellerid(),new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                menudata=data;
                createMenu();
            }
        });
    }
    public void createMenu()
    {
        String data=menudata;
        if(data.equals("")) {
            {
                text_nonedata.setVisibility(View.VISIBLE);
                text_nonedata.setText("暂无菜单");
            }
        }
        else
        {
            text_nonedata.setVisibility(View.GONE);
            List<Food> menu= (List<Food>) JSONArray.toCollection(JSONArray.fromObject(data),Food.class);
            if(menu.size()==0)
            {
                text_nonedata.setVisibility(View.VISIBLE);
                text_nonedata.setText("暂无菜单");
            }
            for (int i=0;i<menu.size();i++)
            {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_food, null);
                final Food food= menu.get(i);
                final int foodid=food.getFoodid();
                final double price=food.getPrice();
                final String name=food.getName();
                String des=food.getDes();
                ImageView img= (ImageView) view.findViewById(R.id.img_food);//菜图片
                ImageButton btn_add= (ImageButton) view.findViewById(R.id.btn_add);
                TextView text_name= (TextView) view.findViewById(R.id.name);//菜名字
                TextView text_toast= (TextView) view.findViewById(R.id.text_toast);//口味
                TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
                final TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
                text_name.setText(name);
                GetImgByUrl.setUrlImg(img,"http://"+ GlobleValue.get_ip()+"/ChujianServer/images/"+sellerInfo.getSellerid()+"/"+foodid+".png");
                text_comment.setText("评分"+food.getComment());
                text_price.setText("¥"+price);
                text_toast.setText(des);
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(getActivity(),FoodDetailActivity.class).putExtra("data",food.toString()),0);
                    }
                });
                contentView.addView(view);
            }


        }
    }

    public void getcommentdata()
    {
        HttpRequestUtil.Send("getsellercomment", "sellerid=" + sellerInfo.getSellerid(), new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                commentdata=data;
            }
        });

    }
}
