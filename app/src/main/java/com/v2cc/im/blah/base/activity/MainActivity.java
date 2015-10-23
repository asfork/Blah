package com.v2cc.im.blah.base.activity;

import android.os.Bundle;
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

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.adapter.ViewPagerAdapter;
import com.v2cc.im.blah.base.app.Constants;
import com.v2cc.im.blah.base.fragment.FragmentTest;
import com.v2cc.im.blah.base.view.StatusBarCompat;
import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.friends.FriendsFragment;
import com.v2cc.im.blah.message.MessageActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/22.
 * If it works, I created this. If not, I didn't.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    //初始化各种控件，照着xml中的顺序写
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    // TabLayout中的tab标题
    private String[] titles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> fragments;
    // ViewPager的数据适配器
    ViewPagerAdapter adapter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
        if (bundle != null) {
//            SystemUtil.startMessageActivity(this, phoneNum);
            MessageActivity.actionStart(this, bundle);
            Log.d("MainActivity", "launchParam exists, redirect to MessageActivity");
        }
    }

    @Override
    public void initData() {
        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        titles = getResources().getStringArray(R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
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
    }

    @Override
    public void configViews() {
        // 设置显示Toolbar
        setSupportActionBar(toolbar);
        // 透明状态栏
        StatusBarCompat.compat(this);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/drawer_header"来设置
        navigationView.inflateHeaderView(R.layout.drawer_header);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/drawer_nav"来设置
        navigationView.inflateMenu(R.menu.drawer_nav);

        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavigationViewMenuItemSelected(navigationView);

        // 初始化ViewPager的适配器，并设置给它
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数
        viewPager.setOffscreenPageLimit(5);

        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        viewPager.addOnPageChangeListener(this);

        tabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        tabLayout.setupWithViewPager(viewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        tabLayout.setTabsFromPagerAdapter(adapter);

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
                String msgString = "";

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
                drawerLayout.closeDrawers();

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
            case R.id.action_settings:
                Log.d("MainActivity", "Settings");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle(titles[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 打开数据库
        DataBaseHelperUtil.getInstance(this).openDataBase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭数据库
        DataBaseHelperUtil.getInstance(this).closeDataBase();
    }
}
