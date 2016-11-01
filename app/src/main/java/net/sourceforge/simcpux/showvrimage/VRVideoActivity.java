package net.sourceforge.simcpux.showvrimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class VRVideoActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    //VR视频
    String videoPath = "http://video.quhuwai.cn/qj24.mp4";
    /**
     * The video view and its custom UI elements.
     */
    protected VrVideoView videoWidgetView;

    SeekBar seekBar;
    /**
     * By default, the video will start playing as soon as it is loaded. This can be changed by using
     * {@link VrVideoView#pauseVideo()} after loading the video.
     */
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrvideo);
        getSupportActionBar().setTitle("展示VR视频");
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        // Bind input and output objects for the view.
        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);
        videoWidgetView.setEventListener(new ActivityEventListener());
        VrVideoView.Options options = new VrVideoView.Options();
        options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
        try {
            //videoWidgetView.loadVideo(Uri.parse(videoPath), options);
            videoWidgetView.loadVideoFromAsset("videos/congo.mp4", options);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Prevent the view from rendering continuously when in the background.
        videoWidgetView.pauseRendering();
        // If the video is playing when onPause() is called, the default behavior will be to pause
        // the video and keep it paused when onResume() is called.
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the 3D rendering.
        videoWidgetView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        // Destroy the widget and free memory.
        videoWidgetView.shutdown();
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            videoWidgetView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    private void togglePause() {
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
    }

    private class ActivityEventListener extends VrVideoEventListener {


        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            seekBar.setMax((int)videoWidgetView.getDuration());
        }

        @Override
        public void onClick() {
            super.onClick();
            togglePause();
        }

        @Override
        public void onNewFrame() {
            super.onNewFrame();
            seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
        }
        /**
         * Make the video play in a loop. This method could also be used to move to the next video in
         * a playlist.
         */
        @Override
        public void onCompletion() {
            videoWidgetView.seekTo(0);
        }
        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
        }
    }


}
