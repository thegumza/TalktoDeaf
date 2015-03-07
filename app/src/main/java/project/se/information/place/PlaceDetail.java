package project.se.information.place;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;

import project.se.model.Place_Detail;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class PlaceDetail extends ActionBarActivity {
    FlatTextView placeName,placeAddress,phone,placeNameTitle;
    String PlaceName,Address,Phone;
    Double latitude,longtitude;
    String place_name = PlaceInfo.place_name;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        //placeName = (FlatTextView)findViewById(R.id.place_name);
        placeAddress = (FlatTextView)findViewById(R.id.address);
        phone = (FlatTextView)findViewById(R.id.phone);
        placeNameTitle = (FlatTextView)findViewById(R.id.place_title);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getPlaceDetailByNameWithCallback(place_name,new Callback<Place_Detail>() {

            @Override
            public void success(Place_Detail listPlace, Response response) {
                Place_Detail place = listPlace;

                PlaceName = place.getPlace_name();
                Address = place.getAddress();
                Phone = place.getPhone();
                latitude = place.getLatitude();
                longtitude = place.getLongitude();



                LatLng School = new LatLng(Double.valueOf(latitude), Double.valueOf(longtitude));
                Marker places = map.addMarker(new MarkerOptions().position(School)
                            .title("" + PlaceName));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(School, 15));

                placeNameTitle.setText("" + PlaceName);
                //placeName.setText("ชื่อ: " + PlaceName);
                placeAddress.setText("ที่อยู่: " + Address);
                phone.setText("เบอร์โทร: " + Phone);
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Phone.trim()));
                        startActivity(callIntent );
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PlaceDetail.this,"Connection fail please try again",Toast.LENGTH_SHORT).show();
            }
        });

    }}
