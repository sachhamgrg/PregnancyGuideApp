package com.example.pregnancyguideapp.maleinvolvement;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class MaleInvolvementFragment extends Fragment {

    StorageReference StoragePostnatalCareRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("MaleInvolvement");
    Bitmap[] Bitmap_GIiP = new Bitmap[3];    // Array of bitmap values for 'Getting involved in the Pregnancy'.
    Bitmap bitmap_PaP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.male_involvement);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_male_involvement);

        return inflater.inflate(R.layout.fragment_male_involvement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        //-----------------------------------Planning a Pregnancy-----------------------------------

        // Toggle visibility for 'Planning a Pregnancy' Layout.
        LinearLayout PaPLayout = getActivity().findViewById(R.id.layout_PlanningAPregnancy);
        TextView txtViewPaP = view.findViewById(R.id.txtPlanningAPregnancy);
        toggleLayout(PaPLayout, txtViewPaP);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_PaP = StoragePostnatalCareRef.child("MaleInvolvement-PaP.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewPaP = getActivity().findViewById(R.id.imageViewPlanningAPregnancy);

        // Cache handling for the image.
        File imageFile_PaP = new File(cacheDir, "MaleInvolvement-PaP.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PaP, imageRef_PaP, imgViewPaP, bitmap -> bitmap_PaP = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewPlanningAPregnancy).setOnClickListener(v -> expandImage(bitmap_PaP));


        //-------------------------------Getting involved in a Pregnancy----------------------------

        // Toggle visibility for 'Getting involved in a Pregnancy' Layout.
        LinearLayout GIiPLayout = getActivity().findViewById(R.id.layout_GettingInvolvedInAPregnancy);
        TextView txtViewGIiP = view.findViewById(R.id.txtGettingInvolvedInAPregnancy);
        toggleLayout(GIiPLayout, txtViewGIiP);


        // Get references to the ImageViews inside Physical health and wellbeing layout.
        ImageView[] imageViewsGIiP = {
                getActivity().findViewById(R.id.imageViewGettingInvolvedInAPregnancy1),
                getActivity().findViewById(R.id.imageViewGettingInvolvedInAPregnancy2),
                getActivity().findViewById(R.id.imageViewGettingInvolvedInAPregnancy3)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsGIiP.length; i++) {
            String imageName = "MaleInvolvement-InvolvingT" + (i+1) + ".png";
            StorageReference imageRef = StoragePostnatalCareRef.child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsGIiP[i], bitmap -> Bitmap_GIiP[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsGIiP[i].setOnClickListener(v -> expandImage(Bitmap_GIiP[idx]));
        }

        //-----------------------------Access the Youtube videos from the app-----------------------

        view.findViewById(R.id.textView_How_to_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeURL = "https://www.youtube.com/watch?v=BAoCtP65z7I";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
                startActivity(intent);
            }
        });

        view.findViewById(R.id.textView_Your_first__few_days_as_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeURL = "https://www.youtube.com/watch?v=m-8-_tISdSU";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
                startActivity(intent);
            }
        });
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

    private interface BitmapLoadCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }

    // Method to handle the cache and download the image from Firebase Storage. Then load those images into the imageViews.
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, MaleInvolvementFragment.BitmapLoadCallback callback) {

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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.zoom_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imgPhotoView);
        photoView.setImageBitmap(bitmap);
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

}
