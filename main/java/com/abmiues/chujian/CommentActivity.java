package com.abmiues.chujian;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abmiues.Utils.GlobleValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {
    LinearLayout contentView;
    HashMap<String ,HashMap> map;
    String sellerid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        contentView= (LinearLayout)findViewById(R.id.contentView);

        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentActivity.this.finish();
            }
        });
        Button btn_save= (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String key : map.keySet()) {
                    HashMap<String,String> datamap=map.get(key);
                    if(key.length()==11)
                    {
                        String param="sellerid="+key+"&content="+datamap.get("content")+"&score="+datamap.get("score");
                        HttpRequestUtil.Send("haddsellercomment",param,new HttpSendCallback() {
                            @Override
                            public void getdata(String data) {
                            }
                        });
                    }
                    else
                    {
                        String param="foodid="+key+"&content="+datamap.get("content")+"&score="+datamap.get("score");
                        HttpRequestUtil.Send("addfoodcomment",param,new HttpSendCallback() {
                            @Override
                            public void getdata(String data) {

                            }
                        });
                    }
               }
               CommentActivity.this.finish();
            }
        });
        map=new HashMap<String, HashMap>();
        addseller(getIntent().getExtras().getString("data"));
    }
    public void addseller(final String datas)
    {
        try {
            final HashMap<String,String> datamap=new HashMap<String,String>();
            datamap.put("score","5");
            final JSONObject jb=new JSONObject(datas);
            LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_comment, null);
            final ImageButton imgstar1= (ImageButton) view.findViewById(R.id.img_star1);
            final ImageButton imgstar2= (ImageButton) view.findViewById(R.id.img_star2);
            final ImageButton imgstar3= (ImageButton) view.findViewById(R.id.img_star3);
            final ImageButton imgstar4= (ImageButton) view.findViewById(R.id.img_star4);
            final ImageButton imgstar5= (ImageButton) view.findViewById(R.id.img_star5);

            imgstar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgstar1.setImageResource(R.mipmap.store_orange);
                    imgstar2.setImageResource(R.mipmap.store_gray);
                    imgstar3.setImageResource(R.mipmap.store_gray);
                    imgstar4.setImageResource(R.mipmap.store_gray);
                    imgstar5.setImageResource(R.mipmap.store_gray);
                    datamap.put("score","1");
                }
            });
            imgstar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgstar1.setImageResource(R.mipmap.store_orange);
                    imgstar2.setImageResource(R.mipmap.store_orange);
                    imgstar3.setImageResource(R.mipmap.store_gray);
                    imgstar4.setImageResource(R.mipmap.store_gray);
                    imgstar5.setImageResource(R.mipmap.store_gray);
                    datamap.put("score","2");
                }
            });
            imgstar3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgstar1.setImageResource(R.mipmap.store_orange);
                    imgstar2.setImageResource(R.mipmap.store_orange);
                    imgstar3.setImageResource(R.mipmap.store_orange);
                    imgstar4.setImageResource(R.mipmap.store_gray);
                    imgstar5.setImageResource(R.mipmap.store_gray);
                    datamap.put("score","3");
                }
            });
            imgstar4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgstar1.setImageResource(R.mipmap.store_orange);
                    imgstar2.setImageResource(R.mipmap.store_orange);
                    imgstar3.setImageResource(R.mipmap.store_orange);
                    imgstar4.setImageResource(R.mipmap.store_orange);
                    imgstar5.setImageResource(R.mipmap.store_gray);
                    datamap.put("score","4");
                }
            });
            imgstar5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgstar1.setImageResource(R.mipmap.store_orange);
                    imgstar2.setImageResource(R.mipmap.store_orange);
                    imgstar3.setImageResource(R.mipmap.store_orange);
                    imgstar4.setImageResource(R.mipmap.store_orange);
                    imgstar5.setImageResource(R.mipmap.store_orange);
                    datamap.put("score","5");
                }
            });
            final EditText addcontent= (EditText) view.findViewById(R.id.add_content);
            addcontent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    datamap.put("content",addcontent.getText().toString());
                }
            });
            ImageView img_seller= (ImageView) view.findViewById(R.id.img_seller);//商家头像
            TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
            text_name.setText((String)jb.get("sellername"));
            sellerid= (String) jb.get("sellerid");
            map.put((String) jb.get("sellerid"),datamap);
            GetImgByUrl.setUrlImg(img_seller,"http://"+ GlobleValue.get_ip()+"/ChujianServer/images/"+jb.get("sellerid")+"/icon.png",0.6);
            contentView.addView(view);
            HttpRequestUtil.Send("getorderdetail","orderid="+jb.get("orderid"),new HttpSendCallback() {
                @Override
                public void getdata(String data) {
                    if(data.equals(""))
                        Toast.makeText(CommentActivity.this,"数据为空",Toast.LENGTH_LONG).show();
                    else
                    {
                        addfood(data);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addfood(final String data)
    {
        try {
            JSONArray jsondata=new JSONArray(data);
            for (int i=0;i<jsondata.length();i++)
            {
                final HashMap<String,String> datamap=new HashMap<String,String>();
                datamap.put("score","5");

                final JSONObject jb=jsondata.getJSONObject(i);
                LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_comment, null);
                TextView textcommenttype=(TextView)view.findViewById(R.id.comment_type);
                textcommenttype.setText("评价美食");
                final ImageButton imgstar1= (ImageButton) view.findViewById(R.id.img_star1);
                final ImageButton imgstar2= (ImageButton) view.findViewById(R.id.img_star2);
                final ImageButton imgstar3= (ImageButton) view.findViewById(R.id.img_star3);
                final ImageButton imgstar4= (ImageButton) view.findViewById(R.id.img_star4);
                final ImageButton imgstar5= (ImageButton) view.findViewById(R.id.img_star5);
                imgstar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgstar1.setImageResource(R.mipmap.store_orange);
                        imgstar2.setImageResource(R.mipmap.store_gray);
                        imgstar3.setImageResource(R.mipmap.store_gray);
                        imgstar4.setImageResource(R.mipmap.store_gray);
                        imgstar5.setImageResource(R.mipmap.store_gray);
                        datamap.put("score","1");
                    }
                });
                imgstar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgstar1.setImageResource(R.mipmap.store_orange);
                        imgstar2.setImageResource(R.mipmap.store_orange);
                        imgstar3.setImageResource(R.mipmap.store_gray);
                        imgstar4.setImageResource(R.mipmap.store_gray);
                        imgstar5.setImageResource(R.mipmap.store_gray);
                        datamap.put("score","2");
                    }
                });
                imgstar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgstar1.setImageResource(R.mipmap.store_orange);
                        imgstar2.setImageResource(R.mipmap.store_orange);
                        imgstar3.setImageResource(R.mipmap.store_orange);
                        imgstar4.setImageResource(R.mipmap.store_gray);
                        imgstar5.setImageResource(R.mipmap.store_gray);
                        datamap.put("score","3");
                    }
                });
                imgstar4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgstar1.setImageResource(R.mipmap.store_orange);
                        imgstar2.setImageResource(R.mipmap.store_orange);
                        imgstar3.setImageResource(R.mipmap.store_orange);
                        imgstar4.setImageResource(R.mipmap.store_orange);
                        imgstar5.setImageResource(R.mipmap.store_gray);
                        datamap.put("score","4");
                    }
                });
                imgstar5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgstar1.setImageResource(R.mipmap.store_orange);
                        imgstar2.setImageResource(R.mipmap.store_orange);
                        imgstar3.setImageResource(R.mipmap.store_orange);
                        imgstar4.setImageResource(R.mipmap.store_orange);
                        imgstar5.setImageResource(R.mipmap.store_orange);
                        datamap.put("score","5");
                    }
                });
                final EditText addcontent= (EditText) view.findViewById(R.id.add_content);
                ImageView img_seller= (ImageView) view.findViewById(R.id.img_seller);//商家头像
                TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
                text_name.setText((String)jb.get("foodname"));
                int foodid=jb.getInt("foodid");
                map.put(String.valueOf(foodid),datamap);
                addcontent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        datamap.put("content",addcontent.getText().toString());
                    }
                });
                GetImgByUrl.setUrlImg(img_seller,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/"+foodid+".png",0.6);
                contentView.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
