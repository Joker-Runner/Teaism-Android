package com.luy.teaism.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luy.teaism.R;
import com.luy.teaism.adapter.TeaFoodModuleAdapter;
import com.luy.teaism.bean.TeaFoodBean;
import com.luy.teaism.ui.module.TeaFoodDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final int MODULE_TYPE_FOOD = 0; // 茶食
    public static final int MODULE_TYPE_TALK = 1; // 茶言
    public static final int MODULE_TYPE_AWAKEN = 2; // 茶悟
    public static final int MODULE_TYPE_ASSOCIATION = 3; // 茶社活动
    public static final int MODULE_TYPE_CULTURE = 4; // 茶文化

    private int moduleType;
    private MainActivity mainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public ModuleFragment() {
        // Required empty public constructor
    }

    public void setArgs(int moduleType, MainActivity mainActivity) {
        this.moduleType = moduleType;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_module, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initView();
        initRecyclerView();
        return view;
    }
    
    public void initView(){
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getContext(), android.R.color.holo_green_light),
                ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
    }

    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ResourceItemDivider(getContext(), R.drawable.divider));
        TeaFoodModuleAdapter teaFoodModuleAdapter = new TeaFoodModuleAdapter(getTeaFoodBeanList());
        teaFoodModuleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(),TeaFoodDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(teaFoodModuleAdapter);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        // TODO: 6/29 0029 刷新 
        swipeRefreshLayout.setRefreshing(false);
    }

    public List<TeaFoodBean> getTeaFoodBeanList() {
        List<TeaFoodBean> teaFoodBeanList = new ArrayList<>();
        TeaFoodBean teaFoodBean;
        for (int i = 0; i < 20; i++) {
            teaFoodBean = new TeaFoodBean();
            teaFoodBean.setTitle("该模块是对有关于茶的吃食的介绍，显示一系列的茶食品，还有对应的制作步骤，让爱茶人见到茶的另一功能。");
            teaFoodBeanList.add(teaFoodBean);
        }
        return teaFoodBeanList;
    }
}
