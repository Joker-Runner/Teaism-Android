package com.luy.teaism.ui.module;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luy.teaism.R;
import com.luy.teaism.adapter.TeaFoodAdapter;
import com.luy.teaism.bean.TeaFoodBean;
import com.luy.teaism.ui.ResourceItemDivider;
import com.luy.teaism.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeaFoodFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    List<TeaFoodBean> teaFoodBeanList;
    //    private TeaFoodTask teaFoodTask = null;
    private static final int HANDLER_SHOW_TEA_FOOD_TAG = 0x002201;

    public TeaFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tea_food, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initView();
        initRecyclerView();
        setRecyclerView();
        return view;
    }

    public void initView() {
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
//        TeaFoodModuleAdapter teaFoodModuleAdapter = new TeaFoodModuleAdapter(getTeaFoodBeanList());
//        teaFoodModuleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getContext(), TeaFoodDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(teaFoodModuleAdapter);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        // TODO: 6/29 0029 刷新
        swipeRefreshLayout.setRefreshing(false);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_TEA_FOOD_TAG:
                    teaFoodBeanList = (List<TeaFoodBean>) msg.obj;
                    if (teaFoodBeanList.size()!=0){
                        TeaFoodAdapter adapter = new TeaFoodAdapter(teaFoodBeanList);
                        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(getContext(),TeaFoodDetailActivity.class);
                                intent.putExtra("TeaFoodBean",teaFoodBeanList.get(position));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
            }
        }
    };

    public void setRecyclerView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG","POST");
                OkHttpUtils.post().url(Utils.getURL() + "/get_tea_food")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("TAG",response);
                        List<TeaFoodBean> teaFoodBeanList ;
                        Type type = new TypeToken<ArrayList<TeaFoodBean>>() {
                        }.getType();
                        Gson gson = new GsonBuilder().create();
                        teaFoodBeanList = gson.fromJson(response, type);
                        Message message = new Message();
                        message.what = HANDLER_SHOW_TEA_FOOD_TAG;
                        message.obj = teaFoodBeanList;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();
    }

//    private class TeaFoodTask extends AsyncTask<Void,Void,ArrayList<TeaFoodBean>>{
//
//        private String teaFoodListJson;
//
//        @Override
//        protected ArrayList<TeaFoodBean> doInBackground(Void... params) {
//            try {
//                RequestBody requestBody = new FormBody.Builder()
////                        .add("start_number", startNumber + "")
//                        .build();
//                Request request = new Request.Builder()
//                        .url(Utils.getURL() + "/get_tea_food").post(requestBody).build();
//                Response response = new OkHttpClient().newCall(request).execute();
//                teaFoodListJson = response.body().string();
//                Log.i("TAG", teaFoodListJson);
//                Type type = new TypeToken<ArrayList<TeaFoodBean>>() {
//                }.getType();
//                Gson gson = new GsonBuilder().create();
//                return gson.fromJson(teaFoodListJson, type);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                return null;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<TeaFoodBean> teaFoodBeenList) {
//            teaFoodTask = null;
//            if (teaFoodBeenList != null) {
//                Message message = new Message();
//                message.arg1 = HANDLER_SHOW_FLOWER_LIST_TAG;
//                message.obj = flowerBeanArrayList;
//                handler.handleMessage(message);
//            }
//        }
//    }
}
