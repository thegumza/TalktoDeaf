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

import java.util.ArrayList;
import java.util.List;

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
    String PlaceName,Address,Phone,PlaceNameTitle;
    String latitude,longtitude,slatitude,slongtitude;
    String place_name = PlaceInfo.place_name;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        placeName = (FlatTextView)findViewById(R.id.place_name);
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
        retrofit.getPlaceDetailByIdWithCallback(place_name,new Callback<List<Place_Detail>>() {

            @Override
            public void success(List<Place_Detail> listPlace, Response response) {
                List<Place_Detail> place = listPlace;

                ArrayList<String> arrPlaceName = new ArrayList<String>();
                ArrayList<String> arrPlaceAdress = new ArrayList<String>();
                ArrayList<String> arrPhone = new ArrayList<String>();
                ArrayList<Double> arrLat = new ArrayList<Double>();
                ArrayList<Double> arrLong = new ArrayList<Double>();

                for (Place_Detail pd : place) arrPlaceName.add((pd.getPlace_name()));
                for (Place_Detail pd : place) arrPlaceAdress.add((pd.getAddress()));
                for (Place_Detail pd : place) arrPhone.add((pd.getPhone()));
                for (Place_Detail pd : place) arrLat.add((pd.getLatitude()));
                for (Place_Detail pd : place) arrLong.add((pd.getLongitude()));

                PlaceName = arrPlaceName.toString();
                Address = arrPlaceAdress.toString();
                Phone = arrPhone.toString();
                latitude = arrLat.toString();
                longtitude = arrLong.toString();

                slatitude = latitude.substring(1, latitude.length() - 1);
                slongtitude = longtitude.substring(1, longtitude.length() - 1);


                LatLng School = new LatLng(Double.valueOf(slatitude), Double.valueOf(slongtitude));
                Marker places = map.addMarker(new MarkerOptions().position(School)
                            .title("" + PlaceName.substring(1, PlaceName.length() - 1)));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(School, 15));

                placeNameTitle.setText("" + PlaceName.substring(1, PlaceName.length() - 1));
                placeName.setText("ชื่อ: " + PlaceName.substring(1, PlaceName.length() - 1));
                placeAddress.setText("ที่อยู่: " + Address.substring(1, Address.length() - 1));
                phone.setText("เบอร์โทร: " + Phone.substring(1, Phone.length() - 1));
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Phone.substring(1, Phone.length() - 1).trim()));
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
