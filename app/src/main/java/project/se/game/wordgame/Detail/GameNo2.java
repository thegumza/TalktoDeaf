package project.se.game.wordgame.Detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatRadioButton;
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

/**
 * Created by wiwat on 2/22/2015.
 */
public class GameNo2 extends Fragment{
    String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private RadioGroup radioGroup;
    Game gm;
    public static String VidName,correct,wrong;
    FlatRadioButton firstchoice,secondchoice;
    VideoView videoView;
    ArrayList<String> shufflelist;
    private static final String ARG_POSITION = "position";
    private int position;
    public static String answer = "correct";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    SweetAlertDialog pDialog;
    public static GameNo2 newInstance(int position) {
        GameNo2 f = new GameNo2();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getActivity().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("Answer2", "");
        editor.commit();
        position = getArguments().getInt(ARG_POSITION);
        getApi();



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_word_game, container, false);
        rootView.findViewById(R.id.linear_layout);
        videoView = (VideoView)rootView.findViewById(R.id.videoView);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.myRadioGroup);
        firstchoice = (FlatRadioButton)rootView.findViewById(R.id.firstchoice);
        secondchoice = (FlatRadioButton)rootView.findViewById(R.id.secondchoice);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.firstchoice) {
                    /*editor.putString("Answer1", shufflelist.get(0));
                    editor.commit();*/
                    if(shufflelist.get(0).equals(correct)){
                        editor.putString("Answer2", shufflelist.get(0));
                        editor.commit();
                    }
                    else{
                        editor.putString("Answer2", "");
                        editor.commit();
                    }
                }
                if (checkedId == R.id.secondchoice) {
                    /*editor.putString("Answer1", shufflelist.get(1));
                    editor.commit();*/
                    if(shufflelist.get(1).equals(correct)){
                        editor.putString("Answer2", shufflelist.get(1));
                        editor.commit();
                    }
                    else{
                        editor.putString("Answer2", "");
                        editor.commit();
                    }
                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
                videoView.start();
    }
    public static String getCorrect() {
        return correct;
    }
    public static String getWrong() {
        return wrong;
    }
    private void getApi() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);
        pDialog.show();
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getGameByMethodWithCallback(new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                try {
                    gm = game;
                    VidName = gm.getVid_name();
                    MediaController mediacontroller = new MediaController(getActivity());
                    mediacontroller.setAnchorView(videoView);

                    final File actiondirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "action");
                    File vid1 = new File(actiondirectory+"/"+VidName+".mp4");
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
                        Uri videoUri = Uri.parse(actiondirectory + "/" + VidName + ".mp4");
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
                Toast.makeText(getActivity(), "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

