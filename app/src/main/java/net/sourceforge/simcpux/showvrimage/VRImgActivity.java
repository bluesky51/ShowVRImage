package net.sourceforge.simcpux.showvrimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VRImgActivity extends AppCompatActivity {
    //VR图片
    String imgPath = "http://image.quhuwai.cn/16103111343978.jpg";
    VrPanoramaView vrPanoramaView;
    private ImageLoaderTask backgroundImageLoaderTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrimg);
        getSupportActionBar().setTitle("展示VR图片");
        vrPanoramaView = (VrPanoramaView) findViewById(R.id.pano_view);
        vrPanoramaView.setEventListener(new VrPanoramaEventListener() {
            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                Toast.makeText(VRImgActivity.this, "装载成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
                Toast.makeText(VRImgActivity.this, "装载失败，原因是：" +
                        errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClick() {
                super.onClick();
                Log.e("=====", "======");
            }

            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
            }
        });
        // Load the bitmap in a background thread to avoid blocking the UI thread. This operation can
        // take 100s of milliseconds.
        //网络VR图片展示
//        if (backgroundImageLoaderTask != null) {
//            // Cancel any task from a previous intent sent to this activity.
//            backgroundImageLoaderTask.cancel(true);
//        }
//        backgroundImageLoaderTask = new ImageLoaderTask();
//        backgroundImageLoaderTask.execute(imgPath);

        //本地assets目录下图片展示
        VrPanoramaView.Options options=new VrPanoramaView.Options();
        options.inputType=VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
        try {
            vrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(getAssets().open("panoramas/andes.jpg")),options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper class to manage threading.
     */
    class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * Reads the bitmap from disk in the background and waits until it's loaded by pano widget.
         */
        @Override
        protected Bitmap doInBackground(String... str) {
            Bitmap bmp = null;
            try {
                URL url = new URL(str[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                if (connection.getResponseCode() == 200) {
                    InputStream is = connection.getInputStream();
                    bmp = BitmapFactory.decodeStream(is);
                }

            } catch (Exception e) {

            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            VrPanoramaView.Options panoOptions = null;  // It's safe to use null VrPanoramaView.Options.
            panoOptions = new VrPanoramaView.Options();
            panoOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            vrPanoramaView.loadImageFromBitmap(bitmap, panoOptions);
        }
    }
}
