package project.se.information.book;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import java.util.List;

import project.se.model.Book;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class BookInfo extends ActionBarActivity {
    private ListView listView;
    public static String book_name;
    private SwipyRefreshLayout mSwipyRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                getBookInfoRefresh();

            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book bookName = (Book) parent.getItemAtPosition(position);
                book_name = bookName.getBook_name();
                Intent bookDetail = new Intent(BookInfo.this, BookDetail.class);
                startActivity(bookDetail);
            }
        });
        getBookInfo();
    }
    private void getBookInfoRefresh() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getBookInfoByMethodWithCallback(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> book, Response response) {
                // accecss the items from you shop list here

                List<Book> ep = book;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new BookListAdapter(ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(BookInfo.this,
                        "Connect Failure Please Try Again",
                        Toast.LENGTH_LONG).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                BookInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
    private void getBookInfo() {
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
        retrofit.getBookInfoByMethodWithCallback(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> book, Response response) {
                // accecss the items from you shop list here

                List<Book> ep = book;
                /*Example[] array = ep.toArray(new Example[ep.size()]);
                List<Example> listsample = ep.getSaleDate();*/
                listView.setAdapter(new BookListAdapter(ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(BookInfo.this,
                        "Connect Failure Please Try Again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getBook_name() {
        return book_name;
    }

    public class BookListAdapter extends BaseAdapter {

        List<Book> Book;

        public BookListAdapter(List<Book> sd) {
            Book = sd;
        }

        @Override
        public int getCount() {
            return Book.size();
        }

        @Override
        public Object getItem(int position) {
            return Book.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            FlatTextView bookName;
            FlatTextView position;
            ImageView image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_book_info_column, parent, false);
                holder = new ViewHolder();
                holder.position = (FlatTextView) convertView.findViewById(R.id.position);
                holder.bookName = (FlatTextView) convertView.findViewById(R.id.bookName);
                holder.image = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Book bk = Book.get(position);
            holder.position.setText("" + (position + 1));
            holder.bookName.setText("" + bk.getBook_name());
            Picasso.with(BookInfo.this).load("http://talktodeafphp-talktodeaf.rhcloud.com/talktodeaf_web/images/" + bk.getBook_image()).resize(100, 150).into(holder.image);

            return convertView;
        }
    }
}