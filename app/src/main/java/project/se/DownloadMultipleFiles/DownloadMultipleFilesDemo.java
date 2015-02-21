package project.se.DownloadMultipleFiles;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
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



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import project.se.talktodeaf.R;

public class DownloadMultipleFilesDemo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_multiple_files_demo);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_multiple_files_demo, menu);
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

        private ListView lstView;
        private ImageAdapter imageAdapter;
        private Handler handler = new Handler();
        public static final int DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS = 0;
        private ProgressDialog mProgressDialog;

        ArrayList<HashMap<String, Object>> MyArrList = new ArrayList<HashMap<String, Object>>();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_download_multiple_files_demo, container, false);


            new LoadContentFromServer().execute();

            return rootView;
        }


        public void ShowThumbnailData()
        {
            // ListView and imageAdapter
            lstView = (ListView) getActivity().findViewById(R.id.listView1);
            lstView.setClipToPadding(false);
            imageAdapter = new ImageAdapter(getActivity());
            lstView.setAdapter(imageAdapter);
        }

        public void startDownload(final int position) {

            Runnable runnable = new Runnable() {
                int Status = 0;

                public void run() {

                    String urlDownload = MyArrList.get(position).get("ImagePathFull").toString();
                    int count = 0;
                    try {

                        URL url = new URL(urlDownload);
                        URLConnection conexion = url.openConnection();
                        conexion.connect();

                        int lenghtOfFile = conexion.getContentLength();
                        Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                        InputStream input = new BufferedInputStream(url.openStream());

                        // Get File Name from URL
                        String fileName = urlDownload.substring(urlDownload.lastIndexOf('/')+1, urlDownload.length() );

                        OutputStream output = new FileOutputStream("/mnt/sdcard/mydata/"+fileName);

                        byte data[] = new byte[1024];
                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            Status = (int)((total*100)/lenghtOfFile);
                            output.write(data, 0, count);

                            // Update ProgressBar
                            handler.post(new Runnable() {
                                public void run() {
                                    updateStatus(position,Status);
                                }
                            });

                        }

                        output.flush();
                        output.close();
                        input.close();

                    } catch (Exception e) {}


                }
            };
            new Thread(runnable).start();
        }// end of method startDownload()

        private void updateStatus(int index,int Status){

            View v = lstView.getChildAt(index - lstView.getFirstVisiblePosition());

            // Update ProgressBar
            ProgressBar progress = (ProgressBar)v.findViewById(R.id.progressBar);
            progress.setProgress(Status);

            // Update Text to ColStatus
            TextView txtStatus = (TextView)v.findViewById(R.id.ColStatus);
            txtStatus.setPadding(10, 0, 0, 0);
            txtStatus.setText("Load : " + String.valueOf(Status)+"%");

            // Enabled Button View
            if(Status >= 100)
            {
                Button btnView = (Button)v.findViewById(R.id.btnView);
                btnView.setTextColor(Color.RED);
                btnView.setEnabled(true);
            }
        }// end of method updateStatus()

        class LoadContentFromServer extends AsyncTask<Object, Integer, Object> {

            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Downloading Thumbnail.....");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            }

            @Override
            protected Object doInBackground(Object... params) {

                HashMap<String, Object> map;
                Bitmap bm;

                map = new HashMap<String, Object>();
                map.put("ImageID", "1");
                map.put("ImageName", "Image 1");
                map.put("ImagePathThum", "http://talktodeafphp-talktodeaf.rhcloud.com/image/DrPrakai.jpg");
                bm = loadBitmap("http://talktodeafphp-talktodeaf.rhcloud.com/image/DrPrakai.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://talktodeafphp-talktodeaf.rhcloud.com/image/DrPrakai.jpg");
                MyArrList.add(map);

                map = new HashMap<String, Object>();
                map.put("ImageID", "2");
                map.put("ImageName", "Image 2");
                map.put("ImagePathThum", "http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                bm = loadBitmap("http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                MyArrList.add(map);

                map = new HashMap<String, Object>();
                map.put("ImageID", "3");
                map.put("ImageName", "Image 3");
                map.put("ImagePathThum", "http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                bm = loadBitmap("http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://talktodeafphp-talktodeaf.rhcloud.com/image/fornrisaya.jpg");
                MyArrList.add(map);

                map = new HashMap<String, Object>();
                map.put("ImageID", "4");
                map.put("ImageName", "Image 4");
                map.put("ImagePathThum", "http://www.thaicreate.com/android/img4_thum.jpg");
                bm = loadBitmap("http://www.thaicreate.com/android/img4_thum.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://www.thaicreate.com/android/img4_full.jpg");
                MyArrList.add(map);

                map = new HashMap<String, Object>();
                map.put("ImageID", "5");
                map.put("ImageName", "Image 5");
                map.put("ImagePathThum", "http://www.thaicreate.com/android/img5_thum.jpg");
                bm = loadBitmap("http://www.thaicreate.com/android/img5_thum.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://www.thaicreate.com/android/img5_full.jpg");
                MyArrList.add(map);

                map = new HashMap<String, Object>();
                map.put("ImageID", "6");
                map.put("ImageName", "Image 6");
                map.put("ImagePathThum", "http://www.thaicreate.com/android/img6_thum.jpg");
                bm = loadBitmap("http://www.thaicreate.com/android/img6_thum.jpg");
                map.put("ImageThumBitmap", bm);
                map.put("ImagePathFull", "http://www.thaicreate.com/android/img6_full.jpg");
                MyArrList.add(map);

                return null;
            }


            @Override
            protected void onPostExecute(Object result) {
                ShowThumbnailData();
                mProgressDialog.dismiss();
                //removeDialog(DIALOG_DOWNLOAD_THUMBNAIL_PROGRESS);
            }
        }


        class ImageAdapter extends BaseAdapter {

            private Context mContext;

            public ImageAdapter(Context context) {
                mContext = context;
            }

            public int getCount() {
                return MyArrList.size();
            }

            public Object getItem(int position) {
                return MyArrList.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.activity_column, null);
                }

                // ColImage
                ImageView imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
                imageView.getLayoutParams().height = 110;
                imageView.getLayoutParams().width = 110;
                imageView.setPadding(10, 10, 10, 10);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try
                {
                    imageView.setImageBitmap((Bitmap)MyArrList.get(position).get("ImageThumBitmap"));
                } catch (Exception e) {
                    // When Error
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }

                // ColImgID
                TextView txtImgID = (TextView) convertView.findViewById(R.id.ColImgID);
                txtImgID.setPadding(10, 0, 0, 0);
                txtImgID.setText("ID : " + MyArrList.get(position).get("ImageID").toString());

                // ColImgName
                TextView txtPicName = (TextView) convertView.findViewById(R.id.ColImgName);
                txtPicName.setPadding(10, 0, 0, 0);
                txtPicName.setText("Name : " + MyArrList.get(position).get("ImageName").toString());

                // ColStatus
                TextView txtStatus = (TextView) convertView.findViewById(R.id.ColStatus);
                txtStatus.setPadding(10, 0, 0, 0);
                txtStatus.setText("...");

                //btnDownload
                final Button btnDownload = (Button) convertView.findViewById(R.id.btnDownload);
                btnDownload.setTextColor(Color.RED);
                btnDownload.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Download
                        btnDownload.setEnabled(false);
                        btnDownload.setTextColor(Color.GRAY);

                        startDownload(position);
                    }
                });

                //btnView
                Button btnView = (Button) convertView.findViewById(R.id.btnView);
                btnView.setEnabled(false);
                btnView.setTextColor(Color.GRAY);
                btnView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewImageSDCard(position);
                    }
                });

                // progressBar
                ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.progressBar);
                progress.setPadding(10, 0, 0, 0);

                return convertView;

            }

        }



        // View Image from SD Card
        public void ViewImageSDCard(int position) {
            final AlertDialog.Builder imageDialog = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                    (ViewGroup) getActivity().findViewById(R.id.layout_root));
            ImageView image = (ImageView) layout.findViewById(R.id.fullimage);

            String urlDownload = MyArrList.get(position).get("ImagePathFull").toString();

            // Get File Name from URL
            String fileName = urlDownload.substring(urlDownload.lastIndexOf('/')+1, urlDownload.length() );

            String strPath = "/mnt/sdcard/mydata/" + fileName;
            Bitmap bm = BitmapFactory.decodeFile(strPath); // Path from SDCard
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            image.setImageBitmap(bm);

            String strName = MyArrList.get(position).get("ImageName").toString();
            imageDialog.setIcon(android.R.drawable.btn_star_big_on);
            imageDialog.setTitle("View : " + strName);
            imageDialog.setView(layout);
            imageDialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            imageDialog.create();
            imageDialog.show();
        }


        /***** Get Image Resource from URL (Start) *****/
        private static final String TAG = "Image";
        private static final int IO_BUFFER_SIZE = 4 * 1024;
        public static Bitmap loadBitmap(String url) {
            Bitmap bitmap = null;
            InputStream in = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                copy(in, out);
                out.flush();

                final byte[] data = dataStream.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 1;

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
            } catch (IOException e) {
                Log.e(TAG, "Could not load Bitmap from: " + url);
            } finally {
                closeStream(in);
                closeStream(out);
            }

            return bitmap;
        }


        private static void closeStream(Closeable stream) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    android.util.Log.e(TAG, "Could not close stream", e);
                }
            }
        }

        private static void copy(InputStream in, OutputStream out) throws IOException {
            byte[] b = new byte[IO_BUFFER_SIZE];
            int read;
            while ((read = in.read(b)) != -1) {
                out.write(b, 0, read);
            }
        }
        /***** Get Image Resource from URL (End) *****/


    }


}
