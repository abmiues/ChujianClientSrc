package com.abmiues.chujian.seller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.GetImgByUrl;
import com.abmiues.chujian.HttpRequestUtil;
import com.abmiues.chujian.HttpSendCallback;
import com.abmiues.chujian.R;
import com.abmiues.chujian.pojo.Order;
import com.abmiues.chujian.pojo.OrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.abmiues.chujian.R.id.sellername;


public class SellerOrderDetailActivity extends AppCompatActivity {
    int orderid;
    LinearLayout contentView;
    String sellerid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);
        ImageView img= (ImageView) findViewById(R.id.img_seller);//商家头像
        TextView text_address=(TextView)findViewById(R.id.text_addr);//地址
        TextView text_name= (TextView) findViewById(sellername);//商家名字
        TextView text_time= (TextView) findViewById(R.id.text_time);//时间
        TextView text_remark= (TextView)findViewById(R.id.text_remark);//备注
        TextView text_priceall= (TextView) findViewById(R.id.text_priceall);//价格
        contentView= (LinearLayout) findViewById(R.id.contentView);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerOrderDetailActivity.this.finish();
            }
        });

        Order oneOrder= new Gson().fromJson(getIntent().getExtras().getString("data"),Order.class);
        String time =oneOrder.getTime();
        sellerid =oneOrder.getSellerid();
        double price = oneOrder.getPrice();
        String remark = oneOrder.getRemark();
        String userName = oneOrder.getUsername();
        orderid = oneOrder.getOrderid();
        text_name.setText(userName);
        text_time.setText(time);
        GetImgByUrl.setUrlImg(img,"http://"+ GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/icon.png");
        text_priceall.setText("¥"+price);

        createOrderlist();
    }
    private void createOrderlist()
    {
        HttpRequestUtil.Send("getorderdetail","orderid="+orderid,new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                if(data.equals(""))
                    Toast.makeText(SellerOrderDetailActivity.this,"数据为空",Toast.LENGTH_LONG).show();
                else
                {
                    List<OrderDetail> foodDetail =new Gson().fromJson(data,new TypeToken<List<OrderDetail>>(){}.getType());
                    for (int i=0;i<foodDetail.size();i++)
                    {
                        OrderDetail food=foodDetail.get(i);
                        int foodid=food.getFoodid();
                        int num=food.getNum();
                        int orderid=food.getOrderid();
                        String remark=food.getRemark();
                        String sellername=food.getSellername();
                        String foodname=food.getFoodname();
                        double price=food.getPrice();
                        createItem( sellerid, foodname, foodid, num, price);
                    }
                }
            }
        });
    }
    private void createItem(String sellerid,String name,int foodid,int num,double price)
    {
        LayoutInflater inflater = (LayoutInflater)SellerOrderDetailActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_order, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_food);//食物图片
        TextView text_name= (TextView) view.findViewById(R.id.name);//食物名字
        TextView text_remark= (TextView) view.findViewById(R.id.text_remark);//口味
        TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
        TextView text_num= (TextView) view.findViewById(R.id.text_num);
        TextView text_time=(TextView) view.findViewById(R.id.text_time);
        text_time.setVisibility(View.GONE);
        text_name.setText(name);
        text_num.setText(String.valueOf(num));
        GetImgByUrl.setUrlImg(img,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/"+foodid+".png");
        text_price.setText("¥"+price);
        contentView.addView(view);
    }
}
