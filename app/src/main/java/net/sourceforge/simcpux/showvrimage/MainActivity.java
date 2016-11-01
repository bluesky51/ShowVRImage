package net.sourceforge.simcpux.showvrimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showVRImage(View view) {
        startActivity(new Intent(MainActivity.this, VRImgActivity.class));
    }

    public void showVRVideo(View view) {
        startActivity(new Intent(MainActivity.this, VRVideoActivity.class));
    }


}