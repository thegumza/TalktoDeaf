package project.se.speak;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.model.Category;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import project.se.ui.CategoryListAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SpeakCategory extends ActionBarActivity implements SearchView.OnQueryTextListener {
    ListView listCategory;
    public static String cat_name;
    String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private SwipyRefreshLayout mSwipyRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_category);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                getCategoryRefresh();

            }
        });
        listCategory = (ListView)findViewById(R.id.listView);
        listCategory.setTextFilterEnabled(true);
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category catName = (Category) parent.getItemAtPosition(position);
                cat_name = catName.getCat_name();
                Intent detail = new Intent(SpeakCategory.this, SpeakVocabulary.class);
                startActivity(detail);
            }
        });
        getCategory();
    }
    public void getCategoryRefresh() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getCategoryByMethodWithCallback(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> category, Response response) {
                // accecss the items from you shop list here
                List<Category> ep = category;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listCategory.setAdapter(new CategoryListAdapter(SpeakCategory.this,ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakCategory.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                SpeakCategory.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
    public void getCategory() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getCategoryByMethodWithCallback(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> category, Response response) {
                // accecss the items from you shop list here

                List<Category> ep = category;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listCategory.setAdapter(new CategoryListAdapter(SpeakCategory.this,ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakCategory.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getCategory();
    }

    public static String getCat_name() {
        return cat_name;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getCategoryBySearchWithCallback(query,new Callback<List<Category>>() {
            @Override
            public void success(List<Category> category, Response response) {
                try {

                    List<Category> ep = category;
                    if(ep.isEmpty()){
                        new SweetAlertDialog(SpeakCategory.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ไม่พบคำที่คุณค้นหา")
                                .setContentText("กรุณาลองอีกครั้ง")
                                .show();
                    }
                    else {
                        listCategory.setAdapter(new CategoryListAdapter(SpeakCategory.this,ep));
                    }
                } catch (Exception e) {
                    new SweetAlertDialog(SpeakCategory.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ไม่พบคำที่คุณค้นหา")
                            .setContentText("กรุณาลองอีกครั้ง")
                            .show();
                    e.printStackTrace();
                    //Toast.makeText(ActionCategory.this, "Category Not Found", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakCategory.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
