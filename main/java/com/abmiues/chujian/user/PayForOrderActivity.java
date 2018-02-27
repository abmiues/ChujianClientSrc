package com.abmiues.chujian.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.GetImgByUrl;
import com.abmiues.chujian.HttpRequestUtil;
import com.abmiues.chujian.HttpSendCallback;
import com.abmiues.chujian.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PayForOrderActivity extends AppCompatActivity {
    LinearLayout contentView;
    String sellerid;
    String cardata;
    JSONObject carItems;
    Button btn_pay;
    double priceall;
    JSONObject carbyseller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_order);
        cardata= GlobleValue.get_globleData().getString("cardata","");
        btn_pay= (Button) findViewById(R.id.btn_pay);
        contentView= (LinearLayout) findViewById(R.id.contentView);
        Button btn_setaddr= (Button) findViewById(R.id.btn_setaddr);
        Button btn_editaddr= (Button) findViewById(R.id.btn_editaddr);
        ImageButton imgbtn_back= (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayForOrderActivity.this.finish();
            }
        });
        final TextView text_addr= (TextView) findViewById(R.id.text_addr);
        final EditText edit_addr= (EditText) findViewById(R.id.edit_addr);
        final RelativeLayout layout_editaddr= (RelativeLayout) findViewById(R.id.layout_editaddr);
        final RelativeLayout layout_addr= (RelativeLayout) findViewById(R.id.layout_addr);
        btn_setaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_editaddr.setVisibility(View.GONE);
                layout_addr.setVisibility(View.VISIBLE);
                text_addr.setText(edit_addr.getText().toString());
            }
        });
        btn_editaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_editaddr.setVisibility(View.VISIBLE);
                layout_addr.setVisibility(View.GONE);
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardata=GlobleValue.get_globleData().getString("cardata","");
                 HttpRequestUtil.Send("addorder", "data=" + cardata, new HttpSendCallback() {
                    @Override
                    public void getdata(String data) {
                        if (Integer.valueOf(data)>0)
                        {
                            Toast.makeText(PayForOrderActivity.this,"下单成功",Toast.LENGTH_LONG);
                            PayForOrderActivity.this.finish();
                            GlobleValue.get_globleData().edit().putString("cardata","").commit();
                        }
                    }
                });


            }
        });
        caculprice();
    }

    public void caculprice()
    {
        priceall=0;
        try {
            if(cardata.equals(""))
                carbyseller=new JSONObject();
            else
                carbyseller=new JSONObject(cardata);//读取购物车json字符串，放这里是因为这里有try,省得多写一个try
        } catch (JSONException e) {
        }

        Iterator sellers=carbyseller.keys();
        while (sellers.hasNext())
        {
            String sellerid= (String) sellers.next();
            try {
                carItems=carbyseller.getJSONObject(sellerid);//读取本商店购物车
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
                    if(num>0)
                    createItem(sellerid,cariteminfo.getString("name"),cariteminfo.getInt("foodid"),num,price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        btn_pay.setText("确认支付(¥"+priceall+")");
    }
    public void createItem(String sellerid,String name,int foodid,int num,double price)
    {
        LayoutInflater inflater = (LayoutInflater)PayForOrderActivity.this.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_order, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_food);//商家头像
        TextView text_name= (TextView) view.findViewById(R.id.name);//商家名字
        TextView text_remark= (TextView) view.findViewById(R.id.text_remark);//口味
        TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
        TextView text_num= (TextView) view.findViewById(R.id.text_num);
        text_name.setText(name);
        text_num.setText(String.valueOf(num));
        GetImgByUrl.setUrlImg(img,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/"+foodid+".png");
        text_price.setText("¥"+price);
        contentView.addView(view);
    }
}
