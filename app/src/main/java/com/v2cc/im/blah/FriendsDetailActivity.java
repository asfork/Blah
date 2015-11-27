package com.v2cc.im.blah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.v2cc.im.blah.bean.User;
import com.v2cc.im.blah.managers.ActivityCollector;

/**
 * Created by steve on 11/4/15.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsDetailActivity extends BaseActivity implements View.OnClickListener {
    private CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar mToolbar;
    private ImageView mImageView;
    private TextView accountTextView;
    private TextView infoTextView;
    private FloatingActionButton mFab;

    private User friend;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FriendsDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_friends_detail);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mImageView = (ImageView) findViewById(R.id.backdrop);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        accountTextView = (TextView) findViewById(R.id.tv_phone);
        infoTextView = (TextView) findViewById(R.id.tv_intro);
    }

    @Override
    public void initEvents() {
        friend = (User) getIntent().getSerializableExtra("friend");
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar.setTitle(friend.getUserName());
        accountTextView.setText(friend.getAccount());
        infoTextView.setText("What's on your mind?");

        // 设置FloatingActionButton的点击事件
        mFab.setOnClickListener(this);

        Glide.with(this).load(friend.getPhoto()).centerCrop().into(mImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:// 发送按钮
                Bundle bundle = getIntent().getExtras();
                bundle.putString("friendName", friend.getUserName());
                bundle.putInt("friendId", friend.getId());
                ChatActivity.actionStart(this, bundle);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(getClass().getSimpleName(), "click home");
                ActivityCollector.finishActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
