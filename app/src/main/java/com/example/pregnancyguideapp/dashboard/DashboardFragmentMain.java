package com.example.pregnancyguideapp.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pregnancyguideapp.R;

import me.relex.circleindicator.CircleIndicator3;


public class DashboardFragmentMain extends Fragment {

    private static final int NUM_Frags = 2; // Number of fragments inside viewpager.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText("Dashboard");

        // ViewPage2 to display the menu icons.
        ViewPager2 viewPager2 = getActivity().findViewById(R.id.viewpager_dashboard);
        FragmentStateAdapter pagerAdapter = new ScreeSlidePageAdapter(getActivity());
        viewPager2.setAdapter(pagerAdapter);

        // CircleIndicator to be used as a page indicator for ViewPager2.
        CircleIndicator3 indicator = view.findViewById(R.id.circleIndicator3);
        indicator.setViewPager(viewPager2);

    }

    private static class ScreeSlidePageAdapter extends FragmentStateAdapter {
        public ScreeSlidePageAdapter(FragmentActivity activity) {
            super(activity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DashboardFragmentOne();
                case 1:
                    return new DashboardFragmentTwo();
                default:
                    return null;
            }
        }
        @Override
        public int getItemCount() {
            return NUM_Frags;
        }
    }
}
