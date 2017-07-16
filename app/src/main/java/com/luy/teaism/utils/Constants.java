package com.luy.teaism.utils;

/**
 * 常量
 * Created by joker on 6/28 0028.
 */

public class Constants {
    /**
     * startActivityForResult请求码
     */
    // 点击登录按钮
    public static final int REQUEST_CODE_SIGN_IN = 0x001001;
    // 注册
    public static final int REQUEST_CODE_SIGN_UP = 0x001002;

    // 创建评论
    public static final int REQUEST_CODE_CREATE_COMMENT = 0x001082;

//    // 查看详情
//    public static final int REQUEST_CODE_DETAIL=0x001003;


    /**
     * startActivityForResult，登录返回Intent
     */
    // 是否登陆成功
    public static final String LOGGED_IN_INTENT = "logged_in_intent";
    // 登陆成功后用户Bean
    public static final String LOGGED_USER_INTENT = "logged_user_intent";
    // 创建的评论
    public static final String CREATE_COMMENT_INTENT = "create_comment_intent";


    /**
     * 初始化设置 SharedPreferences
     */
    public static final String INIT_SETTING_SHARED = "init_sharedPreferences";
    // 是否已登录
    public static final String LOGGED_IN = "logged_in";
    // 登录用户ID
    public static final String LOGGED_USER_ID = "logged_user_id";
    // 登录的用户
    public static final String LOGGED_USER_JSON = "logged_user";
}
