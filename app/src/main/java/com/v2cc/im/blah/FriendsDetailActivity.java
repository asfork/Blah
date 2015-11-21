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
import com.v2cc.im.blah.models.User;
import com.v2cc.im.blah.utils.ActivityCollector;

/**
 * Created by steve on 11/4/15.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsDetailActivity extends BaseActivity implements View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView phoneTextView;
    private TextView infoTextView;
    private FloatingActionButton fab;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FriendsDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initView(){
        setContentView(R.layout.activity_friends_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));

        imageView = (ImageView) findViewById(R.id.backdrop);

        phoneTextView = (TextView) findViewById(R.id.tv_phone);
        phoneTextView.setText(getIntent().getStringExtra("phone"));

        infoTextView = (TextView) findViewById(R.id.tv_intro);
        infoTextView.setText("What's on your mind?");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        // 设置FloatingActionButton的点击事件
        fab.setOnClickListener(this);

        // 载入头像
        imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(User.getRandomPhotosDrawable()).centerCrop().into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:// 发送按钮
                Bundle bundle = getIntent().getExtras();
                // start up MessageActivity
                MessageActivity.actionStart(this, bundle);
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
