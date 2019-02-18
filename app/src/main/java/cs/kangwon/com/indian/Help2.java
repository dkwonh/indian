package cs.kangwon.com.indian;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.widget.MediaController;
import android.os.Bundle;
import android.widget.VideoView;

import cs.kangwon.com.teamsw.R;

public class Help2 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.help2);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        //Button b = (Button) findViewById(R.id.newbutton);

        MediaController mediaController = new MediaController(this);

        mediaController.setAnchorView(videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/indian");

        videoView.setMediaController(mediaController);

        videoView.setVideoURI(video);

        videoView.requestFocus();

        videoView.start();
    }
}
