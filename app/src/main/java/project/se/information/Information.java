package project.se.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.cengalabs.flatui.views.FlatButton;

import project.se.information.book.BookInfo;
import project.se.information.place.PlaceInfo;
import project.se.talktodeaf.R;

public class Information extends ActionBarActivity {
    FlatButton btnbookinfo, btnplaceinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        btnbookinfo = (FlatButton) findViewById(R.id.btn_book);
        btnplaceinfo = (FlatButton) findViewById(R.id.btn_place);
        btnbookinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookinfo = new Intent(Information.this, BookInfo.class);
                startActivity(bookinfo);
            }
        });
        btnplaceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeinfo = new Intent(Information.this, PlaceInfo.class);
                startActivity(placeinfo);
            }
        });

    }
}