package com.example.pregnancyguideapp.pregnancy;

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
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class PregnancyFragment extends Fragment {

    StorageReference StoragePregnancyRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("Pregnancy");
    Bitmap[] Bitmap_SoP = new Bitmap[3];    // Array of bitmap values for Stages of Pregnancy images.
    Bitmap bitmap_PD1, bitmap_PD2, bitmap_BP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.pregnancy);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_pregnancy);

        return inflater.inflate(R.layout.fragment_pregnancy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // --------------------------------- Stages of Pregnancy -----------------------------------

        // Toggle visibility for Stages of Pregnancy Layout.
        LinearLayout StagesOfPregnancyLayout = getActivity().findViewById(R.id.layout_StagesOfPregnancy);
        TextView txtViewSoP = view.findViewById(R.id.txtStagesOfPregnancy);
        toggleLayout(StagesOfPregnancyLayout, txtViewSoP);

        // Get references to the ImageViews inside the Infant Feeding layout.
        ImageView[] imageViewsSoP = {
                getActivity().findViewById(R.id.imageViewStagesOfPregnancy1),
                getActivity().findViewById(R.id.imageViewStagesOfPregnancy2),
                getActivity().findViewById(R.id.imageViewStagesOfPregnancy3)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsSoP.length; i++) {
            String imageName = "Trimester" + (i+1) + ".png";
            StorageReference imageRef = StoragePregnancyRef.child("StagesOfPregnancy").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsSoP[i], bitmap -> Bitmap_SoP[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsSoP[i].setOnClickListener(v -> expandImage(Bitmap_SoP[idx]));
        }

        // ----------------------------------- Physical Discomfort ---------------------------------

        // Toggle visibility for Physical Discomfort Layout.
        LinearLayout PhysicalDiscomfortLayout = getActivity().findViewById(R.id.layout_PhysicalDiscomfort);
        TextView txtViewPD = view.findViewById(R.id.txtPhysicalDiscomfort);
        toggleLayout(PhysicalDiscomfortLayout, txtViewPD);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_PD1 = StoragePregnancyRef.child("PhysicalDiscomfort").child("PhysicalDiscomfort1.png");
        StorageReference imageRef_PD2 = StoragePregnancyRef.child("PhysicalDiscomfort").child("PhysicalDiscomfort2.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewPD1 = getActivity().findViewById(R.id.imageViewPhysicalDiscomfort1);
        ImageView imgViewPD2 = getActivity().findViewById(R.id.imageViewPhysicalDiscomfort2);

        // Cache handling for the image.
        File imageFile_PD1 = new File(cacheDir, "PhysicalDiscomfort1.png");
        File imageFile_PD2 = new File(cacheDir, "PhysicalDiscomfort2.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PD1, imageRef_PD1, imgViewPD1, bitmap -> bitmap_PD1 = bitmap);
        loadImage(imageFile_PD2, imageRef_PD2, imgViewPD2, bitmap -> bitmap_PD2 = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewPhysicalDiscomfort1).setOnClickListener(v -> expandImage(bitmap_PD1));
        view.findViewById(R.id.imageViewPhysicalDiscomfort2).setOnClickListener(v -> expandImage(bitmap_PD2));

        // ----------------------------------- Body Pain -------------------------------------------

        // Toggle visibility for Body Pain Layout.
        LinearLayout BodyPainLayout = getActivity().findViewById(R.id.layout_BodyPain);
        TextView txtViewBP = view.findViewById(R.id.txtBodyPain);
        toggleLayout(BodyPainLayout, txtViewBP);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_BP = StoragePregnancyRef.child("BodyPain").child("BodyPain.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewBodyPain = getActivity().findViewById(R.id.imageViewBodyPain);

        // Cache handling for the image.
        File imageFile_BP = new File(cacheDir, "BodyPain.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_BP, imageRef_BP, imgViewBodyPain, bitmap -> bitmap_BP = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBodyPain).setOnClickListener(v -> expandImage(bitmap_BP));

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
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, PregnancyFragment.BitmapLoadCallback callback) {

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
