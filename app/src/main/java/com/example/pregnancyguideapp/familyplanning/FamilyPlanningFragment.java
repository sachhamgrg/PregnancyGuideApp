package com.example.pregnancyguideapp.familyplanning;

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
import com.example.pregnancyguideapp.familyplanning.medicaloptions.MedicalOptionsFragment;
import com.example.pregnancyguideapp.familyplanning.naturaloptions.NaturalOptionsFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FamilyPlanningFragment extends Fragment {

    StorageReference StorageFamilyPlanningRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("FamilyPlanning");
    Bitmap bitmap_EC1, bitmap_EC2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.family_planning);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_family_planning);

        return inflater.inflate(R.layout.fragment_family_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open up Medical Options fragment upon click event.
        view.findViewById(R.id.txtMedicalOptions).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicalOptionsFragment(), "MedicalOptions_Frag").addToBackStack(null).commit());
        // Open up Natural Options fragment upon click event.
        view.findViewById(R.id.txtNaturalOptions).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NaturalOptionsFragment(), "NaturalOptions_Frag").addToBackStack(null).commit());


        // ----------------------------------- EmergencyContraception ------------------------------

        // Toggle visibility for Emergency Contraception Layout.
        LinearLayout EmergencyContraceptionLayout = getActivity().findViewById(R.id.layout_EmergencyContraception);
        TextView txtViewEC = view.findViewById(R.id.txtEmergencyContraception);
        toggleLayout(EmergencyContraceptionLayout, txtViewEC);

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_EC1 = StorageFamilyPlanningRef.child("EmergencyContraception").child("EC1.png");
        StorageReference imageRef_EC2 = StorageFamilyPlanningRef.child("EmergencyContraception").child("EC2.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewEC1 = getActivity().findViewById(R.id.imageViewEmergencyContraception1);
        ImageView imgViewEC2 = getActivity().findViewById(R.id.imageViewEmergencyContraception2);

        // Cache handling for the image.
        File imageFile_EC1 = new File(cacheDir, "EC1.png");
        File imageFile_EC2 = new File(cacheDir, "EC2.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_EC1, imageRef_EC1, imgViewEC1, bitmap -> bitmap_EC1 = bitmap);
        loadImage(imageFile_EC2, imageRef_EC2, imgViewEC2, bitmap -> bitmap_EC2 = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewEmergencyContraception1).setOnClickListener(v -> expandImage(bitmap_EC1));
        view.findViewById(R.id.imageViewEmergencyContraception2).setOnClickListener(v -> expandImage(bitmap_EC2));

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
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, FamilyPlanningFragment.BitmapLoadCallback callback) {

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
