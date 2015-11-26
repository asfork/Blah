package com.v2cc.im.blah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.v2cc.im.blah.action.UserAction;
import com.v2cc.im.blah.bean.TranObject;
import com.v2cc.im.blah.bean.User;
import com.v2cc.im.blah.global.Result;
import com.v2cc.im.blah.managers.ActivityCollector;
import com.v2cc.im.blah.network.NetService;
import com.v2cc.im.blah.utils.PhotoUtil;

/**
 * Created by Steve Zhang
 * 15-11-26
 * <p>
 * If it works, I created it. If not, I didn't.
 */
public class RegistrationActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private ImageView mPhotoView;
    private LinearLayout mSelectPhotoLayout;
    private LinearLayout mTakePictureLayout;
    private Button mSignUpBtn;
    private EditText mAccountText;
    private EditText mNameText;

    private String mTakePicturePath;
    private Bitmap mUserPhoto;

    private NetService mNetService = NetService.getInstance();
    private static TranObject mReceivedInfo = null;
    private static boolean mIsReceived = false;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_registration);

        mPhotoView = (ImageView) findViewById(R.id.iv_userphoto);
        mSelectPhotoLayout = (LinearLayout) findViewById(R.id.layout_selectphoto);
        mTakePictureLayout = (LinearLayout) findViewById(R.id.layout_takepicture);
        mSignUpBtn = (Button) findViewById(R.id.btn_signup);
        mAccountText = (EditText) findViewById(R.id.et_account);
        mNameText = (EditText) findViewById(R.id.et_name);
    }

    @Override
    public void initEvents() {
        mSelectPhotoLayout.setOnClickListener(this);
        mTakePictureLayout.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mNameText.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_selectphoto:
                PhotoUtil.selectPhoto(this);
                break;
            case R.id.layout_takepicture:
                mTakePicturePath = PhotoUtil.takePicture(this);
                break;
            case R.id.btn_signup:
                verifyDate();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            verifyDate();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.INTENT_REQUEST_CODE_ALBUM:
                if (data == null) {
                    Log.d("register", "data is null");
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        Log.d("register", "data.getData is null");
                        return;
                    }
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            String path = cursor.getString(column_index);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            if (PhotoUtil.bitmapIsLarge(bitmap)) {
                                Log.d("register", "compressionPhoto");
                                setUserPhoto(PhotoUtil.compressionPhoto(80, path, 1));
                            } else {
                                Log.d("register", "compressImage");
                                setUserPhoto(PhotoUtil.compressImage(bitmap));
                            }
                        }
                    }
                }
                break;

            case PhotoUtil.INTENT_REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    String path = mTakePicturePath;
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (PhotoUtil.bitmapIsLarge(bitmap)) {
                        setUserPhoto(PhotoUtil.compressionPhoto(80, path, 1));
                    } else {
                        setUserPhoto(PhotoUtil.compressImage(bitmap));
                    }
                }
                break;
        }
    }

    private void setUserPhoto(Bitmap bitmap) {
        if (bitmap != null) {
            mUserPhoto = bitmap;
            mPhotoView.setImageBitmap(mUserPhoto);
            return;
        }
        Toast.makeText(this, "Use the default avatar", Toast.LENGTH_SHORT).show();
        mUserPhoto = null;
        mPhotoView.setImageResource(R.drawable.icons_head_00);
    }

    private void verifyDate() {
        String account = mAccountText.getText().toString().trim();
        String name = mNameText.getText().toString().trim();
        if (mUserPhoto == null) {
            Toast.makeText(this, "Please choose an avatar", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(account)) {
            mAccountText.setError(getString(R.string.error_field_required));
            mAccountText.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            mNameText.setError(getString(R.string.error_field_required));
            mNameText.requestFocus();
        } else {
            trySignUp();
        }
    }

    private void trySignUp() {
        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("Sign Up");
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String mAccount = mAccountText.getText().toString().trim();
        String mName = mNameText.getText().toString().trim();
        byte[] photoByte = PhotoUtil.getBytes(mUserPhoto);
        int mGender = 0;
        User user = new User(mAccount, mName, mGender, photoByte);

        new AsyncTask<User, Void, Integer>() {
            @Override
            protected Integer doInBackground(User... users) {
                try {
                    mIsReceived = false;
                    mNetService.setupConnection();
                    if (!mNetService.isConnected()) {
                        return 0;
                    } else {
                        UserAction.register(users[0]);

                        // 如果没收到的话就会一直阻塞;
                        while (!mIsReceived) {
                            // TODO add standby time
                        }
                        mNetService.closeConnection();
                        if (mReceivedInfo.getResult() == Result.REGISTER_SUCCESS)
                            return 1;
                        else
                            return 2;
                    }
                } catch (Exception e) {
                    Log.d("register", "注册异常");

                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                if (result == 0) {
                    Toast.makeText(RegistrationActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                } else {
                    if (result == 1) {
                        Toast.makeText(RegistrationActivity.this, "Successful registration", Toast.LENGTH_SHORT).show();
                        ActivityCollector.finishActivity(RegistrationActivity.this);
                    } else if (result == 2) {
                        Toast.makeText(RegistrationActivity.this, "Successful failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute(user);
    }

    public static void setRegisterInfo(TranObject object, boolean isReceived) {
        mReceivedInfo = object;
        mIsReceived = true;
    }
}
