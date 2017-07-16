package com.luy.teaism.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.luy.teaism.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StarActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        initRecyclerView();
//        setViewPager();
    }

//    public void setViewPager() {
//        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
//
//        TeaFoodFragment teaFoodFragment = new TeaFoodFragment();
//        adapter.addFragment(teaFoodFragment,"茶食");
//
////        ModuleFragment foodModuleFragment = new ModuleFragment();
////        foodModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_FOOD, this);
////        adapter.addFragment(foodModuleFragment, "茶食");
//
//        ModuleFragment talkModuleFragment = new ModuleFragment();
//        talkModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_TALK, this);
//        adapter.addFragment(talkModuleFragment, "茶言");
//
//        ModuleFragment awakenModuleFragment = new ModuleFragment();
//        awakenModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_AWAKEN, this);
//        adapter.addFragment(awakenModuleFragment, "茶悟");
//
//        ModuleFragment associationModuleFragment = new ModuleFragment();
//        associationModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_ASSOCIATION, this);
//        adapter.addFragment(associationModuleFragment, "茶社活动");
//
//        ModuleFragment cultureModuleFragment = new ModuleFragment();
//        cultureModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_CULTURE, this);
//        adapter.addFragment(cultureModuleFragment, "茶文化");
//
////        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        viewPager.setAdapter(adapter);
////        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////                currentModuleType = position;
////                switch (position) {
////                    case ModuleFragment.MODULE_TYPE_FOOD:
////                        handler.sendEmptyMessage(FAB_GONE);
////                        break;
////                    case ModuleFragment.MODULE_TYPE_TALK:
////                        handler.sendEmptyMessage(FAB_GONE);
////                        break;
////                    case ModuleFragment.MODULE_TYPE_AWAKEN:
////                        handler.sendEmptyMessage(FAB_VISIBLE);
////                        break;
////                    case ModuleFragment.MODULE_TYPE_ASSOCIATION:
////                        handler.sendEmptyMessage(FAB_VISIBLE);
////                        break;
////                    case ModuleFragment.MODULE_TYPE_CULTURE:
////                        handler.sendEmptyMessage(FAB_GONE);
////                        break;
////                }
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////            }
////        });
//
////        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//    }

//    public void initRecyclerView() {
//        starRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        starRecyclerView.addItemDecoration(new ResourceItemDivider(this, R.drawable.divider));
//        TeaFoodModuleAdapter teaFoodModuleAdapter = new TeaFoodModuleAdapter(getTeaFoodBeanList());
//        teaFoodModuleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(StarActivity.this, TeaFoodDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//        starRecyclerView.setAdapter(teaFoodModuleAdapter);
//    }
//
//    public List<TeaFoodBean> getTeaFoodBeanList() {
//        List<TeaFoodBean> teaFoodBeanList = new ArrayList<>();
//        TeaFoodBean teaFoodBean;
//        for (int i = 0; i < 20; i++) {
//            teaFoodBean = new TeaFoodBean();
//            teaFoodBean.setTitle("该模块是对有关于茶的吃食的介绍，显示一系列的茶食品，还有对应的制作步骤，让爱茶人见到茶的另一功能。");
//            teaFoodBeanList.add(teaFoodBean);
//        }
//        return teaFoodBeanList;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
