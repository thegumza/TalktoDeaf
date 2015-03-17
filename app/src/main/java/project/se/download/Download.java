package project.se.download;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.cengalabs.flatui.views.FlatButton;

import project.se.download.ActionDownload.ActionCategoryDownload;
import project.se.download.SpeakDownload.SpeakCategoryDownload;
import project.se.talktodeaf.R;

/**
 * Created by wiwat on 3/18/2015.
 */
public class Download extends ActionBarActivity {
    FlatButton btnActionDownload, btnSpeakDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        btnActionDownload = (FlatButton) findViewById(R.id.btn_book);
        btnSpeakDownload = (FlatButton) findViewById(R.id.btn_place);
        btnActionDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent download = new Intent(Download.this, ActionCategoryDownload.class);
                startActivity(download);
            }
        });
        btnSpeakDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent download = new Intent(Download.this, SpeakCategoryDownload.class);
                startActivity(download);
            }
        });

    }
}