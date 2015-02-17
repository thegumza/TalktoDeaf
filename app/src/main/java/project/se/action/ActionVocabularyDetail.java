package project.se.action;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import project.se.model.VocabularyDetail;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionVocabularyDetail extends ActionBarActivity {
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
            retrofit.getVocabularyDetailByIdWithCallback(voc_name, new Callback<List<VocabularyDetail>>() {

                @Override
                public void success(List<VocabularyDetail> listVocDetail, Response response) {
                    List<VocabularyDetail> vocDetail = listVocDetail;

                    ArrayList<String> arrVocName = new ArrayList<String>();
                    ArrayList<String> arrVocDes = new ArrayList<String>();
                    ArrayList<String> arrVocExam = new ArrayList<String>();
                    ArrayList<String> arrCatName = new ArrayList<String>();
                    ArrayList<String> arrTypeName = new ArrayList<String>();
                    ArrayList<String> arrVidName = new ArrayList<String>();

                    for (VocabularyDetail vd : vocDetail) arrVocName.add((vd.getVoc_name()));
                    for (VocabularyDetail vd : vocDetail) arrCatName.add((vd.getCat_name()));
                    for (VocabularyDetail vd : vocDetail) arrTypeName.add((vd.getType_name()));
                    for (VocabularyDetail vd : vocDetail) arrVocDes.add((vd.getDes_name()));
                    for (VocabularyDetail vd : vocDetail) arrVocExam.add((vd.getExam()));
                    for (VocabularyDetail vd : vocDetail) arrVidName.add((vd.getVid_name()));

                    VocName = arrVocName.toString();
                    DesName = arrVocDes.toString();
                    VocExam = arrVocExam.toString();
                    CatName = arrCatName.toString();
                    TypeName = arrTypeName.toString();
                    VidName = arrVidName.toString();

                    vocTitle.setText("" + VocName.substring(1, VocName.length() - 1));
                    vocName.setText("คำศัพท์: " + VocName.substring(1, VocName.length() - 1));
                    vocDes.setText("รายละเอียด: " + DesName.substring(1, DesName.length() - 1));
                    catName.setText("หมวด: " + CatName.substring(1, CatName.length() - 1));
                    typeName.setText("ประเภท: " + TypeName.substring(1, TypeName.length() - 1));
                    vocExam.setText("ตัวอย่าง: " + VocExam.substring(1, VocExam.length() - 1));
                    wheel.spin();
                    try {
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(
                                ActionVocabularyDetail.this);
                        mediacontroller.setAnchorView(videoView);
                        // Get the URL from String VideoURL

                        Uri video = Uri.parse("http://talktodeafphp-talktodeaf.rhcloud.com/video/" + VidName.substring(1, VidName.length() - 1) + ".mp4");
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
}
