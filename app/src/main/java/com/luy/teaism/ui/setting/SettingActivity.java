package com.luy.teaism.ui.setting;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.luy.teaism.R;
import com.luy.teaism.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{


    @BindView(R.id.sign_out)
    Button sign_out;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        sign_out.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(Constants.INIT_SETTING_SHARED, MODE_APPEND);
        editor = sharedPreferences.edit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean(Constants.LOGGED_IN, false)) {
            sign_out.setVisibility(View.VISIBLE);
        } else {
            sign_out.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("确定要退出当前账号吗？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.remove(Constants.LOGGED_IN).commit();
                        editor.remove(Constants.LOGGED_USER_ID).commit();
                        editor.remove(Constants.LOGGED_USER_JSON).commit();
                        sign_out.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
    }

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
