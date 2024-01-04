package com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.utility.DatePickerHelper;
import com.example.pregnancyguideapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BirthPlanFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");

    //Labor
    LinearLayout LaborPrefOthersLayout, PainManagementOthersLayout;
    RadioGroup LaborPreferencesRG, PainManagementRG;
    private String txtLaborPref, txtPainMgmt, txtReqComfortMeasures;

    //Delivery
    LinearLayout DeliveryPrefOthersLayout, EpisiotomyOthersLayout, CesareanOthersLayout;
    RadioGroup DeliveryPreferencesRG, EpisiotomyRG, CesareanRG;
    private String txtDeliveryPref, txtEpisiotomy, txtCesarean, txtBirthPartner, txtDeliveryOtherPrefExtra;

    //After Delivery
    LinearLayout keepBabyPrefOthersLayout;
    RadioGroup keepBabyPrefRG;
    private String txtHB, txtDelayNBProcedures, txtMedExplanation, txtKeepBabyPref, txtFeedBabyPref, txtADOtherRequests;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.birth_preparedness);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_birth_preparedness);

        return inflater.inflate(R.layout.fragment_birth_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the current user's phone number from Shared Preferences and store it in a variable phoneN. This variable will be used later to extract all the other user details from the database.
        SharedPreferences sp = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String phoneN = sp.getString("User_PhoneN", "");

        // Get database reference of Birth Preparedness from the database under the user's phone number.
        DatabaseReference BirthPrepRef = databaseReference.child("Users").child(phoneN).child("BirthPreparedness");

        // Resizing of the TextView upon a click event for 'Birth Plan overview'.
        enlargeTextView(getActivity().findViewById(R.id.textView_BirthPlan_overview));

    //---------------------------------Background Details-------------------------------------------

        // Toggle visibility for Background Details.
        LinearLayout backgroundDetailsLayout = getActivity().findViewById(R.id.layout_backgroundDetails);
        TextView txtViewBD = view.findViewById(R.id.textView_backgroundDetails);
        ImageButton Submit_BD = getActivity().findViewById(R.id.imageBtn_BackgroundDetailsSubmit);
        toggleLayout(backgroundDetailsLayout, txtViewBD, Submit_BD);

        EditText name = getActivity().findViewById(R.id.editText_BG_name);
        EditText dueDate = getActivity().findViewById(R.id.editText_BG_dueDate);
        EditText doctor = getActivity().findViewById(R.id.editText_BG_doctor);
        EditText midwife = getActivity().findViewById(R.id.editText_BG_midwife);
        EditText doula = getActivity().findViewById(R.id.editText_BG_doula);
        EditText pediatrician = getActivity().findViewById(R.id.editText_BG_pediatrician);
        EditText hospitalAdd = getActivity().findViewById(R.id.editText_BG_hospital_address);
        EditText birthPartnerDetails = getActivity().findViewById(R.id.editText_BG_birthPartnerDetails);
        EditText Imp_med_issues = getActivity().findViewById(R.id.editText_BG_Imp_med_issues);
        
        // Call DatePickerHelper to display a calendar view to choose a date.
        DatePickerHelper datePicker_dob = new DatePickerHelper(getActivity(), dueDate);
        dueDate.setOnClickListener(datePicker_dob);

        // Gather all the background details data from the realtime database.
        BirthPrepRef.child("BackgroundDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ) {
                    BackgroundDetailsHelper BDData = snapshot.getValue(BackgroundDetailsHelper.class);
                    HashMap<String, String> BDMap = BDData.getBDMap();

                    name.setText(BDMap.get("Name"));
                    dueDate.setText(BDMap.get("dueDate"));
                    doctor.setText(BDMap.get("Doctor"));
                    midwife.setText(BDMap.get("Midwife"));
                    doula.setText(BDMap.get("Doula"));
                    pediatrician.setText(BDMap.get("Pediatrician"));
                    hospitalAdd.setText(BDMap.get("HospitalAddress"));
                    birthPartnerDetails.setText(BDMap.get("BirthPartnerDetails"));
                    Imp_med_issues.setText(BDMap.get("ImportantMedicalIssues"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.imageBtn_BackgroundDetailsSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you happy with your submission?");
                builder.setTitle("Background Details");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {

                    if ( !TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(dueDate.getText()) ){

                        BackgroundDetailsHelper BDHelper = new BackgroundDetailsHelper();

                        BDHelper.updateBDMap("Name", name.getText().toString().trim());
                        BDHelper.updateBDMap("dueDate", dueDate.getText().toString().trim());
                        BDHelper.updateBDMap("Doctor", doctor.getText().toString().trim());
                        BDHelper.updateBDMap("Midwife", midwife.getText().toString().trim());
                        BDHelper.updateBDMap("Doula", doula.getText().toString().trim());
                        BDHelper.updateBDMap("Pediatrician", pediatrician.getText().toString().trim());
                        BDHelper.updateBDMap("HospitalAddress", hospitalAdd.getText().toString().trim());
                        BDHelper.updateBDMap("BirthPartnerDetails", birthPartnerDetails.getText().toString().trim());
                        BDHelper.updateBDMap("ImportantMedicalIssues", Imp_med_issues.getText().toString().trim());

                        BirthPrepRef.child("BackgroundDetails").setValue(BDHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                backgroundDetailsLayout.setVisibility(View.GONE);
                                Submit_BD.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Background Details successfully submitted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(),"Need to fill in the necessary details!", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    //---------------------------------------Labor--------------------------------------------------

        // Toggle visibility for Background Details.
        LinearLayout LaborLayout = getActivity().findViewById(R.id.layout_Labor);
        TextView txtViewLabor = view.findViewById(R.id.textView_Labor);
        ImageButton Submit_Labor = getActivity().findViewById(R.id.imageBtn_LaborSubmit);
        toggleLayout(LaborLayout, txtViewLabor, Submit_Labor);

        // Labor Preferences RadioGroup handling.
        LaborPreferencesRG = getActivity().findViewById(R.id.radioGroup_LaborPref);
        LaborPreferencesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LaborPrefOthersLayout = getActivity().findViewById(R.id.laborPrefLayout_Others);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.LaborPref_4) {
                    LaborPrefOthersLayout.setVisibility(View.VISIBLE);
                    EditText LaborOthers = getActivity().findViewById(R.id.editTextLaborPrefOthers);
                    txtLaborPref = LaborOthers.getText().toString().trim();

                } else {
                    LaborPrefOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtLaborPref = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText LaborOthers = getActivity().findViewById(R.id.editTextLaborPrefOthers);
        LaborOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtLaborPref = s.toString().trim();
            }
        });

        // Labor Pain Management RadioGroup handling.
        PainManagementRG = getActivity().findViewById(R.id.radioGroup_PainManagement);
        PainManagementRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PainManagementOthersLayout = getActivity().findViewById(R.id.painManagementLayout_Others);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.LaborPain_4) {
                    PainManagementOthersLayout.setVisibility(View.VISIBLE);
                    EditText PainMgmtOthers = getActivity().findViewById(R.id.editTextPainManagementOthers);
                    txtPainMgmt = PainMgmtOthers.getText().toString().trim();
                } else {
                    PainManagementOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtPainMgmt = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText PainManagementOthers = getActivity().findViewById(R.id.editTextPainManagementOthers);
        PainManagementOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtPainMgmt = s.toString().trim();
            }
        });

        // Ensures the text value from EditText is taken after the user changes the text.
        EditText ReqComfortMeasures = getActivity().findViewById(R.id.editTextRequestComfortMeasures);
        ReqComfortMeasures.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {txtReqComfortMeasures = s.toString().trim();}
        });

        // Gather all the background details data from the realtime database.
        BirthPrepRef.child("Labor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ) {
                    LaborHelper LaborData = snapshot.getValue(LaborHelper.class);
                    HashMap<String, String> BDMap = LaborData.getLaborMap();

                    // Call the RadioGroupHelper method to sort out the selections of the radio buttons inside each radio groups.
                    LaborData.RadioGroupHelper(getActivity(),BDMap );

                    ReqComfortMeasures.setText(BDMap.get("RequestComfortMeasures"));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.imageBtn_LaborSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Are you happy with your submission?");
                builder.setTitle("Labor");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {

                    // Check if all the required fields for the delivery form is entered before submission.
                    if ( !TextUtils.isEmpty(txtLaborPref) && !TextUtils.isEmpty(txtPainMgmt) ) {

                        LaborHelper LaborHelper = new LaborHelper();

                        LaborHelper.updateLaborMap("LaborPreferences", txtLaborPref);
                        LaborHelper.updateLaborMap("PainManagement", txtPainMgmt);
                        // Checks if the field is empty. This is not as important as the other fields.
                        if (!TextUtils.isEmpty(txtReqComfortMeasures)) {
                            LaborHelper.updateLaborMap("RequestComfortMeasures", txtReqComfortMeasures);
                        }

                        BirthPrepRef.child("Labor").setValue(LaborHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                LaborLayout.setVisibility(View.GONE);
                                Submit_Labor.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Labor form successfully submitted!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }  else {
                        Toast.makeText(getActivity(),"Need all the necessary details!", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    //-------------------------------------Delivery-------------------------------------------------

        // Toggle visibility for Delivery.
        LinearLayout DeliveryLayout = getActivity().findViewById(R.id.layout_Delivery);
        TextView txtViewDelivery = view.findViewById(R.id.textView_Delivery);
        ImageButton Submit_Delivery = getActivity().findViewById(R.id.imageBtn_DeliverySubmit);
        toggleLayout(DeliveryLayout, txtViewDelivery, Submit_Delivery);

        // Delivery Preferences RadioGroup handling.
        DeliveryPreferencesRG = getActivity().findViewById(R.id.radioGroup_DeliveryPref);
        DeliveryPreferencesRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DeliveryPrefOthersLayout = getActivity().findViewById(R.id.deliveryPrefOthers_Layout);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.DeliveryPref_4) {
                    DeliveryPrefOthersLayout.setVisibility(View.VISIBLE);
                    EditText DeliveryOthers = getActivity().findViewById(R.id.editTextDeliveryPrefOthers);
                    txtDeliveryPref = DeliveryOthers.getText().toString().trim();
                } else {
                    DeliveryPrefOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtDeliveryPref = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText DeliveryOthers = getActivity().findViewById(R.id.editTextDeliveryPrefOthers);
        DeliveryOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtDeliveryPref = s.toString().trim();
            }
        });

        // Episiotomy RadioGroup handling.
        EpisiotomyRG = getActivity().findViewById(R.id.radioGroup_Episiotomy);
        EpisiotomyRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                EpisiotomyOthersLayout = getActivity().findViewById(R.id.episiotomyLayout_Others);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.Episiotomy_4) {
                    EpisiotomyOthersLayout.setVisibility(View.VISIBLE);
                    EditText EpisiotomyOthers = getActivity().findViewById(R.id.editTextEpisiotomyOthers);
                    txtEpisiotomy = EpisiotomyOthers.getText().toString().trim();
                } else {
                    EpisiotomyOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtEpisiotomy = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText EpisiotomyOthers = getActivity().findViewById(R.id.editTextEpisiotomyOthers);
        EpisiotomyOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtEpisiotomy = s.toString().trim();
            }
        });

        // Cesarean RadioGroup handling.
        CesareanRG = getActivity().findViewById(R.id.radioGroup_Cesarean);
        CesareanRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                CesareanOthersLayout = getActivity().findViewById(R.id.cesareanLayout_Others);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.Cesarean_4) {
                    CesareanOthersLayout.setVisibility(View.VISIBLE);
                    EditText CesareanOthers = getActivity().findViewById(R.id.editTextCesareanOthers);
                    txtCesarean = CesareanOthers.getText().toString().trim();
                } else {
                    CesareanOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtCesarean = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText CesareanOthers = getActivity().findViewById(R.id.editTextCesareanOthers);
        CesareanOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtCesarean = s.toString().trim();
            }
        });


        // Ensures the text value from EditText is taken after the user changes the text.
        EditText DeliveryOtherPrefExtra = getActivity().findViewById(R.id.editTextDeliveryOtherPrefExtra);
        DeliveryOtherPrefExtra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtDeliveryOtherPrefExtra = s.toString().trim();
            }
        });

        RadioGroup BirthPartnerRG = getActivity().findViewById(R.id.radioGroup_BirthPartner);

        // Gather all the Delivery form's data from the realtime database.
        BirthPrepRef.child("Delivery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ) {
                    DeliveryHelper DeliveryData = snapshot.getValue(DeliveryHelper.class);
                    HashMap<String, String> DeliveryMap = DeliveryData.getDeliveryMap();

                    // Call the RadioGroupHelper method to sort out the selections of the radio buttons inside each radio groups.
                    DeliveryData.RadioGroupHelper(getActivity(),DeliveryMap );

                    DeliveryOtherPrefExtra.setText(DeliveryMap.get("DeliveryOtherPreferences"));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        view.findViewById(R.id.imageBtn_DeliverySubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Are you happy with your submission?");
                builder.setTitle("Delivery");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {

                    // Check if any of the button is selected inside the radio group Birth Partner.
                    int stateBirthPartnerRG = BirthPartnerRG.getCheckedRadioButtonId();
                    if (stateBirthPartnerRG != -1) {
                        // Gets the value of the selected radiobutton from the radioGroup Birth Partner.
                        RadioButton selectedRBPartner  = getActivity().findViewById(BirthPartnerRG.getCheckedRadioButtonId());
                        txtBirthPartner = selectedRBPartner.getText().toString();
                    }


                    // Check if all the required fields for the delivery form is entered before submission.
                    if ( ( !TextUtils.isEmpty(txtDeliveryPref) && !TextUtils.isEmpty(txtBirthPartner) && !TextUtils.isEmpty(txtEpisiotomy) && !TextUtils.isEmpty(txtCesarean) ) ){

                        DeliveryHelper DeliveryHelper = new DeliveryHelper();
                        DeliveryHelper.updateDeliveryMap("DeliveryPreferences", txtDeliveryPref);
                        DeliveryHelper.updateDeliveryMap("BirthPartner", txtBirthPartner);
                        DeliveryHelper.updateDeliveryMap("Episiotomy", txtEpisiotomy);
                        DeliveryHelper.updateDeliveryMap("Cesarean", txtCesarean);

                        // Checks if the field is empty. This is not as important as the other fields.
                        if (!TextUtils.isEmpty(txtDeliveryOtherPrefExtra)) {
                            DeliveryHelper.updateDeliveryMap("DeliveryOtherPreferences", txtDeliveryOtherPrefExtra);
                        }

                        BirthPrepRef.child("Delivery").setValue(DeliveryHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DeliveryLayout.setVisibility(View.GONE);
                                Submit_Delivery.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Delivery form successfully submitted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(),"Need all the necessary details!", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    //---------------------------------After Delivery-----------------------------------------------

        // Toggle visibility for After Delivery.
        LinearLayout AfterDeliveryLayout = getActivity().findViewById(R.id.layout_AfterDelivery);
        TextView txtViewAD = view.findViewById(R.id.textView_AfterDelivery);
        ImageButton Submit_AD = getActivity().findViewById(R.id.imageBtn_AfterDeliverySubmit);
        toggleLayout(AfterDeliveryLayout, txtViewAD, Submit_AD);

        // Keeping baby preferences RadioGroup handling.
        keepBabyPrefRG = getActivity().findViewById(R.id.radioGroup_keepBabyPref);
        keepBabyPrefRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                keepBabyPrefOthersLayout = getActivity().findViewById(R.id.keepBabyPrefLayout_Others);
                RadioButton checkedRB = getActivity().findViewById(checkedId);
                if (checkedId == R.id.keepBabyPref_4) {
                    keepBabyPrefOthersLayout.setVisibility(View.VISIBLE);
                    EditText KeepBabyPrefOthers = getActivity().findViewById(R.id.editTextKeepBabyPrefOthers);
                    txtKeepBabyPref = KeepBabyPrefOthers.getText().toString().trim();
                } else {
                    keepBabyPrefOthersLayout.setVisibility(View.GONE);
                    //Get the text of the radio button and set it to a string.
                    txtKeepBabyPref = checkedRB.getText().toString();
                }
            }
        });
        // Ensures the text value from EditText is taken after the user changes the text.
        EditText KeepBabyPrefOthers = getActivity().findViewById(R.id.editTextKeepBabyPrefOthers);
        KeepBabyPrefOthers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtKeepBabyPref = s.toString().trim();
            }
        });

        RadioGroup HoldBabyRG = getActivity().findViewById(R.id.radioGroup_holdBaby);
        RadioGroup DelayNBProceduresRG = getActivity().findViewById(R.id.radioGroup_delayNewBornProcedures);
        RadioGroup MedExplanationRG = getActivity().findViewById(R.id.radioGroup_AD_med_explanation);
        RadioGroup FeedBabyPrefRG = getActivity().findViewById(R.id.radioGroup_feedBabyPref);

        // Ensures the text value from EditText is taken after the user changes the text.
        EditText ADOtherRequests = getActivity().findViewById(R.id.editTextADOtherRequest);
        ADOtherRequests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                txtADOtherRequests = s.toString().trim();
            }
        });

        // Gather all the Delivery form's data from the realtime database.
        BirthPrepRef.child("AfterDelivery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ( snapshot.exists() ) {
                    AfterDeliveryHelper AfterDeliveryData = snapshot.getValue(AfterDeliveryHelper.class);
                    HashMap<String, String> ADMap = AfterDeliveryData.getAfterDeliveryMap();

                    // Call the RadioGroupHelper method to sort out the selections of the radio buttons inside each radio groups.
                    AfterDeliveryData.RadioGroupHelper(getActivity(),ADMap );

                    ADOtherRequests.setText(ADMap.get("AfterDeliveryOtherRequests"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.imageBtn_AfterDeliverySubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Are you happy with your submission?");
                builder.setTitle("After Delivery");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {

                    // Checks if a radio button is selected inside the radio groups

                    int stateHoldBabyRG = HoldBabyRG.getCheckedRadioButtonId();
                    if (stateHoldBabyRG != -1) {
                        // Gets the value of the selected radiobutton from the radioGroup Hold baby.
                        RadioButton selectedRBHoldBaby = getActivity().findViewById(HoldBabyRG.getCheckedRadioButtonId());
                        txtHB = selectedRBHoldBaby.getText().toString();
                    }

                    int stateDelayProceduresRG = DelayNBProceduresRG.getCheckedRadioButtonId();
                    if (stateDelayProceduresRG != -1) {
                        // Gets the value of the selected radiobutton from the radioGroup Delay New Born Procedures.
                        RadioButton selectedRBDelayProcedures = getActivity().findViewById(DelayNBProceduresRG.getCheckedRadioButtonId());
                        txtDelayNBProcedures = selectedRBDelayProcedures.getText().toString();
                    }

                    int stateMedExplanationRG = MedExplanationRG.getCheckedRadioButtonId();
                    if (stateMedExplanationRG != -1) {
                        // Gets the value of the selected radiobutton from the radioGroup Medication Explanation.
                        RadioButton selectedRBMedExplanation = getActivity().findViewById(MedExplanationRG.getCheckedRadioButtonId());
                        txtMedExplanation = selectedRBMedExplanation.getText().toString();
                    }

                    int stateFeedBabyPrefRG = FeedBabyPrefRG.getCheckedRadioButtonId();
                    if (stateFeedBabyPrefRG != -1) {
                        // Gets the value of the selected radiobutton from the radioGroup Baby feeding preferences.
                        RadioButton selectedRBFeedBaby = getActivity().findViewById(FeedBabyPrefRG.getCheckedRadioButtonId());
                        txtFeedBabyPref = selectedRBFeedBaby.getText().toString();
                    }

                    // Check if all the required fields for the delivery form is entered before submission.
                    if ( ( !TextUtils.isEmpty(txtHB) && !TextUtils.isEmpty(txtDelayNBProcedures) && !TextUtils.isEmpty(txtMedExplanation) && !TextUtils.isEmpty(txtKeepBabyPref) && !TextUtils.isEmpty(txtFeedBabyPref) ) ) {


                        AfterDeliveryHelper ADHelper = new AfterDeliveryHelper();

                        ADHelper.updateAfterDeliveryMap("HoldBaby", txtHB);
                        ADHelper.updateAfterDeliveryMap("delayNewBornProcedures", txtDelayNBProcedures);
                        ADHelper.updateAfterDeliveryMap("MedicalExplanation", txtMedExplanation);
                        ADHelper.updateAfterDeliveryMap("KeepBabyPreferences", txtKeepBabyPref);
                        ADHelper.updateAfterDeliveryMap("FeedBabyPreferences", txtFeedBabyPref);
                        // Checks if the field is empty. This is not as important as the other fields.
                        if (!TextUtils.isEmpty(txtADOtherRequests)) {
                            ADHelper.updateAfterDeliveryMap("AfterDeliveryOtherRequests", txtADOtherRequests);
                        }
                        BirthPrepRef.child("AfterDelivery").setValue(ADHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                AfterDeliveryLayout.setVisibility(View.GONE);
                                Submit_AD.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "After Delivery form successfully submitted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(),"Need all the necessary details!", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

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

    // Method to show/hide a Layout and an ImageButton.
    private void toggleLayout(LinearLayout layout, TextView textView, ImageButton imageBtn) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isVisible = layout.getVisibility();
                if (isVisible == View.VISIBLE) {
                    layout.setVisibility(View.GONE);
                    imageBtn.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    imageBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
