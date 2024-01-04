package com.example.pregnancyguideapp.familyplanning.medicaloptions;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MedicalOptionsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.family_planning);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_family_planning);

        return inflater.inflate(R.layout.fragment_fp_medical_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resizing of the TextView upon a click event for 'Birth Plan overview'.
        enlargeTextView(getActivity().findViewById(R.id.textView_MO_overview));

        // Open up Barrier Methods fragment upon click event.
        view.findViewById(R.id.txtBarrierMethods).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MOBarrierMethodsFragment(), "MOBarrierMethods_Frag").addToBackStack(null).commit());
        // Open up Hormonal fragment upon click event.
        view.findViewById(R.id.txtHormonal).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MOHormonalFragment(), "MOHormonal_Frag").addToBackStack(null).commit());
        // Open up MSterilization fragment upon click event.
        view.findViewById(R.id.txtSterilization).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MOSterilizationFragment(), "MOSterilization_Frag").addToBackStack(null).commit());

    }

    // Method to enlarge TextView
    private void enlargeTextView(TextView textView) {
        // Get the current text size
        final float normalSize = textView.getTextSize();
        // Click Listener to handle the resizing of the TextView upon click events.
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getTextSize() == normalSize) {
                    // Enlarge the text size of the TextView
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                } else {
                    // Set the text size back to its original value
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalSize);
                }
            }
        });
    }

}
