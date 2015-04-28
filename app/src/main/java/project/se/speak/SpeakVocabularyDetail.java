package project.se.speak;

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

public class SpeakVocabularyDetail extends ActionBarActivity  {
    FlatTextView vocName,vocDes,vocExam,catName,typeName,vocTitle;
    VideoView videoView;
    String VocName,DesName,VocExam,CatName,TypeName,VidName,VocEngName,EngCatName,EngTypeName,EngDesName,EngVocExam;
    String voc_name;
    ImageView imageView;
    SweetAlertDialog pDialog;
    File speakdirectory;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_vocabulary_detail);
        speakdirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "speak");
        vocTitle = (FlatTextView) findViewById(R.id.voc_title);
        videoView = (VideoView) findViewById(R.id.videoView);
        vocName = (FlatTextView) findViewById(R.id.voc_name);
        typeName = (FlatTextView) findViewById(R.id.type_name);
        vocDes = (FlatTextView) findViewById(R.id.voc_des);
        vocExam = (FlatTextView) findViewById(R.id.voc_exam);
        catName = (FlatTextView) findViewById(R.id.cat_name);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnTH = (RadioButton)findViewById(R.id.btnTH);
        btnEN = (RadioButton)findViewById(R.id.btnEN);
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
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);



        builder = new GsonBuilder();
        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        retrofit = restAdapter.create(ApiService.class);
        voc_name = SpeakVocabulary.getVoc_name();
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
                vocName.setText("คำศัพท์: " + VocName);
                vocDes.setText("ความหมาย: " + DesName);
                catName.setText("หมวด: " + CatName);
                typeName.setText("ประเภท: " + TypeName);
                vocExam.setText("ตัวอย่าง: " + VocExam);

                //wheel.spin();
                try {
                    // Start the MediaController
                    mediacontroller = new MediaController(
                            SpeakVocabularyDetail.this);
                    mediacontroller.setAnchorView(videoView);
                    // Get the URL from String VideoURL
                    File vid1 = new File(speakdirectory+"/"+VidName+".mp4");
                    if(!vid1.exists()){
                        pDialog.show();
                        Uri video = Uri.parse("https://talktodeafphp-talktodeaf.rhcloud.com/talktodeaf_web/speak_video/" + VidName + "");
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
                        video = Uri.parse(speakdirectory+"/"+VidName+".mp4");
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
                Toast.makeText(SpeakVocabularyDetail.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
