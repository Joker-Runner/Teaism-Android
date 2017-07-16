package com.luy.teaism.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.luy.teaism.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

public class NewArticleActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rich_editor)
    RichEditor richEditor;
    @BindView(R.id.action_undo)
    ImageButton actionUndo;
    @BindView(R.id.action_redo)
    ImageButton actionRedo;
    @BindView(R.id.action_bold)
    ImageButton actionBold;
    @BindView(R.id.action_h1)
    ImageButton actionHeading1;
    @BindView(R.id.action_h2)
    ImageButton actionHeading2;
    @BindView(R.id.action_h3)
    ImageButton actionHeading3;
    @BindView(R.id.action_align_left)
    ImageButton actionAlignLeft;
    @BindView(R.id.action_align_center)
    ImageButton actionAlignCenter;
    @BindView(R.id.action_align_right)
    ImageButton actionAlignRight;
    @BindView(R.id.action_insert_image)
    ImageButton actionInsertImage;
    @BindView(R.id.action_insert_bullets)
    ImageButton actionInsertBullets;
    @BindView(R.id.action_insert_numbers)
    ImageButton actionInsertNumbers;
    @BindView(R.id.action_insert_link)
    ImageButton actionInsertLink;

    String html;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle(getIntent().getIntExtra("type", 0));
        sharedPreferences = getSharedPreferences("shared", MODE_APPEND);
        editor = sharedPreferences.edit();
        initEditor();
    }

    /**
     * 初始化编辑器
     */
    public void initEditor() {
        richEditor.setEditorHeight(300);
        richEditor.setEditorFontSize(18);
        richEditor.setEditorFontColor(Color.GRAY);
        //richEditor.setEditorBackgroundColor(Color.BLUE);
        //richEditor.setBackgroundColor(Color.BLUE);
        //richEditor.setBackgroundResource(R.drawable.bg);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        richEditor.setPlaceholder("Insert text here...");
        //richEditor.setInputEnabled(false);
        setListener();
        html = sharedPreferences.getString("html", "");
        if (!html.equals("")) {
            richEditor.setHtml(html);
        }
    }

    /**
     * 设置监听器
     */
    public void setListener() {
        actionUndo.setOnClickListener(this);
        actionRedo.setOnClickListener(this);
        actionBold.setOnClickListener(this);
        actionHeading1.setOnClickListener(this);
        actionHeading2.setOnClickListener(this);
        actionHeading3.setOnClickListener(this);
        actionAlignLeft.setOnClickListener(this);
        actionAlignCenter.setOnClickListener(this);
        actionAlignRight.setOnClickListener(this);
        actionInsertImage.setOnClickListener(this);
        actionInsertBullets.setOnClickListener(this);
        actionInsertNumbers.setOnClickListener(this);
        actionInsertLink.setOnClickListener(this);
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d("TAG", text);
                html = text;
//                webPreview.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_undo:
                richEditor.undo();
                break;
            case R.id.action_redo:
                richEditor.redo();
                break;
            case R.id.action_bold:
                richEditor.setBold();
                break;
            case R.id.action_h1:
                richEditor.setHeading(2);
                break;
            case R.id.action_h2:
                richEditor.setHeading(3);
                break;
            case R.id.action_h3:
                richEditor.setHeading(4);
                break;
            case R.id.action_align_left:
                richEditor.setAlignLeft();
                break;
            case R.id.action_align_center:
                richEditor.setAlignCenter();
                break;
            case R.id.action_align_right:
                richEditor.setAlignRight();
                break;
            case R.id.action_insert_bullets:
                richEditor.setBullets();
                break;
            case R.id.action_insert_numbers:
                richEditor.setNumbers();
                break;
            case R.id.action_insert_link:
                richEditor.insertLink("https://github.com/wasabeef", "Link");
                break;
            case R.id.action_insert_image:
                richEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
                        "dachshund");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        menu.add(1,1,1,"提交").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        getMenuInflater().inflate(R.menu.activity_new_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (html != null && !html.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("是否保存草稿");
                    builder.setCancelable(true);
                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putString("html", html).commit();
                            finish();
                        }
                    });
                    builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    finish();
                }
                break;
            case R.id.submit:
                Toast.makeText(this, "提交", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
