package com.luy.teaism.ui.module;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luy.teaism.R;
import com.luy.teaism.utils.Constants;
import com.luy.teaism.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class CreateCommentActivity extends AppCompatActivity {

    @BindView(R.id.comment_content)
    EditText commentContent;
    @BindView(R.id.commit_comment)
    Button commitComment;

    int teaFoodId;
    int commentatorId;
    String comment;

    private SharedPreferences sharedPreferences;
    private static final int HANDLER_COMMENT_SUCCESS_TAG = 0x002102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        teaFoodId = getIntent().getIntExtra("tea_food_id", 1);
        sharedPreferences = getSharedPreferences(Constants.INIT_SETTING_SHARED, MODE_APPEND);
        commentatorId = sharedPreferences.getInt(Constants.LOGGED_USER_ID, 0);

        commitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentContent.getText().toString();
                if (!comment.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils.post().url(Utils.getURL() + "/create_comment")
                                    .addParams("tea_food_id", teaFoodId + "")
                                    .addParams("commentator_id", commentatorId + "")
                                    .addParams("comment", comment).build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Toast.makeText(CreateCommentActivity.this,
                                                    "评论失败，请重试", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            if (response != null && response.equals("1")) {
                                                Message message = new Message();
                                                message.what = HANDLER_COMMENT_SUCCESS_TAG;
                                                message.arg1 = 1;
                                                handler.sendMessage(message);
                                            }
                                        }
                                    });

                        }
                    }).start();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_COMMENT_SUCCESS_TAG) {
                if (msg.arg1 == 1) {
                    Intent intent = new Intent(CreateCommentActivity.this, TeaFoodDetailActivity.class);
                    intent.putExtra(Constants.CREATE_COMMENT_INTENT, comment);
                    CreateCommentActivity.this.setResult(RESULT_OK, intent);
                    CreateCommentActivity.this.finish();
                }
            }
        }
    };

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
