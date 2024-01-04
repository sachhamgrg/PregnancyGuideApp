package com.example.pregnancyguideapp.nutrition;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ExerciseFragment extends Fragment {

    StorageReference StorageExerciseRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("Exercise");
    Bitmap[] setBitmapE = new Bitmap[5];    // Array of bitmap values for Exercise images.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.nutrition);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_nutrition);

        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get references to the ImageViews inside the layout.
        ImageView[] imageViews = {
                getActivity().findViewById(R.id.imageViewExercise1),
                getActivity().findViewById(R.id.imageViewExercise2),
                getActivity().findViewById(R.id.imageViewExercise3),
                getActivity().findViewById(R.id.imageViewExercise4),
                getActivity().findViewById(R.id.imageViewExercise5)
        };

        // Cache handling for image/video.
        File cacheDir = getActivity().getCacheDir();

        // Iterate over the ImageViews and StorageReferences, and handle the cache.
        for (int i = 0; i < imageViews.length; i++) {
            String imageName = "Exercise" + (i+1) + ".png";
            StorageReference imageRef = StorageExerciseRef.child(imageName);
            File imageFile = new File(cacheDir, imageName);

            // Call a function to handle the cache and download logic.
            // store bitmap values inside an array.
            int idx = i;
            loadImage(imageFile, imageRef, imageViews[i], bitmap -> setBitmapE[idx] = bitmap);
        }

        // Ensures the image is expanded once the transition is finished on each imageViews.
        MotionLayout motionLayout = view.findViewById(R.id.motionLayoutExercise);
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {}
            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {}
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                // After the transition is completed, expand the image inside a inflator layout.
                switch (currentId) {
                    case R.id.endE1:
                        expandImage(setBitmapE[0],motionLayout);
                        break;
                    case R.id.endE2:
                        expandImage(setBitmapE[1],motionLayout);
                        break;
                    case R.id.endE3:
                        expandImage(setBitmapE[2],motionLayout);
                        break;
                    case R.id.endE4:
                        expandImage(setBitmapE[3],motionLayout);
                        break;
                    case R.id.endE5:
                        expandImage(setBitmapE[4],motionLayout);
                        break;
                }
            }
            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {}
        });
    }

    // Callback interface for notifying when a bitmap has been loaded.
    private interface BitmapLoadCallback {
        // Called when the bitmap has been loaded.
        void onBitmapLoaded(Bitmap bitmap);
    }

    // Method to handle the cache and download the image from Firebase Storage. Then load those images into the imageViews.
    private void loadImage(File imageFile, StorageReference imageRef, ImageView imageView, ExerciseFragment.BitmapLoadCallback callback) {
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
    private void expandImage(Bitmap bitmap, MotionLayout motionLayout) {

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

        // On dismiss of the dialog box, the motion layout goes back to its initial state.
        mDialog.setOnDismissListener(dialog -> motionLayout.transitionToStart());
    }


}
