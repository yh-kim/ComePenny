package com.enterpaper.comepenny.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.enterpaper.comepenny.tab.t2booth.BoothFragment;
import com.enterpaper.comepenny.tab.t1idea.IdeaFragment;

/**
 * Created by Kim on 2015-07-21.
 */
public class ComePennyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Idea", "Booth" };
    Fragment frag =null;
    final int PAGE_COUNT = 2;


    public ComePennyFragmentPagerAdapter(FragmentManager fm){
        super(fm);

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return IdeaFragment.newInstance();
            case 1:
                return BoothFragment.newInstance();


        }
        return null;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}