package com.abmiues.chujian;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Fragment_livevideo extends Fragment{
    LinearLayout contentView;
    SharedPreferences localdata;
    String ip;
    String searchstr="";
    EditText editsearch;
    int host;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_livevideo,container,false);
        contentView= (LinearLayout) view.findViewById(R.id.contentView);
        localdata=getActivity().getSharedPreferences("localdata",Context.MODE_PRIVATE);
        ip=localdata.getString("ip","10.0.2.2");
        editsearch= (EditText) view.findViewById(R.id.editText);
        host=localdata.getInt("host",80);
        ImageButton btnsearch= (ImageButton) view.findViewById(R.id.btn_search);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchstr=editsearch.getText().toString();
                if(searchstr.equals(""))
                    Toast.makeText(getActivity(),"请输入要查找的商家名称",Toast.LENGTH_LONG).show();
                else
                    new HttpRequestUtil("http://"+ip+":"+host+"/ChujianServer/user/searchcamera", "name="+searchstr, new HttpSendCallback() {
                        @Override
                        public void getdata(String data) {
                            if(data.equals(""))
                            {
                                Toast.makeText(getActivity(),"未搜索到相关信息",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                startActivity(new Intent(getActivity(), SearchCameraActivity.class).putExtra("data",data));
                            }
                        }
                    },getActivity()).execute();
            }
        });
        drawVideo();
        return view;
    }
    public void drawVideo() {
        new HttpRequestUtil("http://" + ip +":"+host+ "/ChujianServer/user/getcamera","", new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                if (data.equals(""))
                    Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_LONG).show();
                else {

                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.item_livevideo, null);
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ImageView img = (ImageView) view.findViewById(R.id.img_video);//直播图片
                            ImageView img_seller=(ImageView)view.findViewById(R.id.img_seller);
                            TextView text_name = (TextView) view.findViewById(R.id.text_name);//商家名字
                            TextView text_num = (TextView) view.findViewById(R.id.text_num);//观看人数
                            final String name=jsonObject.getString("name");
                            text_name.setText(name);
                            final String url=jsonObject.getString("address");
                            GetImgByUrl.setUrlImg(img, "http://" + ip + "/ChujianServer/images/" + jsonObject.getString("sellerid") + "/camera"+jsonObject.getString("cameraid")+".png",true,0.8);
                            GetImgByUrl.setUrlImg(img_seller, "http://" + ip + "/ChujianServer/images/" + jsonObject.getString("sellerid") + "/icon.png");
                            text_num.setText("0");
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(),WebUi.class).putExtra("url",url).putExtra("title",name));
                                }
                            });
                            contentView.addView(view);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, getActivity()).execute();

    }
}
