package com.example.pregnancyguideapp.familyplanning.medicaloptions;

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
import com.example.pregnancyguideapp.pregnancy.PregnancyFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MOBarrierMethodsFragment extends Fragment {

    StorageReference StorageFP_MedicalOptionsRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("FamilyPlanning").child("MedicalOptions");
    Bitmap[] Bitmap_BM = new Bitmap[6];    // Array of bitmap values for Barrier Methods images.
    Bitmap bitmap_BM_overview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.family_planning);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_family_planning);

        return inflater.inflate(R.layout.fragment_fp_mo_barrier_methods, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // ----------------------------------- Barrier Methods Overview-------------------------------------------

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_BM_overview = StorageFP_MedicalOptionsRef.child("BarrierMethods").child("BarrierMethodsOverview.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewBM_overview = getActivity().findViewById(R.id.imageViewBarrierMethodsOverview);

        // Cache handling for the image.
        File imageFile_BM_overview = new File(cacheDir, "BarrierMethodsOverview.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_BM_overview, imageRef_BM_overview, imgViewBM_overview, bitmap -> bitmap_BM_overview = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBarrierMethodsOverview).setOnClickListener(v -> expandImage(bitmap_BM_overview));

        // -------------------------------- Types of Barrier Method ---------------------------------

        // Toggle visibility for Types of Barrier Method Layout.
        LinearLayout TypesOfBarrierMethodLayout = getActivity().findViewById(R.id.layout_TypesOfBarrierMethod);
        TextView txtViewToBM = view.findViewById(R.id.txtTypesOfBarrierMethod);
        toggleLayout(TypesOfBarrierMethodLayout, txtViewToBM);

        // Get references to the ImageViews inside Types of Barrier Method layout.
        ImageView[] imageViewsToBM = {
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod1),
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod2),
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod3),
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod4),
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod5),
                getActivity().findViewById(R.id.imageViewTypesOfBarrierMethod6)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsToBM.length; i++) {
            String imageName = "BM" + (i+1) + ".png";
            StorageReference imageRef = StorageFP_MedicalOptionsRef.child("BarrierMethods").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            int idx = i;
            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            loadImage(imageFile, imageRef, imageViewsToBM[i], bitmap -> Bitmap_BM[idx] = bitmap);

            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsToBM[i].setOnClickListener(v -> expandImage(Bitmap_BM[idx]));
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
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, MOBarrierMethodsFragment.BitmapLoadCallback callback) {

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
