package project.se.download.SpeakDownload;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import project.se.model.Vocabulary;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import project.se.ui.DownloadSpeakListAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by wiwat on 2/27/2015.
 */
public class SpeakVocabularyDownload extends ActionBarActivity  {
    private ListView listView;
    public static String  voc_name,vid_name;
    public String cat_name;
    private MyDownloadStatusListener myDownloadStatusListener = new MyDownloadStatusListener();
    private ThinDownloadManager downloadManager;
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
    private SweetAlertDialog pDialog;
    private List<Vocabulary> vc;
    private BaseAdapter adapter;
    int listposition,downloadId2;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private String video2;
    private Uri downloadUri2,destinationUri2;
    File vid2,speakdirectory;
    private DownloadRequest downloadRequest2;
    Vocabulary  vocName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_vocabulary);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                getDownloadVocabularyRefresh();
            }
        });

        downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pDialog.setTitleText("กำลังดาวน์โหลด");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(true);

        speakdirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "speak");
        speakdirectory.mkdirs();


        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                vocName = (Vocabulary) parent.getItemAtPosition(position);
                listposition = position;
                voc_name = vocName.getVoc_name();
                vid_name = vocName.getVid_name();
                video2 = ("https://talktodeafphp-talktodeaf.rhcloud.com/talktodeaf_web/speak_video/" + vid_name+"");

                downloadUri2 = Uri.parse(video2);

                destinationUri2 = Uri.parse(speakdirectory+"/"+vid_name+".mp4");

                vid2 = new File(speakdirectory+"/"+vid_name+".mp4");

                if(!vid2.exists()){

                    downloadRequest2 = new DownloadRequest(downloadUri2)
                            .setDestinationURI(destinationUri2).setPriority(DownloadRequest.Priority.HIGH)
                            .setDownloadListener(myDownloadStatusListener);

                    downloadId2 = downloadManager.add(downloadRequest2);
                    pDialog.show();

                }
                else{
                    Toast.makeText(SpeakVocabularyDownload.this,"คุณได้ดาวน์โหลดแล้ว",Toast.LENGTH_SHORT).show();
                }

            }
        });
        getDownloadVocabulary();
    }

    private void getDownloadVocabularyRefresh() {
        final GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = SpeakCategoryDownload.getCat_name();
        Log.d("Category Name", "" + cat_name);
        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {

                vc = voc;

                adapter = new DownloadSpeakListAdapter(SpeakVocabularyDownload.this,vc);

                listView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakVocabularyDownload.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                SpeakVocabularyDownload.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }

    private void getDownloadVocabulary() {
        final GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        cat_name = SpeakCategoryDownload.getCat_name();
        Log.d("Category Name", "" + cat_name);
        retrofit.getVocabularyByMethodWithCallback(cat_name,new Callback<List<Vocabulary>>() {
            @Override
            public void success(List<Vocabulary> voc, Response response) {

                vc = voc;

                adapter = new DownloadSpeakListAdapter(SpeakVocabularyDownload.this,vc);

                listView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SpeakVocabularyDownload.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getVoc_name() {
        return voc_name;
    }


    private class MyDownloadStatusListener implements DownloadStatusListener {
        @Override
        public void onDownloadComplete(int id) {
            pDialog.dismiss();
            View view = getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
            TextView title = (TextView)view.findViewById(R.id.title);
            title.setText("วิดีโอฝึกพูด "+voc_name);
            final Crouton crouton;
            crouton = Crouton.make(SpeakVocabularyDownload.this, view);
            crouton.show();
            getDownloadVocabulary();


        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            pDialog.dismiss();
            View view = getLayoutInflater().inflate(R.layout.crouton_custom_error_view, null);
            final Crouton crouton;
            crouton = Crouton.make(SpeakVocabularyDownload.this, view);
            crouton.show();

        }

        @Override
        public void onProgress(int id, long totalBytes, int progress) {
        }
    }

}



