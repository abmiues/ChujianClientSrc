package com.abmiues.chujian;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.abmiues.chujian.R.id.view;

/**
 * Created by Administrator on 2017/2/20.
 */

public class Fragment_person extends Fragment{
    private View view;
    private Button btn_exist;
    private SharedPreferences localdata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_person, container, false);
        btn_exist=(Button) view.findViewById(R.id.btn_exit);
        localdata=getActivity().getSharedPreferences("localdata", 0);
        btn_exist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                localdata.edit().remove("sessionid").commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}
