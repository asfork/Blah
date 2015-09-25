package com.v2cc.im.blah;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/22.
 * If it works, I created this. If not, I didn't.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mFragments) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChatListFragment.newInstance(position);
            case 1:
                return FriendsListFragment.newInstance(position);
            default:
                return FragmentTest.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
