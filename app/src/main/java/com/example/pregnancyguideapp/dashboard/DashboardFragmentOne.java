package com.example.pregnancyguideapp.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.breastfeeding.birthpreparedness.BirthPreparednessFragment;
import com.example.pregnancyguideapp.nutrition.NutritionFragment;
import com.example.pregnancyguideapp.postnatalcare.PostnatalCareFragment;
import com.example.pregnancyguideapp.pregnancy.PregnancyFragment;
import com.example.pregnancyguideapp.R;

public class DashboardFragmentOne extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open up Pregnancy Fragment
        view.findViewById(R.id.imgViewPregnancy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PregnancyFragment(), "Pregnancy_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Nutrition Fragment
        view.findViewById(R.id.imgViewNutrition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NutritionFragment(), "Nutrition_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Birth Preparedness Fragment
        view.findViewById(R.id.imgViewBirthPreparedness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BirthPreparednessFragment(), "BirthPreparedness_Frag").addToBackStack(null).commit();
            }
        });
        // Open up Postnatal Care Fragment
        view.findViewById(R.id.imgViewPostnatalCare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PostnatalCareFragment(), "PostnatalCare_Frag").addToBackStack(null).commit();
            }
        });
    }

}
