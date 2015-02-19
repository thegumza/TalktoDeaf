package project.se.action;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatTextView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.GsonBuilder;
import com.pnikosis.materialishprogress.ProgressWheel;

import project.se.model.VocabularyDetail;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionVocabularyDetail extends ActionBarActivity implements ObservableScrollViewCallbacks {
        FlatTextView vocName,vocDes,vocExam,catName,typeName,vocTitle;
        VideoView videoView;
        String VocName,DesName,VocExam,CatName,TypeName,VidName;
        String voc_name;
        ImageView imageView;
        ProgressWheel wheel;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            super.onCreate(savedInstanceState);
            //getSupportActionBar().hide();
            setContentView(R.layout.activity_action_vocabulary_detail);
            ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
            scrollView.setScrollViewCallbacks(this);
            vocTitle = (FlatTextView) findViewById(R.id.voc_title);
            videoView = (VideoView) findViewById(R.id.videoView);
            vocName = (FlatTextView) findViewById(R.id.voc_name);
            typeName = (FlatTextView) findViewById(R.id.type_name);
            vocDes = (FlatTextView) findViewById(R.id.voc_des);
            vocExam = (FlatTextView) findViewById(R.id.voc_exam);
            catName = (FlatTextView) findViewById(R.id.cat_name);
            imageView = (ImageView) findViewById(R.id.imageView);

            wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
            wheel.setBarColor(Color.rgb(25, 181, 254));

            GsonBuilder builder = new GsonBuilder();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                    .setConverter(new GsonConverter(builder.create()))
                    .build();
            ApiService retrofit = restAdapter.create(ApiService.class);
            voc_name = ActionVocabulary.getVoc_name();
            Log.d("vocabulary Name", "" + voc_name);
            retrofit.getVocabularyDetailByNameWithCallback(voc_name, new Callback<VocabularyDetail>() {

                @Override
                public void success(VocabularyDetail listVocDetail, Response response) {
                    VocabularyDetail vocDetail = listVocDetail;
                    VocName = vocDetail.getVoc_name();
                    DesName = vocDetail.getDes_name();
                    CatName = vocDetail.getCat_name();
                    TypeName = vocDetail.getType_name();
                    VocExam = vocDetail.getExam();
                    VidName = vocDetail.getVid_name();

                    vocTitle.setText("" + VocName);
                    vocName.setText("คำศัพท์: " + VocName);
                    vocDes.setText("รายละเอียด: " + DesName);
                    catName.setText("หมวด: " + CatName);
                    typeName.setText("ประเภท: " + TypeName);
                    vocExam.setText("ตัวอย่าง: " + VocExam);
                    wheel.spin();
                    try {
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(
                                ActionVocabularyDetail.this);
                        mediacontroller.setAnchorView(videoView);
                        // Get the URL from String VideoURL

                        Uri video = Uri.parse("http://talktodeafphp-talktodeaf.rhcloud.com/action_video/" + VidName + ".mp4");
                        videoView.setMediaController(mediacontroller);
                        videoView.setVideoURI(video);

                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        // Close the progress bar and play the video
                        public void onPrepared(MediaPlayer mp) {
                            //pDialog.dismiss();
                            wheel.stopSpinning();
                            videoView.start();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(ActionVocabularyDetail.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
