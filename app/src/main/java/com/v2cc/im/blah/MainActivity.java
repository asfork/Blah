package com.v2cc.im.blah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.v2cc.im.blah.fragments.FragmentTest;
import com.v2cc.im.blah.fragments.FriendsFragment;
import com.v2cc.im.blah.global.App;
import com.v2cc.im.blah.managers.ActivityCollector;
import com.v2cc.im.blah.network.NetService;
import com.v2cc.im.blah.views.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/22.
 * If it works, I created this. If not, I didn't.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    //初始化各种控件，照着xml中的顺序写
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavigationView mNavigationView;
    private FloatingActionButton mFab;

    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> fragments;
    // ViewPager的数据适配器
    ViewPagerAdapter mAdapter;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        configView();
    }

    @Override
    public void initEvents() {
        // 判断是否是来自点击通知的跳转
        Bundle bundle = getIntent().getBundleExtra(App.EXTRA_BUNDLE);
        if (bundle != null) {
            ChatActivity.actionStart(this, bundle);
            Log.d("MainActivity", "launchParam exists, redirect to ChatActivity");
        }
    }

    private void configView() {
        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);
        //初始化填充到ViewPager中的Fragment集合
        fragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            switch (i) {
                case 0:
                    FragmentTest fragmentTest = new FragmentTest();
                    fragmentTest.setArguments(mBundle);
                    fragments.add(i, fragmentTest);
                    break;
                case 1:
                    FriendsFragment friendsFragment = new FriendsFragment();
                    friendsFragment.setArguments(mBundle);
                    fragments.add(i, friendsFragment);
                    break;
                default:
                    FragmentTest mFragment = new FragmentTest();
                    mFragment.setArguments(mBundle);
                    fragments.add(i, mFragment);
                    break;
            }
        }

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 透明状态栏
//        StatusBarCompat.compat(this);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/drawer_nav_header"来设置
        mNavigationView.inflateHeaderView(R.layout.drawer_nav_header);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/drawer_nav"来设置
        mNavigationView.inflateMenu(R.menu.drawer_nav);

        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavigationViewMenuItemSelected(mNavigationView);

        // 初始化ViewPager的适配器，并设置给它
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, fragments);
        mViewPager.setAdapter(mAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(5);

        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        // 设置FloatingActionButton的点击事件
        mFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
//                SnackbarUtil.show(v, getString(R.string.plusone), 0);
                break;
        }
    }

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav Navigation
     */
    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String msgString;

                // TODO add navigation menu functions
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        msgString = (String) menuItem.getTitle();
                        Log.d("MainActivity", msgString);
                        break;
                    case R.id.nav_discover:
                        msgString = (String) menuItem.getTitle();
                        Log.d("MainActivity", msgString);
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit:
                NetService.getInstance().closeConnection();
                ActivityCollector.finishActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
