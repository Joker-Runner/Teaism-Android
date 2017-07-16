package com.luy.teaism.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luy.teaism.R;
import com.luy.teaism.adapter.FragmentAdapter;
import com.luy.teaism.bean.UserBean;
import com.luy.teaism.ui.module.TeaFoodFragment;
import com.luy.teaism.ui.setting.MeActivity;
import com.luy.teaism.ui.setting.SettingActivity;
import com.luy.teaism.ui.setting.SignInActivity;
import com.luy.teaism.utils.Constants;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ImageView nav_icon; // 头像
    private TextView userName; //用户名
    private TextView city;
    private LinearLayout selectCity;

    private int currentModuleType = 0;

    /**
     * FAB可见性
     */
    public static final int FAB_GONE = -1;
    public static final int FAB_VISIBLE = 1;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.MainActivityTheme_NoActionBar);
        this.getWindow().setBackgroundDrawableResource(R.drawable.main_activity_bg);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setViewPager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userName = (TextView) headerView.findViewById(R.id.nav_user_name);
        nav_icon = (ImageView) headerView.findViewById(R.id.nav_icon);
        city = (TextView) headerView.findViewById(R.id.city);
        selectCity = (LinearLayout) headerView.findViewById(R.id.select_city);
        nav_icon.setOnClickListener(this);
        selectCity.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(Constants.INIT_SETTING_SHARED, MODE_APPEND);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userBeanJson;
        if (sharedPreferences.getBoolean(Constants.LOGGED_IN, false)) {
            userBeanJson = sharedPreferences.getString(Constants.LOGGED_USER_JSON, "{}");
            Type type = new TypeToken<UserBean>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            userBean = gson.fromJson(userBeanJson, type);
            if (userBean != null) {
                Glide.with(this).load(userBean.getIcon()).error(R.drawable.icon_default).centerCrop().into(nav_icon);
                userName.setText(userBean.getUsername());
                city.setText(userBean.getCityCode());
                // TODO: 7/9 0009 改城市
            }
        } else {
            nav_icon.setImageResource(R.drawable.icon_none);
            userName.setText(getString(R.string.action_sign));
        }
    }

    public void setViewPager() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        TeaFoodFragment teaFoodFragment = new TeaFoodFragment();
        adapter.addFragment(teaFoodFragment,"茶食");

//        ModuleFragment foodModuleFragment = new ModuleFragment();
//        foodModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_FOOD, this);
//        adapter.addFragment(foodModuleFragment, "茶食");

        ModuleFragment talkModuleFragment = new ModuleFragment();
        talkModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_TALK, this);
        adapter.addFragment(talkModuleFragment, "茶言");

        ModuleFragment awakenModuleFragment = new ModuleFragment();
        awakenModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_AWAKEN, this);
        adapter.addFragment(awakenModuleFragment, "茶悟");

        ModuleFragment associationModuleFragment = new ModuleFragment();
        associationModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_ASSOCIATION, this);
        adapter.addFragment(associationModuleFragment, "茶社活动");

        ModuleFragment cultureModuleFragment = new ModuleFragment();
        cultureModuleFragment.setArgs(ModuleFragment.MODULE_TYPE_CULTURE, this);
        adapter.addFragment(cultureModuleFragment, "茶文化");

//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentModuleType = position;
                switch (position) {
                    case ModuleFragment.MODULE_TYPE_FOOD:
                        handler.sendEmptyMessage(FAB_GONE);
                        break;
                    case ModuleFragment.MODULE_TYPE_TALK:
                        handler.sendEmptyMessage(FAB_GONE);
                        break;
                    case ModuleFragment.MODULE_TYPE_AWAKEN:
                        handler.sendEmptyMessage(FAB_VISIBLE);
                        break;
                    case ModuleFragment.MODULE_TYPE_ASSOCIATION:
                        handler.sendEmptyMessage(FAB_VISIBLE);
                        break;
                    case ModuleFragment.MODULE_TYPE_CULTURE:
                        handler.sendEmptyMessage(FAB_GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FAB_GONE:
                    fab.setVisibility(View.GONE);
                    break;
                case FAB_VISIBLE:
                    fab.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_icon:
                drawer.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sharedPreferences.getBoolean(Constants.LOGGED_IN, false)) {
                            Intent intent = new Intent(MainActivity.this, MeActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                            startActivityForResult(intent, Constants.REQUEST_CODE_SIGN_IN);
                        }
                    }
                }, 200);
                break;
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, NewArticleActivity.class);
                intent.putExtra("type", currentModuleType);
                startActivity(intent);
//                if (currentMmduleType == ModuleFragment.MODULE_TYPE_AWAKEN) {
//                    Intent intent = new Intent(MainActivity.this,NewArticleActivity.class);
//                    startActivity(intent);
//                } else if (currentMmduleType == ModuleFragment.MODULE_TYPE_ASSOCIATION) {
//                    Intent intent = new Intent(MainActivity.this,NewArticleActivity.class);
//                    startActivity(intent);
//                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK && data.getBooleanExtra(Constants.LOGGED_IN_INTENT, false)) {
                    // 登陆成功
                    editor.putBoolean(Constants.LOGGED_IN, true).commit();
                    UserBean userBean = (UserBean) data.getSerializableExtra(Constants.LOGGED_USER_INTENT);
                    Log.d("TAG", userBean.getIcon());
                    Log.d("TAG", userBean.getUsername());
                    Glide.with(this).load(userBean.getIcon()).centerCrop().into(nav_icon);
                    userName.setText(userBean.getUsername());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case R.id.nav_home:
                        Intent intentHome = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.nav_star:
                        Intent intentStar = new Intent(MainActivity.this, StarActivity.class);
                        startActivity(intentStar);
                        break;
                    case R.id.nav_record:
                        Toast.makeText(MainActivity.this, "开发中... 请期待", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_scan:
                        Intent intentScan = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(intentScan);
                        break;
                    case R.id.nav_setting:
                        Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intentSetting);
                        break;
                    case R.id.nav_help_feedback:
                        break;
                    case R.id.nav_share:
                        break;
                    default:
                        break;
                }
            }
        }, 200);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
