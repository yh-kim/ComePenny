package com.enterpaper.comepenny.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.enterpaper.comepenny.R;
import com.enterpaper.comepenny.activities.MyinfoLikeFragment;
import com.enterpaper.comepenny.activities.MyinfoWriteFragment;
import com.enterpaper.comepenny.tab.t1idea.IdeaFragment;
import com.enterpaper.comepenny.tab.t2booth.BoothFragment;

/**
 * Created by Kim on 2015-07-21.
 */
public class ComePennyMyinfoFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{
//    private final String[] TITLES = {"Today's Homo", "Category" };
    private int switch_Icons[] = {R.drawable.selector_tab_homo, R.drawable.selector_tab_category};
    Fragment frag =null;
    final int PAGE_COUNT = 2;


    public ComePennyMyinfoFragmentPagerAdapter(FragmentManager fm){
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
                return MyinfoWriteFragment.newInstance();
            case 1:
                return MyinfoLikeFragment.newInstance();


        }
        return null;
    }

    @Override
    public int getPageIconResId(int position) {
//        return tabIcons[position];

        return switch_Icons[position];
    }

    /*
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
    */
}