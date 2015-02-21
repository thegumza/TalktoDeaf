package project.se.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.cengalabs.flatui.views.FlatButton;

import project.se.game.videogame.VideoGame;
import project.se.game.wordgame.WordGame;
import project.se.talktodeaf.R;

public class Game extends ActionBarActivity {
    FlatButton btnWordGame,btnVideoGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        btnWordGame = (FlatButton)findViewById(R.id.btn_word);
        btnVideoGame = (FlatButton)findViewById(R.id.btn_video);
        btnWordGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wordGame = new Intent(Game.this, WordGame.class);
                startActivity(wordGame);
            }
        });
        btnVideoGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoGame = new Intent(Game.this, VideoGame.class);
                startActivity(videoGame);
            }
        });

    }}