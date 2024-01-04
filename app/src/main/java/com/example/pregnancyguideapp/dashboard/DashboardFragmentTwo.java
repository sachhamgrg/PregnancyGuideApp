package com.example.pregnancyguideapp.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.accountdetails.AccountDetailsFragment;
import com.example.pregnancyguideapp.breastfeeding.BreastfeedingMainFragment;
import com.example.pregnancyguideapp.familyplanning.FamilyPlanningFragment;
import com.example.pregnancyguideapp.maleinvolvement.MaleInvolvementFragment;
import com.example.pregnancyguideapp.R;

public class DashboardFragmentTwo extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open up Breastfeeding Fragment
        view.findViewById(R.id.imgViewBreastfeeding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BreastfeedingMainFragment(), "BreastfeedingMain_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Family Planning Fragment
        view.findViewById(R.id.imgViewFamilyPlanning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FamilyPlanningFragment(), "FamilyPlanning_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Male Involvement Fragment
        view.findViewById(R.id.imgViewMaleInvolvement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MaleInvolvementFragment(), "MaleInvolvement_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Account Details Fragment
        view.findViewById(R.id.imgViewAccountDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountDetailsFragment(), "AccountDetails_Frag").addToBackStack(null).commit();
            }
        });
    }

}
