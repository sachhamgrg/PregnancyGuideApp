package com.example.pregnancyguideapp.breastfeeding;

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
import com.example.pregnancyguideapp.breastfeeding.log.BreastfeedingLogFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class BreastfeedingMainFragment extends Fragment {

    StorageReference StorageBreastfeedingRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("Breastfeeding");
    Bitmap bitmap_PBF, bitmap_BC;
    Bitmap[] BitmapIF = new Bitmap[3]; // Array of bitmap values for Infant Feeding images.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.breastfeeding);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_breastfeeding);

        return inflater.inflate(R.layout.fragment_breastfeedingmain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open up Breastfeeding Log fragment upon click event.
        view.findViewById(R.id.btnBreastfeedingLog).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BreastfeedingLogFragment(), "BreastfeedingLog_Frag").addToBackStack(null).commit());
        // Open up Poor breastfeeding video fragment upon click event.
        view.findViewById(R.id.btnBreastfeedingVideo).setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BreastfeedingVideoFragment(), "BreastfeedingVideo_Frag").addToBackStack(null).commit());

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // ---------------------------- Signs of Poor Breastfeeding --------------------------------

        // Toggle visibility for Signs of Poor Feeding Layout.
        LinearLayout PoorFeedingLayout = getActivity().findViewById(R.id.layout_SoPF);
        TextView txtViewPF = view.findViewById(R.id.txtSignsOfPoorFeeding);
        toggleLayout(PoorFeedingLayout, txtViewPF);

        // Create a reference to the image in Firebase Storage
        StorageReference PBF_imageRef = StorageBreastfeedingRef.child("PoorFeeding").child("SignsofPoorBreastfeeding.png");

        // Get a reference to the ImageView in your layout
        ImageView imageView_PBF = getActivity().findViewById(R.id.imageViewBreastfeeding);

        // Cache handling for the image.
        File imageFile_PBF = new File(cacheDir, "SignsofPoorBreastfeeding.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PBF, PBF_imageRef, imageView_PBF, bitmap -> bitmap_PBF = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBreastfeeding).setOnClickListener(v -> expandImage(bitmap_PBF));


        // -------------------------------------- Breast Care --------------------------------------

        // Toggle visibility for Breast Care Layout.
        LinearLayout BreastCareLayout = getActivity().findViewById(R.id.layout_BreastCare);
        TextView txtViewBC = view.findViewById(R.id.txtBreastCare);
        toggleLayout(BreastCareLayout, txtViewBC);


        // Create a reference to the image in Firebase Storage
        StorageReference BC_imageRef = StorageBreastfeedingRef.child("BreastCare").child("BreastCare.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewBreastCare = getActivity().findViewById(R.id.imageViewBreastCare);

        // Cache handling for the image.
        File imageFile_BC = new File(cacheDir, "BreastCare.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_BC, BC_imageRef, imgViewBreastCare, bitmap -> bitmap_BC = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBreastCare).setOnClickListener(v -> expandImage(bitmap_BC));

        // ------------------------------------ Infant Feeding -------------------------------------

        // Toggle visibility for Infant Feeding Layout.
        LinearLayout InfantFeedingLayout = getActivity().findViewById(R.id.layout_InfantFeeding);
        TextView txtViewIF = view.findViewById(R.id.txtInfantFeeding);
        toggleLayout(InfantFeedingLayout, txtViewIF);

        // Get references to the ImageViews inside the Infant Feeding layout.
        ImageView[] imageViewsIF = {
                getActivity().findViewById(R.id.imageViewInfantFeeding1),
                getActivity().findViewById(R.id.imageViewInfantFeeding2),
                getActivity().findViewById(R.id.imageViewInfantFeeding3)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsIF.length; i++) {
            String imageName = "IF" + (i+1) + ".png";
            StorageReference imageRef = StorageBreastfeedingRef.child("InfantFeeding").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsIF[i], bitmap -> BitmapIF[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsIF[i].setOnClickListener(v -> expandImage(BitmapIF[idx]));
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
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, BitmapLoadCallback callback) {

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
