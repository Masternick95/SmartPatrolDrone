package com.example.nick.smartpatroldrone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class DroneControlActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button takePictureButton;

    private Context context = null;

    private final String PATH = "tcp://192.168.1.1:5555/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_control);
        context = getApplicationContext();

        videoView = (VideoView) findViewById(R.id.vitamio_videoView);
        takePictureButton = (Button) findViewById(R.id.takePhotoButton);

        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        videoView.setBufferSize(4096);
        videoView.setVideoPath(PATH);
        videoView.requestFocus();
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto(null);
            }
        });
    }

    private void capturePhoto(View view){
        try{
            new PhotoSaver(context, videoView.getMediaPlayer()).record();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Picture error!", Toast.LENGTH_SHORT).show();
        }
    }
}