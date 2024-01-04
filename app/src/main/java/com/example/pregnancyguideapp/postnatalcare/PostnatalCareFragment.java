package com.example.pregnancyguideapp.postnatalcare;

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
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class PostnatalCareFragment extends Fragment {

    StorageReference StoragePostnatalCareRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("PostnatalCare");
    Bitmap[] Bitmap_PHandW = new Bitmap[3];    // Array of bitmap values for Physical health & wellbeing images.
    Bitmap bitmap_HC, bitmap_PD1, bitmap_PD2, bitmap_BB, bitmap_PC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.postnatal_care);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_postnatal_care);

        return inflater.inflate(R.layout.fragment_postnatal_care, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resizing of the TextView upon a click event for the 'Postnatal Care overview'.
        enlargeTextView(getActivity().findViewById(R.id.txtViewPC_overview));

        // Cache handling for image.
        File cacheDir = getActivity().getCacheDir();

        //-----------------------------Physical health and wellbeing--------------------------------

        // Toggle visibility for 'Physical health and wellbeing' Layout.
        LinearLayout PhysicalHealthLayout = getActivity().findViewById(R.id.layout_PhysicalHealthAndWellbeing);
        TextView txtViewPHandW = view.findViewById(R.id.txtPhysicalHealthAndWellbeing);
        toggleLayout(PhysicalHealthLayout, txtViewPHandW);

        // Resizing of the TextView upon a click event for 'Physical health and wellbeing'.
        enlargeTextView(getActivity().findViewById(R.id.txtViewPHandW));

        // Get references to the ImageViews inside Physical health and wellbeing layout.
        ImageView[] imageViewsPHandW = {
                getActivity().findViewById(R.id.imageViewPhysicalHealthAndWellbeing1),
                getActivity().findViewById(R.id.imageViewPhysicalHealthAndWellbeing2),
                getActivity().findViewById(R.id.imageViewPhysicalHealthAndWellbeing3)
        };

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViewsPHandW.length; i++) {
            String imageName = "PHandW" + (i+1) + ".png";
            StorageReference imageRef = StoragePostnatalCareRef.child("PhysicalHealthAndWellbeing").child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic
            // Store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViewsPHandW[i], bitmap -> Bitmap_PHandW[idx] = bitmap);
            // Add click listeners on the images if the user wishes to zoom in/out.
            imageViewsPHandW[i].setOnClickListener(v -> expandImage(Bitmap_PHandW[idx]));
        }

        //--------------------------------------Health Concerns-------------------------------------

        // Toggle visibility for 'Health Concerns' Layout.
        LinearLayout HealthConcernsLayout = getActivity().findViewById(R.id.layout_HealthConcerns);
        TextView txtViewHC = view.findViewById(R.id.txtHealthConcerns);
        toggleLayout(HealthConcernsLayout, txtViewHC);

        // Resizing of the TextView upon a click event for 'Health Concerns'.
        enlargeTextView(getActivity().findViewById(R.id.txtViewHC));

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_HC = StoragePostnatalCareRef.child("HealthConcerns").child("HealthConcerns.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewHealthConcerns = getActivity().findViewById(R.id.imageViewHealthConcerns);

        // Cache handling for the image.
        File imageFile_HC = new File(cacheDir, "HealthConcerns.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_HC, imageRef_HC, imgViewHealthConcerns, bitmap -> bitmap_HC = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewHealthConcerns).setOnClickListener(v -> expandImage(bitmap_HC));

        //-----------------------------------Postnatal Depression-----------------------------------

        // Toggle visibility for 'Postnatal Depression' Layout.
        LinearLayout PostnatalDepressionLayout = getActivity().findViewById(R.id.layout_PostnatalDepression);
        TextView txtViewPD = view.findViewById(R.id.txtPostnatalDepression);
        toggleLayout(PostnatalDepressionLayout, txtViewPD);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_PD1 = StoragePostnatalCareRef.child("PostnatalDepression").child("PND1.png");
        StorageReference imageRef_PD2 = StoragePostnatalCareRef.child("PostnatalDepression").child("PND2.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewPD1 = getActivity().findViewById(R.id.imageViewPostnatalDepression1);
        ImageView imgViewPD2 = getActivity().findViewById(R.id.imageViewPostnatalDepression2);

        // Cache handling for the image.
        File imageFile_PD1 = new File(cacheDir, "PND1.png");
        File imageFile_PD2 = new File(cacheDir, "PND2.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PD1, imageRef_PD1, imgViewPD1, bitmap -> bitmap_PD1 = bitmap);
        loadImage(imageFile_PD2, imageRef_PD2, imgViewPD2, bitmap -> bitmap_PD2 = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewPostnatalDepression1).setOnClickListener(v -> expandImage(bitmap_PD1));
        view.findViewById(R.id.imageViewPostnatalDepression2).setOnClickListener(v -> expandImage(bitmap_PD2));

        //-----------------------------------Baby Blues-----------------------------------

        // Toggle visibility for 'Baby Blues' Layout.
        LinearLayout BabyBluesLayout = getActivity().findViewById(R.id.layout_BabyBlues);
        TextView txtViewBB = view.findViewById(R.id.txtBabyBlues);
        toggleLayout(BabyBluesLayout, txtViewBB);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_BB = StoragePostnatalCareRef.child("BabyBlues").child("BabyBlues.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewBabyBlues = getActivity().findViewById(R.id.imageViewBabyBlues);

        // Cache handling for the image.
        File imageFile_BB = new File(cacheDir, "BabyBlues.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_BB, imageRef_BB, imgViewBabyBlues, bitmap -> bitmap_BB = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewBabyBlues).setOnClickListener(v -> expandImage(bitmap_BB));

        //-----------------------------------Postnatal Checkup--------------------------------------

        // Toggle visibility for 'Postnatal Checkup' Layout.
        LinearLayout PostnatalCheckupLayout = getActivity().findViewById(R.id.layout_PostnatalCheckup);
        TextView txtViewPC = view.findViewById(R.id.txtPostnatalCheckup);
        toggleLayout(PostnatalCheckupLayout, txtViewPC);

        // Create a reference to the image in Firebase Storage
        StorageReference imageRef_PC = StoragePostnatalCareRef.child("PostnatalCheckup").child("PostnatalCheckup.png");

        // Get a reference to the ImageView in your layout
        ImageView imgViewPostnatalCheckup = getActivity().findViewById(R.id.imageViewPostnatalCheckup);

        // Cache handling for the image.
        File imageFile_PC = new File(cacheDir, "PostnatalCheckup.png");

        // Call the method to load the image on the image view and store the returned bitmap value.
        loadImage(imageFile_PC, imageRef_PC, imgViewPostnatalCheckup, bitmap -> bitmap_PC = bitmap);

        // Add click listener on the image if the user wishes to zoom in/out.
        view.findViewById(R.id.imageViewPostnatalCheckup).setOnClickListener(v -> expandImage(bitmap_PC));

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

    private interface BitmapLoadCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }

    // Method to handle the cache and download the image from Firebase Storage. Then load those images into the imageViews.
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, PostnatalCareFragment.BitmapLoadCallback callback) {

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
