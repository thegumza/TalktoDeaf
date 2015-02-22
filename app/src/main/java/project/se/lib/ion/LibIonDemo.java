package project.se.lib.ion;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import project.se.action.ActionCategory;
import project.se.model.Category;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class LibIonDemo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_ion_demo);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lib_ion_demo, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        Button download;
        TextView downloadCount;
        ProgressBar progressBar;

        String url = "http://talktodeafphp-talktodeaf.rhcloud.com";
        Future<File> downloading;
        ListView listView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_lib_ion_demo, container, false);

            // Enable global Ion logging
            Ion.getDefault(getActivity()).configure().setLogging("ion-sample", Log.DEBUG);

            download = (Button)rootView.findViewById(R.id.download);
            downloadCount = (TextView)rootView.findViewById(R.id.download_count);
            progressBar = (ProgressBar)rootView.findViewById(R.id.progress);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (downloading != null && !downloading.isCancelled()) {
                        resetDownload();
                        return;
                    }

                    download.setText("Cancel");
                    // this is a 180MB zip file to test with
                    downloading = Ion.with(getActivity())
                            .load("http://talktodeafphp-talktodeaf.rhcloud.com/action_video")
                                    // attach the percentage report to a progress bar.
                                    // can also attach to a ProgressDialog with progressDialog.
                            .progressBar(progressBar)
                                    // callbacks on progress can happen on the UI thread
                                    // via progressHandler. This is useful if you need to update a TextView.
                                    // Updates to TextViews MUST happen on the UI thread.
                            .progressHandler(new ProgressCallback() {
                                @Override
                                public void onProgress(long downloaded, long total) {
                                    downloadCount.setText("" + downloaded + " / " + total);
                                }
                            })
                                    // write to a file
                            .write(getActivity().getFileStreamPath("zip-" + System.currentTimeMillis()+"test"+ ".zip"))
                                    // run a callback on completion
                            .setCallback(new FutureCallback<File>() {
                                @Override
                                public void onCompleted(Exception e, File result) {
                                    resetDownload();
                                    if (e != null) {
                                        Toast.makeText(getActivity(), "Error downloading file", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Toast.makeText(getActivity(), "File upload complete", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });







            return rootView;
        }

        void resetDownload() {
            // cancel any pending download
            downloading.cancel();
            downloading = null;

            // reset the ui
            download.setText("Download");
            downloadCount.setText(null);
            progressBar.setProgress(0);
        }


    }
}
