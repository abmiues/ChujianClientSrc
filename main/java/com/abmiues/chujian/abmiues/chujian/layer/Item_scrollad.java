package com.abmiues.chujian.abmiues.chujian.layer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abmiues.chujian.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/21.
 * 主页滚动广告部件
 */

public class Item_scrollad extends LinearLayout{
    View view;
    ArrayList<View> viewList;

    public Item_scrollad(Context context)
    {this(context,null);}
    public Item_scrollad(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        view= LayoutInflater.from(context).inflate(R.layout.item_scrollad,this,true);
        ViewPager pager= (ViewPager) view.findViewById(R.id.viewpaper_ad);
        View img=createpaper(context,R.mipmap.ad1);
        View img2=createpaper(context,R.mipmap.ad2);
        View img3=createpaper(context,R.mipmap.ad3);
        View img4=createpaper(context,R.mipmap.ad4);
        viewList = new ArrayList<View>();
        viewList.add(img);
        viewList.add(img2);
        viewList.add(img3);
        viewList.add(img4);
        PagerAdapter adapter=new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public View createpaper(Context context,int res)
    {
        ImageView img=new ImageView(context);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(res);
        return img;
    }


}
