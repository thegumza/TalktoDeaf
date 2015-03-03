package project.se.download;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import project.se.model.Vocabulary;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by wiwat on 2/27/2015.
 */
public class VocabularyDownload extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private ListView listView;
    public static String  voc_name,vid_name;
    public String cat_name;
    private MyDownloadStatusListener myDownloadStatusListener = new MyDownloadStatusListener();
    private ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    SweetAlertDialog pDialog;
    List<Vocabulary> vc;
    BaseAdapter adapter;
    int listposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_vocabulary);
        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);



        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vocabulary  vocName = (Vocabulary) parent.getItemAtPosition(position);
                listposition = position;
                voc_name = vocName.getVoc_name();
                vid_name = vocName.getVid_name();
                String video = ("http://talktodeafphp-talktodeaf.rhcloud.com/action_video/" + vid_name+".mp4");
                Uri downloadUri = Uri.parse(video);
                Uri destinationUri = Uri.parse(getApplicationContext().getFilesDir().toString()+"/"+vid_name+".mp4");

                final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setDownloadListener(myDownloadStatusListener);

                 downloadManager.add(downloadRequest1);
                pDialog.show();



            }
        });
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = CategoryDownload.getCat_name();
        Log.d("Category Name", "" + cat_name);
        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {

                vc = voc;

                adapter = new vocListAdapter(vc);

                listView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(VocabularyDownload.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
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

                    vc = voc;
                    if(vc.isEmpty()){
                        new SweetAlertDialog(VocabularyDownload.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ไม่พบคำที่คุณค้นหา")
                                .setContentText("กรุณาลองอีกครั้ง")
                                .show();
                    }
                    else {

                        listView.setAdapter(adapter);

                    }
                } catch (Exception e) {
                    new SweetAlertDialog(VocabularyDownload.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("ไม่พบคำที่คุณค้นหา")
                            .setContentText("กรุณาลองอีกครั้ง")
                            .show();
                    e.printStackTrace();
                }

            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(VocabularyDownload.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
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

        public class ViewHolder {
            FlatTextView vocName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder  holder;
            LayoutInflater inflater = getLayoutInflater();
            if(convertView == null){
                convertView = inflater.inflate(R.layout.download_vocabulary_list, parent,false);
                holder = new ViewHolder();
                holder.vocName=(FlatTextView)convertView.findViewById(R.id.vocName);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Vocabulary bk = Vocabulary.get(position);
            holder.vocName.setText("" + bk.getVoc_name());

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

    private class MyDownloadStatusListener implements DownloadStatusListener {
        @Override
        public void onDownloadComplete(int id) {
            pDialog.dismiss();
            View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
            final Crouton crouton;
            vc.remove(listposition);
            adapter.notifyDataSetChanged();
            crouton = Crouton.make(VocabularyDownload.this, view);
            crouton.show();
        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            pDialog.dismiss();
            View view = getLayoutInflater().inflate(R.layout.crouton_custom_error_view, null);
            final Crouton crouton;
            crouton = Crouton.make(VocabularyDownload.this, view);
            crouton.show();
        }

        @Override
        public void onProgress(int id, long totalBytes, int progress) {
        }
    }


}



