package com.uniben.attsys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by ILENWABOR DAVID on 30/05/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public ViewPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new AttendanceFragment();
            case 1: return new CoursesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
