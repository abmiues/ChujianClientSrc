package com.abmiues.chujian.user;

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
import android.widget.Toast;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.GetImgByUrl;
import com.abmiues.chujian.HttpRequestUtil;
import com.abmiues.chujian.HttpSendCallback;
import com.abmiues.chujian.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Fragment_order extends Fragment{
    LinearLayout contentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        contentView= (LinearLayout) view.findViewById(R.id.contentView);
        ImageButton btn_refresh= (ImageButton) view.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrderlist();
            }
        });
        createOrderlist();
        return view;
    }
    public void createOrderlist()
    {
        contentView.removeAllViews();
        HttpRequestUtil.Send("getorderlist","",new HttpSendCallback() {
            @Override
            public void getdata(final String data) {
                if(data.equals(""))
                    Toast.makeText(getActivity(),"数据为空",Toast.LENGTH_LONG).show();
                else
                {
                    try {
                        JSONArray jsonArray=new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.item_orderlist, null);
                            final JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String time=jsonObject.getString("time");
                            String[] arr;
                            arr=time.split(" ");
                            final String sellerid=jsonObject.getString("sellerid");
                            double price=jsonObject.getDouble("price");
                            String remark=jsonObject.getString("remark");
                            String sellername=jsonObject.getString("sellername");
                            final int orderid=jsonObject.getInt("orderid");
                            final int[] state = {jsonObject.getInt("state")};


                            final ImageView img= (ImageView) view.findViewById(R.id.img_seller);//商家头像
                            final ImageView img_food= (ImageView) view.findViewById(R.id.img_food);//商家头像
                            TextView text_name= (TextView) view.findViewById(R.id.sellername);//商家名字
                            final TextView text_foodname= (TextView) view.findViewById(R.id.foodname);//商家名字
                            TextView text_time= (TextView) view.findViewById(R.id.text_time);//口味
                            TextView text_remark= (TextView) view.findViewById(R.id.text_remark);//评价
                            TextView text_price= (TextView) view.findViewById(R.id.text_price);//价格
                            Button btn_intoshop=(Button) view.findViewById(R.id.btn_intoshop);//进入店铺
                            final Button btn_action=(Button) view.findViewById(R.id.btn_action);//评价
                            final TextView text_state=(TextView)view.findViewById(R.id.text_state);//订单状态
                            text_name.setText(sellername);
                            text_time.setText(arr[0]);

                            if(state[0] ==0)
                            {
                                text_state.setText("等待接单");
                                btn_action.setVisibility(View.GONE);
                            }
                            else if(state[0] ==1)
                            {
                                text_state.setText("等待送达");
                                //btn_action.setVisibility(View.GONE);
                                btn_action.setText("确认送达");
                            }
                            else if(state[0]==3)
                            {
                                text_state.setText("已取消");
                                btn_action.setVisibility(View.GONE);
                            }
                            else
                            {
                                text_state.setText("已完成");
                                btn_action.setVisibility(View.VISIBLE);
                            }

                            GetImgByUrl.setUrlImg(img,"http://"+ GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/icon.png",false,0.6);
                            text_price.setText("¥"+price);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(),OrderDetailActivity.class).putExtra("data",jsonObject.toString()));
                                }
                            });
                            btn_action.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(state[0] ==1)
                                       HttpRequestUtil.Send("updateorder", "state=2&orderid=" + orderid, new HttpSendCallback() {
                                           @Override
                                           public void getdata(String data) {
                                               btn_action.setText("评价");
                                               text_state.setText("已完成");
                                               state[0] =2;
                                           }
                                       });//确认送达
                                    else
                                        startActivity(new Intent(getActivity(),CommentActivity.class).putExtra("data",jsonObject.toString()));
                                }
                            });
                            btn_intoshop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    HttpRequestUtil.Send("getsellerbyid","sellerid="+sellerid,new HttpSendCallback() {
                                        @Override
                                        public void getdata(String data) {
                                            if(data.equals(""))
                                                Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_LONG).show();
                                            else
                                            {
                                                startActivity(new Intent(getActivity(),SellerDetailActivity.class).putExtra("sellerinfo",data));
                                            }
                                        }
                                    });
                                }
                            });
                            HttpRequestUtil.Send("getorderdetail","orderid="+orderid,new HttpSendCallback() {
                                @Override
                                public void getdata(String data) {
                                    if(data.equals(""))
                                        Toast.makeText(getActivity(),"数据错误",Toast.LENGTH_LONG).show();
                                    else
                                    {
                                        try {
                                            JSONArray jsonArray=new JSONArray(data);
                                            int count=jsonArray.length();
                                            JSONObject jsonObject=jsonArray.getJSONObject(1);
                                            String foodname=jsonObject.getString("foodname");
                                            int foodid=jsonObject.getInt("foodid");
                                            GetImgByUrl.setUrlImg(img_food,"http://"+GlobleValue.get_ip()+"/ChujianServer/images/"+sellerid+"/"+foodid+".png");
                                            if(count>1)
                                                text_foodname.setText(foodname+"等"+count+"样");
                                            else
                                                text_foodname.setText(foodname);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                            contentView.addView(view);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
