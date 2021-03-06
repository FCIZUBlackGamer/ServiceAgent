package com.thetatechno.serviceagent.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.thetatechno.serviceagent.model.pojo.CurrentLocation;
import com.thetatechno.serviceagent.ui.home.HomeAgentFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    Map<Integer, Fragment> fragmentsListMap = new HashMap<>();
   List<CurrentLocation> mCurrentLocationList;
   private FragmentManager fragmentManager;
    public ViewPagerAdapter(@NonNull FragmentActivity fm, List<CurrentLocation> mCurrentLocationList) {
        super(fm);
        this.mCurrentLocationList = mCurrentLocationList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = HomeAgentFragment.newInstance(mCurrentLocationList.get(position).getSessionId());
        fragmentsListMap.put(position,fragment);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return mCurrentLocationList.size();
    }
    public Map<Integer, Fragment> getFragmentsList(){
        return fragmentsListMap;
    }


}
