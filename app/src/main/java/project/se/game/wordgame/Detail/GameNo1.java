package project.se.game.wordgame.Detail;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.cengalabs.flatui.views.FlatRadioButton;
import com.google.gson.GsonBuilder;

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
public class GameNo1 extends Fragment {
    String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private RadioGroup radioGroup;
    Game gm;
    String choice1,choice2,voc_name,VidName;
    FlatRadioButton correctchoice,wrongchoice;
    VideoView videoView;

    private static final String ARG_POSITION = "position";

    private int position;

    public static GameNo1 newInstance(int position) {
        GameNo1 f = new GameNo1();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        correctchoice = (FlatRadioButton)rootView.findViewById(R.id.correctchoice);
        wrongchoice = (FlatRadioButton)rootView.findViewById(R.id.wrongchoice);


        return rootView;
    }
    private void getApi() {
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
                    try {
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(
                                getActivity());
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
                            //videoView.start();
                        }
                    });
                    correctchoice.setText(gm.getCorrect());
                    wrongchoice.setText(gm.getWrong());
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // find which radio button is selected
                            if (checkedId == R.id.correctchoice) {

                            }
                            if (checkedId == R.id.wrongchoice) {
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

