package com.example.myapp_badminton.megha;
/*
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
*/

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by megha on 12/7/19.
 */

public class PagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SubmitShotLocation tab1 = new SubmitShotLocation();
                return tab1;
            case 1:
                SubmitTypeOfShot tab2 = new SubmitTypeOfShot();
                return tab2;

        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
