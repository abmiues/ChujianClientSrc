package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abmiues.Utils.GlobleValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.abmiues.chujian.R.id.comment;
import static com.abmiues.chujian.R.id.sellername;

/**
 * 商家主页
 */
public class SellerDetailActivity extends AppCompatActivity {
    LinearLayout contentView;
    String sellerid;
    String cardata;
    JSONObject carItems;
    TextView text_priceall;
    double priceall;
    JSONObject carbyseller;
    String menudata="";
    String commentdata="";
    TextView text_nonedata;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        caculprice();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        priceall=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        contentView= (LinearLayout) findViewById(R.id.contentView);
        text_nonedata= (TextView) findViewById(R.id.nonedata);
        final String sellerinfo=getIntent().getStringExtra("sellerinfo");
        ImageButton imgbtn_livevideo= (ImageButton) findViewById(R.id.imgbtn_livevideo);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        Button btn_ok= (Button) findViewById(R.id.btn_ok);
        Button btn_getmenu= (Button) findViewById(R.id.btn_getmenu);
        Button btn_getdetail= (Button) findViewById(R.id.btn_getdetail);
        Button btn_getcomment= (Button) findViewById(R.id.btn_getcomment);
        text_priceall= (TextView) findViewById(R.id.text_priceall);
        TextView text_comment= (TextView) findViewById(R.id.text_comment);
        ImageView img_title= (ImageView) findViewById(R.id.img_title);//商家标题图片
        cardata= GlobleValue.get_globleData().getString("cardata","");
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(sellerinfo);
            sellerid= (String) jsonObject.get("sellerid");

            text_comment.setText("总评分："+(Integer)jsonObject.get("comment"));
            GetImgByUrl.setUrlImg(img_title,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/icon.png",true,0.6);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerDetailActivity.this.finish();
            }
        });
        imgbtn_livevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerDetailActivity.this,WebUi.class).putExtra("url","http://open.ys7.com/openlive/ebbb77823054487a837e2908f92bb3ee.m3u8").putExtra("title",sellername));
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceall>0)
                    startActivityForResult(new Intent(SellerDetailActivity.this,PayForOrderActivity.class),0);
                else
                    Toast.makeText(SellerDetailActivity.this,"未选择商品",Toast.LENGTH_SHORT).show();
                //TODO 跳转结算界面
            }
        });

        getmenudata();

        getcommentdata();
        caculprice();
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
            try {
                JSONArray jsonArray=new JSONArray(data);
                if(jsonArray.length()==0)
                {
                    text_nonedata.setVisibility(View.VISIBLE);
                    text_nonedata.setText("暂无评论");
                }
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jo=jsonArray.getJSONObject(i);
                    LayoutInflater inflater = (LayoutInflater)SellerDetailActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.item_food_comment, null);
                    TextView text_time=(TextView)view.findViewById(R.id.text_time);
                    TextView text_name= (TextView) view.findViewById(R.id.name);
                    TextView text_content= (TextView) view.findViewById(R.id.text_content);
                    TextView text_comment= (TextView) view.findViewById(comment);
                    text_time.setText(jo.getString("time"));
                    text_name.setText(jo.getString("username"));
                    text_content.setText(jo.getString("content"));
                    text_comment.setText("评分:"+jo.getInt("score"));
                    contentView.addView(view);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTitle(String info)
    {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sellerinfo, null);
        try {
            JSONObject jsonObject=new JSONObject(info);
            sellerid= (String) jsonObject.get("sellerid");
            ImageView img= (ImageView) view.findViewById(R.id.img_seller);//商家头像
            TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
            TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
            TextView text_menu= (TextView) view.findViewById(R.id.text_menu);//菜系
            TextView text_tel=(TextView)view.findViewById(R.id.text_tel);
            TextView text_addr=(TextView)view.findViewById(R.id.text_addr);
            text_tel.setText("联系方式："+(String) jsonObject.get("tel"));
            text_name.setText((String)jsonObject.get("name"));
            GetImgByUrl.setUrlImg(img,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+jsonObject.get("sellerid")+"/icon.png",0.8);
            text_menu.setText("湘菜");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        contentView.addView(view);
    }
    public void caculprice()
    {
        priceall=0;
        cardata=GlobleValue.get_globleData().getString("cardata","");
        try {
            if(cardata.equals(""))
                carbyseller=new JSONObject();
            else
                carbyseller=new JSONObject(cardata);//读取购物车json字符串，放这里是因为这里有try,省得多写一个try
            carItems=carbyseller.getJSONObject(String.valueOf(sellerid));//读取本商店购物车
        } catch (JSONException e) {
            carItems=new JSONObject();
        }

        Iterator it=carItems.keys();//开始遍历json
        while (it.hasNext())
        {
            String key= (String) it.next();
            JSONObject cariteminfo= null;
            try {
                cariteminfo = carItems.getJSONObject(key);
                Double price=cariteminfo.getDouble("price");
                int num=cariteminfo.getInt("num");
                priceall+=num*price;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        text_priceall.setText("¥"+priceall);
    }
    public void getmenudata()
    {
        HttpRequestUtil.Send("getmenu","sellerid="+sellerid,new HttpSendCallback() {
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
            try {
                JSONArray jsonArray=new JSONArray(data);
                if(jsonArray.length()==0)
                {
                    text_nonedata.setVisibility(View.VISIBLE);
                    text_nonedata.setText("暂无菜单");
                }
                for (int i=0;i<jsonArray.length();i++)
                {
                    LayoutInflater inflater = (LayoutInflater)SellerDetailActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.item_food, null);
                    final JSONObject jsonObject=jsonArray.getJSONObject(i);
                    final int foodid=jsonObject.getInt("foodid");
                    final double price=jsonObject.getDouble("price");
                    final String name=jsonObject.getString("name");
                    String des=jsonObject.getString("des");
                    ImageView img= (ImageView) view.findViewById(R.id.img_food);//商家头像
                    ImageButton btn_add= (ImageButton) view.findViewById(R.id.btn_add);
                    TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
                    TextView text_toast= (TextView) view.findViewById(R.id.text_toast);//口味
                    TextView text_comment= (TextView) view.findViewById(R.id.text_comment);//评价
                    final TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
                    text_name.setText(name);
                    GetImgByUrl.setUrlImg(img,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/"+foodid+".png");
                    text_comment.setText("评分"+(Integer)jsonObject.get("comment"));
                    text_price.setText("¥"+price);
                    text_toast.setText(des);
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            JSONObject cariteminfo=null;
                            try {
                                cariteminfo=carItems.getJSONObject(String.valueOf(foodid));
                            } catch (JSONException e) {
                            }
                            try {
                                if(cariteminfo==null|| cariteminfo.length()==0)
                                {
                                    cariteminfo=new JSONObject();
                                    cariteminfo.put("price",price);
                                    cariteminfo.put("num",1);
                                    cariteminfo.put("name",name);
                                    cariteminfo.put("foodid",foodid);
                                }
                                else {
                                    int num = cariteminfo.getInt("num");//读取json字符串
                                    num++;
                                    cariteminfo.put("num", num);
                                }
                                carItems.put(String.valueOf(foodid),cariteminfo);
                                carbyseller.put(String.valueOf(sellerid),carItems);
                                GlobleValue.get_globleData().edit().putString("cardata",carbyseller.toString()).commit();
                                priceall+=cariteminfo.getDouble("price");
                                text_priceall.setText("¥"+priceall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(SellerDetailActivity.this,FoodDetailActivity.class).putExtra("data",jsonObject.toString()),0);
                        }
                    });

                    contentView.addView(view);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void getcommentdata()
    {
         HttpRequestUtil.Send("getsellercomment", "sellerid=" + sellerid, new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                commentdata=data;
            }
        });

    }
}
