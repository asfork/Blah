package com.v2cc.im.blah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.v2cc.im.blah.action.UserAction;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.User;
import com.v2cc.im.blah.global.App;
import com.v2cc.im.blah.global.Result;
import com.v2cc.im.blah.managers.ActivityCollector;
import com.v2cc.im.blah.network.NetService;
import com.v2cc.im.blah.utils.SpUtil;

import java.io.IOException;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 10/26/2015.
 * If this class works, I created it. If not, I didn't.
 */
public class LoginActivity extends BaseActivity {
    private SharedPreferences sp;
    private Context mContext;

    private EditText mAccountText;
    private Button signInButton;
    private Button signUpButton;

    private NetService mNetService = NetService.getInstance();

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mContext = this;

        mAccountText = (EditText) findViewById(R.id.et_account);
        signUpButton = (Button) findViewById(R.id.btn_sign_up);
        signInButton = (Button) findViewById(R.id.btn_sign_in);

    }

    @Override
    public void initEvents() {
        // 判断是否首次登录
        SpUtil.getInstance();
        sp = SpUtil.getSharePerference(this);
        SpUtil.getInstance();
        boolean isFirst = SpUtil.isFirst(sp);
        if (!isFirst) {
            // 判断是否来自点击通知
            Bundle bundle = getIntent().getBundleExtra(App.EXTRA_BUNDLE);
            if (bundle != null) {
                MainActivity.actionStart(this, bundle);
                ActivityCollector.finishActivity(this);
            } else {
                SpUtil.getInstance();
                SpUtil.setBooleanSharedPreference(sp, "isFirst", true);
                bundle = new Bundle();
                MainActivity.actionStart(this, bundle);
                ActivityCollector.finishActivity(this);
            }
        }

        mAccountText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                String account = mAccountText.getText().toString().trim();
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    tryLogin(account);
                    return true;
                }
                return false;
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRegister();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mAccountText.getText().toString().trim();
                Log.d("登录帐号为 ", account);
                // Reset errors.
                mAccountText.setError(null);
                // TODO　Check phone number for validity
                if (TextUtils.isEmpty(account)) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    mAccountText.setError(getString(R.string.error_field_required));
                    mAccountText.requestFocus();
                } else if (account.length() == 11) {
                    tryLogin(account);
                } else {
                    mAccountText.setError(getString(R.string.error_account_format));
                    mAccountText.requestFocus();
                }
            }
        });
    }

    private void tryRegister() {
        Bundle bundle = new Bundle();
        RegistrationActivity.actionStart(mContext, bundle);
    }

    private void tryLogin(final String account) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Sign In");
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected void onCancelled() {
                super.onCancelled();
                progressDialog.dismiss();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    mNetService.closeConnection();
                    mNetService.onInit(LoginActivity.this);
                    mNetService.setupConnection();
                    Log.d("network", "set up connection");
                    if (!mNetService.isConnected()) {
                        return 0;
                    }

                    User user = new User();
                    user.setAccount(account);
                    UserAction.loginVerify(user);
                    ApplicationData data = ApplicationData.getInstance();
                    data.initData(LoginActivity.this);
                    data.start();
                    System.out.println(data.getReceivedMessage().getResult());
                    if (data.getReceivedMessage().getResult() == Result.LOGIN_SUCCESS)
                        return 1;// 登录成功
                    else if (data.getReceivedMessage().getResult() == Result.LOGIN_FAILED)
                        return 2;// 登录失败
                } catch (IOException e) {
                    Log.d("network", "IO error");
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                if (result == 0) {
                    Toast.makeText(mContext, "Server error", Toast.LENGTH_SHORT).show();
                } else {
                    if (result == 2) {
                        Toast.makeText(mContext, "Login failure", Toast.LENGTH_SHORT).show();
                    } else if (result == 1) {
                        Bundle bundle = new Bundle();
                        MainActivity.actionStart(mContext, bundle);
                        Log.d("LoginActivity", account);
                        ActivityCollector.finishActivity(LoginActivity.this);
                    }
                }
            }
        }.execute();
    }
}
