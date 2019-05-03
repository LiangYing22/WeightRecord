package com.example.lying.weightrecord;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    Context context;

    RecyclerView WeightRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView =  inflater.inflate(R.layout.fragment_home, container, false);
        WeightRecyclerView = (RecyclerView)homeView.findViewById(R.id.WeightRecyclerView);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //设置为垂直布局，这也是默认的设置
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
        WeightRecyclerView.setLayoutManager(layoutManager);
        //设置适配器
        WeightReclerViewAdapt adapt = new WeightReclerViewAdapt(context);
        WeightRecyclerView.setAdapter(adapt);
        // TODO RecyclerView消费了FrameLayout的滑动手势监听，待处理。
        return homeView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //显示时刷新数据
            WeightReclerViewAdapt adapt = (WeightReclerViewAdapt)WeightRecyclerView.getAdapter();
            adapt.flashData();
            adapt.notifyDataSetChanged();
        }
    }
}
