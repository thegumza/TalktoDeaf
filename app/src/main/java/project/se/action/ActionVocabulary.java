package project.se.action;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.model.Vocabulary;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import project.se.ui.VocabularyListAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionVocabulary extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private ListView listView;
    public static String  voc_name;
    public String cat_name;
    String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private SwipyRefreshLayout mSwipyRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_vocabulary);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                getVocabularyRefresh();

            }
        });

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vocabulary  vocName = (Vocabulary) parent.getItemAtPosition(position);
                voc_name = vocName.getVoc_name();
                Intent vocDetail = new Intent(ActionVocabulary.this, ActionVocabularyDetail.class);
                startActivity(vocDetail);
            }
        });
        getVocabulary();
    }
    private void getVocabularyRefresh() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = ActionCategory.getCat_name();
        Log.d("Category Name", "" + cat_name);
        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {
                // accecss the items from you shop list here

                List<Vocabulary> vc = voc;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new VocabularyListAdapter(ActionVocabulary.this,vc));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ActionVocabulary.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                ActionVocabulary.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
    private void getVocabulary() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = ActionCategory.getCat_name();
        Log.d("Category Name", "" + cat_name);

        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {
                // accecss the items from you shop list here

                List<Vocabulary> vc = voc;
                /*if(vc.isEmpty()){
                    Toast.makeText(ActionVocabulary.this, "ไม่พบคำศัพท์ในหมวดนี้", Toast.LENGTH_SHORT).show();
                }else*/
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new VocabularyListAdapter(ActionVocabulary.this,vc));

            }

            @Override
            public void failure(RetrofitError error) {
                        Toast.makeText(ActionVocabulary.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
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
                        new SweetAlertDialog(ActionVocabulary.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ไม่พบคำที่คุณค้นหา")
                                .setContentText("กรุณาลองอีกครั้ง")
                                .show();
                    }
                    else {
                        listView.setAdapter(new VocabularyListAdapter(ActionVocabulary.this,vc));
                    }
                } catch (Exception e) {
                    new SweetAlertDialog(ActionVocabulary.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ไม่พบคำที่คุณค้นหา")
                            .setContentText("กรุณาลองอีกครั้ง")
                            .show();
                    e.printStackTrace();
                }

            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ActionVocabulary.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
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
