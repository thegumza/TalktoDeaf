package project.se.game.wordgames;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatRadioButton;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.model.Game;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class WordsGame extends ActionBarActivity {

    private String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private Uri videoUri;
    private RadioGroup radioGroup;
    public static String VidName,correct,wrong;
    private VideoView videoView;
    private ArrayList<String> shufflelist;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SweetAlertDialog pDialog;
    private  String txtFirstChoice,txtSecondChoice;
    private String TOP_SCORE = "TopScore";
    private int score,topScore;
    private File actionDirectory;
    private ApiService retrofit;
    private GsonBuilder builder;
    private RestAdapter restAdapter;
    private MediaController mediacontroller;
    private File vid1;
    Game gm;
    FlatTextView txtTopScore;
    FlatRadioButton firstchoice,secondchoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_game);

        txtTopScore = (FlatTextView)findViewById(R.id.topscore);
        videoView = (VideoView)findViewById(R.id.videoView);
        radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        firstchoice = (FlatRadioButton)findViewById(R.id.firstchoice);
        secondchoice = (FlatRadioButton)findViewById(R.id.secondchoice);
        sp = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        //editor = sp.edit();
        topScore = sp.getInt(TOP_SCORE, 0);
        //editor.commit();
        txtTopScore.setText("คะแนนสูงสุด "+topScore+" คะแนน" );
        getApi();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.firstchoice) {
                    txtFirstChoice = firstchoice.getText().toString();
                    if(txtFirstChoice.equals(correct)){
                        score++;
                        getApi();

                    }
                    else {
                        Toast.makeText(WordsGame.this, "คุณตอบผิด", Toast.LENGTH_SHORT).show();

                        if (score > topScore) {
                            editor = sp.edit();
                            editor.putInt(TOP_SCORE, score);
                            editor.commit();

                        }


                    }
                    }

                if(checkedId == R.id.secondchoice){
                    txtSecondChoice = secondchoice.getText().toString();
                    if(txtSecondChoice.equals(correct)){
                        score++;
                        getApi();

                    }
                    else{
                        Toast.makeText(WordsGame.this, "คุณตอบผิด", Toast.LENGTH_SHORT).show();
                        if(score > topScore)
                        {
                            editor = sp.edit();
                            editor.putInt(TOP_SCORE,score);
                            editor.commit();

                        }
                    }
                }
                topScore = sp.getInt(TOP_SCORE, 0);
                txtTopScore.setText("คะแนนสูงสุด "+topScore+" คะแนน" );
                firstchoice.setChecked(false);
                secondchoice.setChecked(false);
            }
        });



        }


    private void getApi() {
        pDialog = new SweetAlertDialog(WordsGame.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();
        builder = new GsonBuilder();
        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        retrofit = restAdapter.create(ApiService.class);
        retrofit.getGameByMethodWithCallback(new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                try {

                    gm = game;
                    VidName = gm.getVid_name();

                    mediacontroller = new MediaController(WordsGame.this);
                    mediacontroller.setAnchorView(videoView);

                    actionDirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "action");
                    vid1 = new File(actionDirectory+"/"+VidName+".mp4");
                    if(!vid1.exists()){
                        pDialog.show();
                        Uri videoUri = Uri.parse("https://talktodeafphp-talktodeaf.rhcloud.com/talktodeaf_web/action_video/" + VidName + "");
                        mediacontroller.setAnchorView(videoView);
                        videoView.setMediaController(mediacontroller);
                        videoView.setVideoURI(videoUri);
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
                    else {
                        videoUri = Uri.parse(actionDirectory + "/" + VidName + ".mp4");
                        mediacontroller.setAnchorView(videoView);
                        videoView.setMediaController(mediacontroller);
                        videoView.setVideoURI(videoUri);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                videoView.start();
                            }
                        });

                    }

                    shufflelist = new ArrayList<String>();
                    correct = gm.getCorrect();
                    wrong =gm.getWrong();

                    shufflelist.add(correct);
                    shufflelist.add(wrong);

                    Collections.shuffle(shufflelist);

                    firstchoice.setText(shufflelist.get(0));
                    secondchoice.setText(shufflelist.get(1));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                    }
                });


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WordsGame.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
