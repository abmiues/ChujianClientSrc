package com.abmiues.chujian.user;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abmiues.Utils.GlobleValue;
import com.abmiues.chujian.LoginActivity;
import com.abmiues.chujian.R;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Fragment_person extends Fragment{
    private View view;
    private Button btn_exist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_person, container, false);
        btn_exist=(Button) view.findViewById(R.id.btn_exit);
        btn_exist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobleValue.get_globleData().edit().remove("sessionid").commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                GlobleValue.get_pushReciver().DisConnect();
            }
        });
        return view;
    }
}
