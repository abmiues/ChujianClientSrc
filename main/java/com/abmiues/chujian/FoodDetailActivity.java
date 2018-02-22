package com.abmiues.chujian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.abmiues.chujian.R.id.comment;

public class FoodDetailActivity extends AppCompatActivity {
    LinearLayout contentView;
    SharedPreferences localdata;
    String ip;
    int foodid;
    String sellerid;
    String cardata;
    JSONObject carItems;
    JSONObject carbyseller;
    TextView text_price;
    double priceall=0;
    double price=0;
    EditText edit_num;
    String name;
    boolean iseditable;
    String videourl;
    int host;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        caculprice();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        localdata=getSharedPreferences("localdata",Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        text_price= (TextView) findViewById(R.id.text_priceall);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        ImageButton btn_add= (ImageButton) findViewById(R.id.btn_add);
        ImageButton btn_pub= (ImageButton) findViewById(R.id.btn_pub);
        Button btn_ok= (Button) findViewById(R.id.btn_ok);
        View layout_history=findViewById(R.id.layout_history);
        edit_num= (EditText) findViewById(R.id.edit_num);
        iseditable=false;
        layout_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videourl==null || videourl.equals("") ||videourl.equals("null"))
                    Toast.makeText(FoodDetailActivity.this,"无录像",Toast.LENGTH_LONG).show();
                else
                    startActivity(new Intent(FoodDetailActivity.this,WebUi.class).putExtra("url",videourl).putExtra("title",name+"历史录像"));
            }
        });
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodDetailActivity.this.finish();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changenum(1,true);
            }
        });
        btn_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(edit_num.getText().toString())>0)
                changenum(-1,true);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changenum(0,true);//更新数据
                String cardata=localdata.getString("cardata","");
                if(priceall>0)
                    startActivityForResult(new Intent(FoodDetailActivity.this,PayForOrderActivity.class),0);
                else
                    Toast.makeText(FoodDetailActivity.this,"未选择商品",Toast.LENGTH_SHORT).show();
                //TODO 跳转下单
            }
        });
        edit_num.setInputType(InputType.TYPE_CLASS_NUMBER);

        cardata=localdata.getString("cardata","");
        contentView= (LinearLayout) findViewById(R.id.contentView);
        String jsonstr=getIntent().getExtras().getString("data");
        drawinfo(jsonstr);
        drawcomment();
        caculprice();
        iseditable=true;
    }
    public void caculprice()
    {
        priceall=0;
        edit_num.setText("0");
        cardata=localdata.getString("cardata","");
        try {
            if(cardata.equals(""))
                carbyseller=new JSONObject();
            else
                carbyseller=new JSONObject(cardata);
            carItems=carbyseller.getJSONObject(String.valueOf(sellerid));
        } catch (JSONException e) {
            carItems=new JSONObject();
        }

        try {
            Iterator it=carItems.keys();//开始遍历json
            while (it.hasNext())
            {
                String key= (String) it.next();
                JSONObject cariteminfo=carItems.getJSONObject(key);
                Double price=cariteminfo.getDouble("price");
                int num=cariteminfo.getInt("num");
                priceall+=num*price;
                if(Integer.valueOf(key)==foodid)
                    edit_num.setText(String.valueOf(num));
            }
            text_price.setText("¥"+priceall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changenum(int i,boolean refleshtext)
    {
        int num=0;
        String str=edit_num.getText().toString();
        if(!str.equals(""))
            num=Integer.valueOf(str);
        JSONObject cariteminfo= null;
        try {
            cariteminfo = carItems.getJSONObject(String.valueOf(foodid));
        } catch (JSONException e) {
        }
        try {
            if(cariteminfo==null||cariteminfo.length()==0)
            {
                cariteminfo=new JSONObject();
                cariteminfo.put("price",price);
                cariteminfo.put("name",name);
                cariteminfo.put("num",num);
                cariteminfo.put("foodid",foodid);
            }
            else {
                num+=i;
                cariteminfo.put("num", num);
            }
            if(refleshtext)
                edit_num.setText(String.valueOf(num));
            carItems.put(String.valueOf(foodid),cariteminfo);
            carbyseller.put(String.valueOf(sellerid),carItems);
            localdata.edit().putString("cardata",carbyseller.toString()).commit();
            caculprice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void drawinfo(String jsonstr)
    {
        try {
            JSONObject jsonObject=new JSONObject(jsonstr);
            foodid=jsonObject.getInt("foodid");
            sellerid=jsonObject.getString("sellerid");
            name=jsonObject.getString("name");
            price=jsonObject.getDouble("price");
            videourl=jsonObject.getString("url");
            int comment=jsonObject.getInt("comment");
            String des=jsonObject.getString("des");
            ImageView img_title= (ImageView) contentView.findViewById(R.id.img_title);
            TextView text_price= (TextView) contentView.findViewById(R.id.text_price);
            TextView text_name= (TextView) contentView.findViewById(R.id.text_name);
            GetImgByUrl.setUrlImg(img_title,"http://"+ip+"/ChujianServer/images/"+sellerid+"/"+foodid+".png",true);
            text_price.setText("¥"+price);
            text_name.setText(name);
            host=localdata.getInt("host",80);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void drawcomment()
    {
        new HttpRequestUtil("http://" + ip +":"+host+ "/ChujianServer/user/getfoodcomment", "foodid=" + foodid, new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                try {
                    JSONArray jsonArray=new JSONArray(data);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jo=jsonArray.getJSONObject(i);
                        LayoutInflater inflater = (LayoutInflater)FoodDetailActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
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
        },this).execute();

    }
}
