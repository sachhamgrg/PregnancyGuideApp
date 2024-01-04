package com.example.pregnancyguideapp.breastfeeding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class BreastfeedingVideoFragment extends Fragment {

    StorageReference StorageBreastfeedingRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("Breastfeeding");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.breastfeeding);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_breastfeeding);

        return inflater.inflate(R.layout.fragment_breastfeedingvideo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cache handling for image/video.
        File cacheDir = getActivity().getCacheDir();

        // -------------------------Breastfeeding demo video----------------------------------------
        final StorageReference BF_videoRef = StorageBreastfeedingRef.child("PoorFeeding").child("PoorFeedingDemo.mp4");
        VideoView videoView = getActivity().findViewById(R.id.videoViewBreastfeeding);

        // Add appropriate controls to the VideoView.
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Cache handling for the video.

        File videoFile = new File(cacheDir, "PoorFeedingDemo.mp4");

        if (videoFile.exists()) {
            // Video already exists in the cache directory, load it.
            videoView.setVideoPath(videoFile.getAbsolutePath());
        } else {
            // Video does not exist in the cache directory, download it and save to cache
            BF_videoRef.getFile(videoFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Video downloaded successfully, load it from the cache.
                            videoView.setVideoPath(videoFile.getAbsolutePath());
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            exception.printStackTrace();
                        }
                    });
        }


    }

}
