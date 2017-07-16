package com.luy.teaism.ui.setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luy.teaism.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;

public class SignUpActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * 请求通讯录的权限ID
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * 处理用户登录的异步任务
     */
    private UserSignUpTask mAuthTask = null;

    /**
     * 获取验证码的异步任务
     */
    private UserGetVerificationCodeTask mUserGetVerificationCodeTask = null;

    // UI references.

    @BindView(R.id.username)
    EditText mUsernameView;
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.verification_code)
    EditText mVerificationCode;
    @BindView(R.id.get_verification_code)
    Button mGetVerificationCode;
    @BindView(R.id.sign_in_button)
    Button mSignUpButton;
    @BindView(R.id.sign_up_progress)
    View mProgressView;
    @BindView(R.id.sign_up_form)
    View mSignUpFormView;

    private Intent intent;

    // 验证码
    private String theVerificationCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        // Set up the login form.
        populateAutoComplete();

        mVerificationCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.sign_up || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        mGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptGetVerificationCode();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        intent = new Intent(this, SignInActivity.class);
    }

    /**
     * 编辑自动补齐
     */
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * 请求通讯录权限
     * SDK > 23 返回true，否则检查申请权限
     *
     * @return 是否拥有权限
     */
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        // 检查需要的权限是否允许，若允许，直接返回true。否则，申请权限
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // 在实际显示权限对话框之前是否显示一个对正在请求权限的解释。
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * 请求权限完成后的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptGetVerificationCode() {
        if (mUserGetVerificationCodeTask != null) {
            return;
        }
        mEmailView.setError(null);
        String email = mEmailView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mUserGetVerificationCodeTask = new UserGetVerificationCodeTask(email);
            mUserGetVerificationCodeTask.execute((Void[]) null);
        }
    }

    /**
     * 登录或按给定邮箱注册用户。
     * 如果有错误（邮箱格式错误，密码过短等）将不会登录。
     */
    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mVerificationCode.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String verificationCode = mVerificationCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid verificationCode, if the user entered one.
        if (TextUtils.isEmpty(verificationCode)) {
            mVerificationCode.setError(getString(R.string.error_field_required));
            focusView = mVerificationCode;
            cancel = true;
        } else if (!isVerificationCodeValid(verificationCode)) {
            mVerificationCode.setError(getString(R.string.error_invalid_verification_code));
            focusView = mVerificationCode;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserSignUpTask(username, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isVerificationCodeValid(String verificationCode) {
        return verificationCode.equals(theVerificationCode);
    }

    /**
     * 显示进度控件
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    /**
     * 读取通讯录中的邮箱List，用于自动补全用户名输入
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignUpActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
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

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 异步任务：用于登录/注册用户。
     */
    private class UserGetVerificationCodeTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;

        UserGetVerificationCodeTask(String email) {
            mEmail = email;
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseData = null;
//            try {
//                RequestBody requestBody = new FormBody.Builder().add("email", mEmail).build();
//                Request request = new Request.Builder()
//                        .url(Constants.getURL() + "/verification_code").post(requestBody).build();
//                Response response = new OkHttpClient().newCall(request).execute();
//                responseData = response.body().string();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return responseData;
        }


        /**
         * @param responseData 0：已被注册；-1：出错；或者未被注册并返回验证码；
         */
        @Override
        protected void onPostExecute(final String responseData) {
            mUserGetVerificationCodeTask = null;
            showProgress(false);
            if (responseData == null || responseData.equals("-1")) {
                Toast.makeText(SignUpActivity.this, "获取验证码失败，请重试", Toast.LENGTH_LONG).show();
            } else if (responseData.equals("0")) {
                Toast.makeText(SignUpActivity.this, "改邮箱已被注册，请检查邮箱是否拼写正确", Toast.LENGTH_LONG).show();
            } else if (responseData.length() == 6) {
                theVerificationCode = responseData;
                Toast.makeText(SignUpActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mUserGetVerificationCodeTask = null;
        }
    }

    /**
     * 异步任务：用于登录/注册用户。
     */
    private class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mEmail;
        private final String mPassword;

        UserSignUpTask(String username, String email, String password) {
            mUsername = username;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String responseData = null;
//            try {
//                RequestBody requestBody = new FormBody.Builder().add("username", mUsername)
//                        .add("email", mEmail).add("password", mPassword).build();
//                Request request = new Request.Builder()
//                        .url(Constants.getURL() + "/sign_up").post(requestBody).build();
//                Response response = new OkHttpClient().newCall(request).execute();
//                responseData = response.body().string();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Log.i("TAG",responseData);
            return responseData != null && responseData.equals("1");
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Log.i("TAG", success + "");
            if (success) {
                //注册成功，跳转到登录界面，并返回用户名
                Bundle bundle = new Bundle();
                bundle.putCharSequence("email", mEmail);
                intent.putExtras(bundle);
                SignUpActivity.this.setResult(RESULT_OK, intent);
                SignUpActivity.this.finish();
            } else {
                Toast.makeText(SignUpActivity.this, "注册失败，请重试", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
