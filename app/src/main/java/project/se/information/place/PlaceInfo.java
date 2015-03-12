package project.se.information.place;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import project.se.information.book.BookInfo;
import project.se.model.Place;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class PlaceInfo extends ActionBarActivity {
    private ListView listView;
    public static String place_name;
    private SwipyRefreshLayout mSwipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                getPlaceInfoRefresh();

            }
        });
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place placeName = (Place) parent.getItemAtPosition(position);
                place_name = placeName.getPlace_name();
                Intent bookDetail = new Intent(PlaceInfo.this, PlaceDetail.class);
                startActivity(bookDetail);
            }
        });
        getPlaceInfo();
    }
    private void getPlaceInfoRefresh() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getPlaceInfoByMethodWithCallback(new Callback<List<Place>>() {
            @Override
            public void success(List<Place> book, Response response) {
                // accecss the items from you shop list here

                List<Place> ep = book;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new BookListAdapter(ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PlaceInfo.this,
                        "Connect Failure Please Try Again",
                        Toast.LENGTH_LONG).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                PlaceInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
    private void getPlaceInfo() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getPlaceInfoByMethodWithCallback(new Callback<List<Place>>() {
            @Override
            public void success(List<Place> book, Response response) {
                // accecss the items from you shop list here

                List<Place> ep = book;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new BookListAdapter(ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PlaceInfo.this,
                        "Connect Failure Please Try Again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public class BookListAdapter extends BaseAdapter {

        List<Place> Place;
        public BookListAdapter(List<Place> sd) {
            Place = sd;
        }
        @Override
        public int getCount() {
            return Place.size();
        }

        @Override
        public Object getItem(int position) {
            return Place.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            FlatTextView placeName;
            FlatTextView position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder  holder;
            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_place_info_column, parent,false);
                holder = new ViewHolder();
                holder.position=(FlatTextView)convertView.findViewById(R.id.position);
                holder.placeName=(FlatTextView)convertView.findViewById(R.id.place_name);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Place bk = Place.get(position);
            holder.position.setText(""+(position+1));
            holder.placeName.setText("" + bk.getPlace_name());

            return convertView;
        }
    }
}