package com.luy.teaism.ui.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luy.teaism.R;
import com.luy.teaism.adapter.CommentAdapter;
import com.luy.teaism.bean.CommentUserBean;
import com.luy.teaism.bean.TeaFoodBean;
import com.luy.teaism.ui.ResourceItemDivider;
import com.luy.teaism.utils.Constants;
import com.luy.teaism.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class TeaFoodDetailActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.create_date)
    TextView createDate;
    @BindView(R.id.web_content)
    WebView webContent;
    @BindView(R.id.good_toggle_button)
    ToggleButton goodToggle;
    //    @BindView(R.id.favorite_toggle_button)
//    ToggleButton favoriteToggle;
    @BindView(R.id.good_count)
    TextView goodCount;

    @BindView(R.id.create_comment)
    LinearLayout createComment;
    @BindView(R.id.no_comment)
    TextView noComment;
    @BindView(R.id.comment_recycler)
    RecyclerView commentRecycler;

    TeaFoodBean teaFoodBean;
    List<CommentUserBean> commentUserBeanList;

    //    private SharedPreferences sharedPreferences;
    private static final int HANDLER_SHOW_COMMENT_TAG = 0x002201;
//    private static final int HANDLER_COMMENT_SUCCESS_TAG = 0x002102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_food_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

//        sharedPreferences = getSharedPreferences(Constants.INIT_SETTING_SHARED, MODE_APPEND);
        teaFoodBean = (TeaFoodBean) getIntent().getSerializableExtra("TeaFoodBean");
        initView();
        initRecyclerView();
        goodToggle.setOnCheckedChangeListener(this);
        createComment.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerView();
    }

    public void initView() {
        title.setText(teaFoodBean.getTitle());
        author.setText(teaFoodBean.getAuthor());
        createDate.setText(Utils.parseToDate(teaFoodBean.getCreate_date()));
        webContent.loadDataWithBaseURL(null, teaFoodBean.getArticle(), "text/html", "utf-8", null);
        goodCount.setText(teaFoodBean.getGoodCount() + "");
        // TODO: 7/10 0010 评论
    }

    public void initRecyclerView() {
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentRecycler.addItemDecoration(new ResourceItemDivider(this, R.drawable.divider));
    }

    public void setRecyclerView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "POST");
                OkHttpUtils.post().url(Utils.getURL() + "/get_comment")
                        .addParams("tea_food_id", teaFoodBean.getId() + "")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("TAG", response);
                        List<CommentUserBean> commentUserBeanList;
                        Type type = new TypeToken<ArrayList<CommentUserBean>>() {
                        }.getType();
                        Gson gson = new GsonBuilder().create();
                        commentUserBeanList = gson.fromJson(response, type);
                        Message message = new Message();
                        message.what = HANDLER_SHOW_COMMENT_TAG;
                        message.obj = commentUserBeanList;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SHOW_COMMENT_TAG:
                    commentUserBeanList = (List<CommentUserBean>) msg.obj;
                    if (commentUserBeanList.size() > 0) {
                        noComment.setVisibility(View.GONE);
                        CommentAdapter adapter = new CommentAdapter(commentUserBeanList);
                        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        commentRecycler.setAdapter(adapter);
                        commentRecycler.setNestedScrollingEnabled(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == goodToggle) {
            int count = Integer.parseInt(goodCount.getText().toString());
            if (isChecked)// TODO: 7/10 0010 点赞
                goodCount.setText(count + 1 + "");
            else
                goodCount.setText(count - 1 + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_comment:
                Intent intentCreateComment = new Intent(this, CreateCommentActivity.class);
                intentCreateComment.putExtra("tea_food_id", teaFoodBean.getId());
                startActivityForResult(intentCreateComment, Constants.REQUEST_CODE_CREATE_COMMENT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_CREATE_COMMENT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(TeaFoodDetailActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

//    // 让菜单同时显示图标和文字
//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        if (menu != null) {
//            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
//                try {
//                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    method.setAccessible(true);
//                    method.invoke(menu, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return super.onMenuOpened(featureId, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_star:
                Toast.makeText(this, "Star", Toast.LENGTH_SHORT).show();// TODO: 7/10 0010 收藏
                break;
            default:
                break;
        }
        return true;
    }
}
