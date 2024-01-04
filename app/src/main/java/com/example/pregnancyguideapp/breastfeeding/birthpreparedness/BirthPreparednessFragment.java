package com.example.pregnancyguideapp.breastfeeding.birthpreparedness;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.R;
import com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan.BirthPlanFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class BirthPreparednessFragment extends Fragment {

    StorageReference StoragePregnancyRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("BirthPreparedness");
    Bitmap[] Bitmap_LandBP = new Bitmap[5];    // Array of bitmap values for Labor and Birth Preparation images.
    Bitmap[] Bitmap_SoL = new Bitmap[6];    // Array of bitmap values for Stages of Labor images.
    Bitmap bitmap_PLS1, bitmap_PLS2, bitmap_ELS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.birth_preparedness);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_birth_preparedness);

        return inflater.inflate(R.layout.fragment_birth_preparedness, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open up BirthPlan fragment upon click event.
        view.findViewById(R.id.btnMyBirthPlan).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BirthPlanFragment(), "BirthPlan_Frag").addToBackStack(null).commit());

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // ---------------------------- Labor and Birth Preparation --------------------------------

        // Toggle visibility for Labor and Birth Preparation Layout.
        LinearLayout LandBPLayout = getActivity().findViewById(R.id.layout_LaborAndBirthPrep);
        TextView txtViewLandBP = view.findViewById(R.id.txtLaborAndBirthPrep);
        toggleLayout(LandBPLayout, txtViewLandBP);

        // Get references to the ImageViews inside the Labor and Birth Preparation layout.
        ImageView[] imageViewsLandBP = {
                getActivity().findViewById(R.id.imageViewLaborAndBirthPrep1),
                getActivity().findViewById(R.id.imageViewLaborAndBirthPrep2),
                getActivity().findViewById(R.id.imageViewLaborAndBirthPrep3),
                getActivity().findViewById(R.id.imageViewLaborAndBirthPrep4),
                getActivity().findViewById(R.id.imageViewLaborAndBirthPrep5)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsLandBP.length; i++) {
            String imageName = "LandBP" + (i+1) + ".png";
            StorageReference imageRef = StoragePregnancyRef.child("LaborAndBirthPreparation").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsLandBP[i], bitmap -> Bitmap_LandBP[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsLandBP[i].setOnClickListener(v -> expandImage(Bitmap_LandBP[idx]));
        }


        // ------------------------------------- Pre-Labor Signs -----------------------------------

        // Toggle visibility for Pre-Labor Signs Layout.
        LinearLayout PreLaborSignsLayout = getActivity().findViewById(R.id.layout_PreLaborSigns);
        TextView txtViewPLS = view.findViewById(R.id.txtPreLaborSigns);
        toggleLayout(PreLaborSignsLayout, txtViewPLS);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_PLS1 = StoragePregnancyRef.child("PreLaborSigns").child("PreLaborSigns1.png");
        StorageReference imageRef_PLS2 = StoragePregnancyRef.child("PreLaborSigns").child("PreLaborSigns2.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewPLS1 = getActivity().findViewById(R.id.imageViewPreLaborSigns1);
        ImageView imgViewPLS2 = getActivity().findViewById(R.id.imageViewPreLaborSigns2);

        // Cache handling for the image.
        File imageFile_PLS1 = new File(cacheDir, "PreLaborSigns1.png");
        File imageFile_PLS2 = new File(cacheDir, "PreLaborSigns2.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PLS1, imageRef_PLS1, imgViewPLS1, bitmap -> bitmap_PLS1 = bitmap);
        loadImage(imageFile_PLS2, imageRef_PLS2, imgViewPLS2, bitmap -> bitmap_PLS2 = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewPreLaborSigns1).setOnClickListener(v -> expandImage(bitmap_PLS1));
        view.findViewById(R.id.imageViewPreLaborSigns2).setOnClickListener(v -> expandImage(bitmap_PLS2));

        // ----------------------------------- Early Labor Signs -----------------------------------

        // Toggle visibility for Early Labor Signs Layout.
        LinearLayout EarlyLaborSignsLayout = getActivity().findViewById(R.id.layout_EarlyLaborSigns);
        TextView txtViewELS = view.findViewById(R.id.txtEarlyLaborSigns);
        toggleLayout(EarlyLaborSignsLayout, txtViewELS);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_ELS = StoragePregnancyRef.child("EarlyLaborSigns").child("EarlyLaborSigns.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewEarlyLaborSigns = getActivity().findViewById(R.id.imageViewEarlyLaborSigns);

        // Cache handling for the image.
        File imageFile_ELS = new File(cacheDir, "EarlyLaborSigns.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_ELS, imageRef_ELS, imgViewEarlyLaborSigns, bitmap -> bitmap_ELS = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewEarlyLaborSigns).setOnClickListener(v -> expandImage(bitmap_ELS));

        // ---------------------------- Stages of Labor Preparation --------------------------------

        // Toggle visibility for Stages of Labor Layout.
        LinearLayout StagesOfLaborLayout = getActivity().findViewById(R.id.layout_StagesOfLabor);
        TextView txtViewSoL = view.findViewById(R.id.txtStagesOfLabor);
        toggleLayout(StagesOfLaborLayout, txtViewSoL);

        // Get references to the ImageViews inside the Stages of Labor layout.
        ImageView[] imageViewsSoL = {
                getActivity().findViewById(R.id.imageViewStagesOfLabor1),
                getActivity().findViewById(R.id.imageViewStagesOfLabor2),
                getActivity().findViewById(R.id.imageViewStagesOfLabor3),
                getActivity().findViewById(R.id.imageViewStagesOfLabor4),
                getActivity().findViewById(R.id.imageViewStagesOfLabor5),
                getActivity().findViewById(R.id.imageViewStagesOfLabor6)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsSoL.length; i++) {
            String imageName = "SoL" + (i+1) + ".png";
            StorageReference imageRef = StoragePregnancyRef.child("StagesOfLabor").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsSoL[i], bitmap -> Bitmap_SoL[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsSoL[i].setOnClickListener(v -> expandImage(Bitmap_SoL[idx]));
        }

    }

    // Method to show/hide a layout.
    private void toggleLayout(LinearLayout layout, TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isVisible = layout.getVisibility();
                if (isVisible == View.VISIBLE) {
                    layout.setVisibility(View.GONE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Callback interface for notifying when a bitmap has been loaded.
    private interface BitmapLoadCallback {
        // Called when the bitmap has been loaded.
        void onBitmapLoaded(Bitmap bitmap);
    }

    // Method to handle the cache and download the image from Firebase Storage. Then load those images into the imageViews.
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, BirthPreparednessFragment.BitmapLoadCallback callback) {

        if (imageFile.exists()) {
            // Image already exists in the cache directory, load it
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            callback.onBitmapLoaded(bitmap);
        } else {
            // Image does not exist in the cache directory, download it and save to cache
            imageRef.getFile(imageFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // After the image has been downloaded successfully, load it from the cache.
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                        callback.onBitmapLoaded(bitmap);
                    });
        }
    }

    // Method to expand an image on a PhotoView.
    private void expandImage(Bitmap bitmap) {
        // Build an AlertDialog box to inflate the child layout.
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        // Inflate the child layout to be shown on that AlertDialog box.
        View mView = getLayoutInflater().inflate(R.layout.zoom_layout, null);
        // Set the bitmap on the PhotoView.
        PhotoView photoView = mView.findViewById(R.id.imgPhotoView);
        photoView.setImageBitmap(bitmap);
        // Set the view on the AlertDialog box.
        mBuilder.setView(mView);
        // Create and show the AlertDialog box.
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

}
