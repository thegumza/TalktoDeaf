package project.se.speak;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatTextView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.GsonBuilder;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.model.VocabularyDetail;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SpeakVocabularyDetail extends ActionBarActivity implements ObservableScrollViewCallbacks {
    FlatTextView vocName,vocDes,vocExam,catName,typeName,vocTitle;
    VideoView videoView;
    String VocName,DesName,VocExam,CatName,TypeName,VidName;
    String voc_name;
    ImageView imageView;
    //ProgressWheel wheel;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_speak_vocabulary_detail);
        final File speakdirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "speak");
        vocTitle = (FlatTextView) findViewById(R.id.voc_title);
        videoView = (VideoView) findViewById(R.id.videoView);
        vocName = (FlatTextView) findViewById(R.id.voc_name);
        typeName = (FlatTextView) findViewById(R.id.type_name);
        vocDes = (FlatTextView) findViewById(R.id.voc_des);
        vocExam = (FlatTextView) findViewById(R.id.voc_exam);
        catName = (FlatTextView) findViewById(R.id.cat_name);
        imageView = (ImageView) findViewById(R.id.imageView);
        //wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        //wheel.setBarColor(Color.rgb(25, 181, 254));
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        voc_name = SpeakVocabulary.getVoc_name();
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

                //wheel.spin();
                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(
                            SpeakVocabularyDetail.this);
                    mediacontroller.setAnchorView(videoView);
                    // Get the URL from String VideoURL
                    File vid1 = new File(speakdirectory+"/"+VidName+".mp4");
                    if(!vid1.exists()){
                        pDialog.show();
                        Uri video = Uri.parse("http://talktodeafphp-talktodeaf.rhcloud.com/speak_video/" + VidName + ".mp4");
                        videoView.setMediaController(mediacontroller);
                        videoView.setVideoURI(video);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                //pDialog.dismiss();
                                //wheel.stopSpinning();
                                pDialog.dismiss();
                                videoView.start();
                            }
                        });
                    }
                    else{
                        Uri video = Uri.parse(speakdirectory+"/"+VidName+".mp4");
                        videoView.setVideoURI(video);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                videoView.start();
                            }
                        });
                    }

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakVocabularyDetail.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
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
