package com.example.sporttracker.Views.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();

    private final List<String> fragmentTitleList = new ArrayList<>();

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
