package com.v2cc.im.blah;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.v2cc.im.blah.models.Constants;
import com.v2cc.im.blah.db.DataBaseHelper;
import com.v2cc.im.blah.utils.ActivityCollector;
import com.v2cc.im.blah.managers.ContactsAsyncQueryHandler;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 10/26/2015.
 * If this class works, I created it. If not, I didn't.
 */
public class LoginActivity extends BaseActivity {
    private EditText editText;
    private Button signInButton;
    private String userPhone;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);

        // Set up the login form.
        editText = (EditText) findViewById(R.id.phone_input);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    requestLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLogin();
            }
        });
    }

    @Override
    public void initData() {
        // 判断是否来自点击通知
        Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
        if (bundle != null) {
            MainActivity.actionStart(this, bundle);
            ActivityCollector.finishActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSocket.off("login", onLogin);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void requestLogin() {
        // Reset errors.
        editText.setError(null);

        // Store values at the time of the login attempt.
        String userPhone = editText.getText().toString().trim();

        // TODO　Check phone number for validity
        if (TextUtils.isEmpty(userPhone)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            editText.setError(getString(R.string.error_field_required));
            editText.requestFocus();
//            return;
        }

        this.userPhone = userPhone;

        // perform the user login attempt.
//        mSocket.emit("add user", userPhone)

        if (userPhone.length() == 11) {
            DataBaseHelper.getInstance(this).openDataBase();
            // 异步读取通讯录
            ContactsAsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
            // 实例化
            asyncQueryHandler = new ContactsAsyncQueryHandler(this.getContentResolver());
            // 按照sort_key升序查詢
            asyncQueryHandler.startQuery(0, null, ContactsAsyncQueryHandler.CONTACTS_URI,
                    ContactsAsyncQueryHandler.CONTACTS_INFO, null, null,
                    "sort_key COLLATE LOCALIZED asc");
            DataBaseHelper.getInstance(this).closeDataBase();

            // start up MessageActivity
            Bundle bundle = new Bundle();
            MainActivity.actionStart(this, bundle);
            Log.d("LoginActivity", userPhone);
            ActivityCollector.finishActivity(this);
        } else {
            editText.setError(getString(R.string.error_phone_format));
            editText.requestFocus();
        }
    }
}
