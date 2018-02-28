package com.abmiues.chujian.seller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.abmiues.chujian.WebUi;
import com.abmiues.chujian.pojo.Camera;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class FragmentSellerLivevideo extends Fragment{
    LinearLayout contentView;
    String searchstr="";
    EditText editsearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_seller_livevideo,container,false);
        contentView= (LinearLayout) view.findViewById(R.id.contentView);
        editsearch= (EditText) view.findViewById(R.id.editText);
        ImageButton btnsearch= (ImageButton) view.findViewById(R.id.btn_search);
        Button btnAdd= (Button) view.findViewById(R.id.btn_addCamera);
      /*  btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchstr=editsearch.getText().toString();
                if(searchstr.equals(""))
                    Toast.makeText(getActivity(),"请输入要查找的摄像头名字",Toast.LENGTH_LONG).show();
                else
                    HttpRequestUtil.Send("searchcamera", "name="+searchstr, new HttpSendCallback() {
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
                    });
            }
        });*/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(),SellerAddCamera.class),111);
            }
        });
        drawVideo();
        return view;
    }
    private void drawVideo() {
        HttpRequestUtil.Send("getcamera","", new HttpSendCallback() {
            @Override
            public void getdata(String data) {
                if (data.equals(""))
                    Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_LONG).show();
                else {
                    List<Camera> cameraList =new Gson().fromJson(data,new TypeToken<List<Camera>>(){}.getType());
                   // List<Camera> cameraList= (List<Camera>) JSONArray.toCollection(JSONArray.fromObject(data),Camera.class);
                    for (int i = 0; i < cameraList.size(); i++) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.item_livevideo, null);
                        Camera camera=cameraList.get(i);
                        ImageView img = (ImageView) view.findViewById(R.id.img_video);//直播图片
                        ImageView img_seller=(ImageView)view.findViewById(R.id.img_seller);
                        TextView text_name = (TextView) view.findViewById(R.id.text_name);//商家名字
                        TextView text_num = (TextView) view.findViewById(R.id.text_num);//观看人数
                        final String name=camera.getName();
                        text_name.setText(name);
                        final String url=camera.getAddress();
                        GetImgByUrl.setUrlImg(img, "http://" + GlobleValue.get_ip() + "/ChujianServer/images/" + camera.getSellerid() + "/camera"+camera.getCameraid()+".png",true,0.8);
                        GetImgByUrl.setUrlImg(img_seller, "http://" + GlobleValue.get_ip() + "/ChujianServer/images/" + camera.getSellerid()  + "/icon.png");
                        text_num.setText("0");
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getActivity(),WebUi.class).putExtra("url",url).putExtra("title",name));
                            }
                        });
                        contentView.addView(view);
                    }
                }
            }
        });

    }

    public void Refresh()
    {
        drawVideo();
    }
}
