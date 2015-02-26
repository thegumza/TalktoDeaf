package project.se.speak;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.model.Vocabulary;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SpeakVocabulary extends ActionBarActivity implements SearchView.OnQueryTextListener{
    private ListView listView;
    public static String  voc_name;
    public String cat_name;
    String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_vocabulary);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vocabulary vocName = (Vocabulary) parent.getItemAtPosition(position);
                voc_name = vocName.getVoc_name();
                Intent vocDetail = new Intent(SpeakVocabulary.this, SpeakVocabularyDetail.class);
                startActivity(vocDetail);
            }
        });
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = SpeakCategory.getCat_name();
        Log.d("Category Name", "" + cat_name);
        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {
                // accecss the items from you shop list here

                List<Vocabulary> vc = voc;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new vocListAdapter(vc));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakVocabulary.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getVoc_name() {
        return voc_name;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Query String", "" + query);
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getVocabularyBySearchWithCallback(cat_name,query, new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {
                try {

                    List<Vocabulary> vc = voc;
                    if(vc.isEmpty()){
                        new SweetAlertDialog(SpeakVocabulary.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ไม่พบคำที่คุณค้นหา")
                                .setContentText("กรุณาลองอีกครั้ง")
                                .show();
                    }
                    else {
                        listView.setAdapter(new vocListAdapter(vc));
                    }
                } catch (Exception e) {
                    new SweetAlertDialog(SpeakVocabulary.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ไม่พบคำที่คุณค้นหา")
                            .setContentText("กรุณาลองอีกครั้ง")
                            .show();
                    e.printStackTrace();
                    //Toast.makeText(ActionCategory.this, "Category Not Found", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakVocabulary.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class vocListAdapter extends BaseAdapter {

        List<Vocabulary> Vocabulary;
        public vocListAdapter(List<Vocabulary> sd) {
            Vocabulary = sd;
        }
        @Override
        public int getCount() {
            return Vocabulary.size();
        }

        @Override
        public Object getItem(int position) {
            return Vocabulary.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            FlatTextView vocName;
            FlatTextView position;
            ImageView imageview;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder  holder;
            LayoutInflater inflater = getLayoutInflater();
            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_action_vocabulary_column, parent,false);
                holder = new ViewHolder();
                holder.position=(FlatTextView)convertView.findViewById(R.id.position);
                holder.imageview=(ImageView)convertView.findViewById(R.id.imageView);
                holder.vocName=(FlatTextView)convertView.findViewById(R.id.vocName);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Vocabulary bk = Vocabulary.get(position);
            String FirstVoc = bk.getVoc_name().substring(0, 1);
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ThaiSansNeue_regular.ttf");
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .useFont(type)
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound("" + FirstVoc, Color.DKGRAY);
            NumberFormat f = new DecimalFormat("00");
            String vidname = bk.getVid_name();
            holder.position.setText(""+f.format(position + 1));
            holder.vocName.setText("" + bk.getVoc_name());
            holder.imageview.setImageDrawable(drawable);

            return convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_category, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("ป้อนคำค้นหา");
        return true;
    }
}
