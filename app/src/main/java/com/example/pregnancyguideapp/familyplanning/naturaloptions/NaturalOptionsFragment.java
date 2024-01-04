package com.example.pregnancyguideapp.familyplanning.naturaloptions;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
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
import com.example.pregnancyguideapp.familyplanning.medicaloptions.MOBarrierMethodsFragment;
import com.example.pregnancyguideapp.familyplanning.medicaloptions.MOHormonalFragment;
import com.example.pregnancyguideapp.familyplanning.medicaloptions.MOSterilizationFragment;
import com.example.pregnancyguideapp.familyplanning.medicaloptions.MedicalOptionsFragment;
import com.example.pregnancyguideapp.pregnancy.PregnancyFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class NaturalOptionsFragment extends Fragment {

    StorageReference StorageFP_MedicalOptionsRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("FamilyPlanning").child("NaturalOptions");
    Bitmap bitmap_RM, bitmap_CMM, bitmap_BBP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.family_planning);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_family_planning);

        return inflater.inflate(R.layout.fragment_fp_natural_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resizing of the TextView upon a click event for 'Birth Plan overview'.
        enlargeTextView(getActivity().findViewById(R.id.textView_NO_overview));

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        // ---------------------------------- Rhythm Method ----------------------------------------

        // Toggle visibility for Rhythm Method Layout.
        LinearLayout RhythmMethodLayout = getActivity().findViewById(R.id.layout_RhythmMethod);
        TextView txtViewRM = view.findViewById(R.id.txtRhythmMethod);
        toggleLayout(RhythmMethodLayout, txtViewRM);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_RM = StorageFP_MedicalOptionsRef.child("RhythmMethod.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewRhythmMethod = getActivity().findViewById(R.id.imageViewRhythmMethod);

        // Cache handling for the image.
        File imageFile_RM = new File(cacheDir, "RhythmMethod.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_RM, imageRef_RM, imgViewRhythmMethod, bitmap -> bitmap_RM = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewRhythmMethod).setOnClickListener(v -> expandImage(bitmap_RM));

        // -------------------------------- Cervical mucus Method ----------------------------------

        // Toggle visibility for Cervical mucus Method Layout.
        LinearLayout CervicalMucusMethodLayout = getActivity().findViewById(R.id.layout_CervicalMucusMethod);
        TextView txtViewCMM = view.findViewById(R.id.txtCervicalMucusMethod);
        toggleLayout(CervicalMucusMethodLayout, txtViewCMM);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_CMM = StorageFP_MedicalOptionsRef.child("CervicalMucusMethod.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewCervicalMucusMethod = getActivity().findViewById(R.id.imageViewCervicalMucusMethod);

        // Cache handling for the image.
        File imageFile_CMM = new File(cacheDir, "RhythmMethod.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_CMM, imageRef_CMM, imgViewCervicalMucusMethod, bitmap -> bitmap_CMM = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewCervicalMucusMethod).setOnClickListener(v -> expandImage(bitmap_CMM));


        // -------------------------------- Basal body temperature ---------------------------------

        // Toggle visibility for Basal body temperature Layout.
        LinearLayout BasalBodyTempLayout = getActivity().findViewById(R.id.layout_BasalBodyTemp);
        TextView txtViewBBP = view.findViewById(R.id.txtBasalBodyTemp);
        toggleLayout(BasalBodyTempLayout, txtViewBBP);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_BPP = StorageFP_MedicalOptionsRef.child("BasalBodyTemp.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewBasalBodyTemp = getActivity().findViewById(R.id.imageViewBasalBodyTemp);

        // Cache handling for the image.
        File imageFile_BPP = new File(cacheDir, "BasalBodyTemp.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_BPP, imageRef_BPP, imgViewBasalBodyTemp, bitmap -> bitmap_BBP = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBasalBodyTemp).setOnClickListener(v -> expandImage(bitmap_BBP));

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
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, NaturalOptionsFragment.BitmapLoadCallback callback) {

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
