package project.se.action;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.android.segmented.SegmentedGroup;
import project.se.model.VocabularyDetail;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionVocabularyDetail extends ActionBarActivity  {
        FlatTextView vocName,vocDes,vocExam,catName,typeName,vocTitle;
        VideoView videoView;
        String VocName,DesName,VocExam,CatName,TypeName,VidName,VocEngName,EngCatName,EngTypeName,EngDesName,EngVocExam;
        String voc_name;
        ImageView imageView;
        SweetAlertDialog pDialog;
        File actiondirectory;
        SegmentedGroup segmented;
        VocabularyDetail vocDetail;
        Uri video;
        MediaController mediacontroller;
        ApiService retrofit;
        GsonBuilder builder;
        RestAdapter restAdapter;
        RadioButton btnTH,btnEN;
        private Typeface typeface;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            super.onCreate(savedInstanceState);
            //getSupportActionBar().hide();
            setContentView(R.layout.activity_action_vocabulary_detail);
            //ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
            //scrollView.setScrollViewCallbacks(this);
            segmented = (SegmentedGroup) findViewById(R.id.segmented);
            btnTH = (RadioButton)findViewById(R.id.btnTH);
            btnEN = (RadioButton)findViewById(R.id.btnEN);
            vocTitle = (FlatTextView) findViewById(R.id.voc_title);
            videoView = (VideoView) findViewById(R.id.videoView);
            vocName = (FlatTextView) findViewById(R.id.voc_name);
            typeName = (FlatTextView) findViewById(R.id.type_name);
            vocDes = (FlatTextView) findViewById(R.id.voc_des);
            vocExam = (FlatTextView) findViewById(R.id.voc_exam);
            catName = (FlatTextView) findViewById(R.id.cat_name);
            imageView = (ImageView) findViewById(R.id.imageView);
            typeface = Typeface.createFromAsset(getAssets(), "fonts/ThaiSansNeue_regular.ttf");
            btnTH.setTypeface(typeface);
            btnEN.setTypeface(typeface);
            btnTH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(btnTH.isChecked()){
                        vocTitle.setText("" + VocName);
                        vocName.setText("คำศัพท์: " + VocName);
                        vocDes.setText("ความหมาย: " + DesName);
                        catName.setText("หมวด: " + CatName);
                        typeName.setText("ประเภท: " + TypeName);
                        vocExam.setText("ตัวอย่าง: " + VocExam);
                    }}

            });
            btnEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(btnEN.isChecked()){
                    vocTitle.setText("" + VocEngName);
                    vocName.setText("Name: " + VocEngName);
                    vocDes.setText("Description: " + EngDesName);
                    catName.setText("Category: " + EngCatName);
                    typeName.setText("Type: " + EngTypeName);
                    vocExam.setText("Example: " + EngVocExam);
                }}
            });
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
            pDialog.setTitleText("กำลังดาวน์โหลด");
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(true);

            actiondirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "action");
            //wheel = (ProgressWheel) findViewById(R.id.progress_wheel);
            //wheel.setBarColor(Color.rgb(25, 181, 254));

            builder = new GsonBuilder();
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                    .setConverter(new GsonConverter(builder.create()))
                    .build();
            retrofit = restAdapter.create(ApiService.class);
            voc_name = ActionVocabulary.getVoc_name();
            Log.d("vocabulary Name", "" + voc_name);
            retrofit.getVocabularyDetailByNameWithCallback(voc_name, new Callback<VocabularyDetail>() {

                @Override
                public void success(VocabularyDetail listVocDetail, Response response) {
                    vocDetail = listVocDetail;
                    VocName = vocDetail.getVoc_name();
                    DesName = vocDetail.getDes_name();
                    CatName = vocDetail.getCat_name();
                    TypeName = vocDetail.getType_name();
                    VocExam = vocDetail.getExam();
                    VidName = vocDetail.getVid_name();
                    VocEngName = vocDetail.getEng_voc_name();
                    EngCatName = vocDetail.getEng_cat_name();
                    EngTypeName = vocDetail.getEng_type_name();
                    EngDesName = vocDetail.getEng_des_name();
                    EngVocExam = vocDetail.getEng_exam();

                    vocTitle.setText("" + VocName);
                    vocName.setText("คำศัพท์: " + VocName+"");
                    vocDes.setText("ความหมาย: " + DesName);
                    catName.setText("หมวด: " + CatName);
                    typeName.setText("ประเภท: " + TypeName);
                    vocExam.setText("ตัวอย่าง: " + VocExam);
                    //wheel.spin();
                    try {
                        // Start the MediaController
                        mediacontroller = new MediaController(ActionVocabularyDetail.this);

                        // Get the URL from String VideoURL
                        File vid1 = new File(actiondirectory+"/"+VidName+".mp4");
                        if(!vid1.exists()){
                            pDialog.show();
                            Uri video = Uri.parse("https://talktodeafphp-talktodeaf.rhcloud.com/talktodeaf_web/action_video/" + VidName + "");
                            mediacontroller.setAnchorView(videoView);
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
                            video = Uri.parse(actiondirectory+"/"+VidName+".mp4");
                            mediacontroller.setAnchorView(videoView);
                            videoView.setMediaController(mediacontroller);
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
                    Toast.makeText(ActionVocabularyDetail.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }




}
