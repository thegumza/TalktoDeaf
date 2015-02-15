package project.se.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.google.gson.GsonBuilder;

import java.util.List;

import project.se.model.Category;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionCategory extends ActionBarActivity {
    ListView listCategory;
    public static String cat_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_category);
        listCategory = (ListView)findViewById(R.id.listView);
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category catName = (Category) parent.getItemAtPosition(position);
                cat_name = catName.getCat_name();
                Intent detail = new Intent(ActionCategory.this, ActionVocabulary.class);
                startActivity(detail);
            }
        });

        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
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
                listCategory.setAdapter(new CategoryListAdapter(ep));

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ActionCategory.this, "Connection fail please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CategoryListAdapter extends BaseAdapter {

        List<Category> Category;
        public CategoryListAdapter(List<Category> ct) {
            Category = ct;
        }
        @Override
        public int getCount() {
            return Category.size();
        }

        @Override
        public Object getItem(int position) {
            return Category.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            FlatTextView catName;
            FlatTextView position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder  holder;
            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_action_category_column, parent,false);
                holder = new ViewHolder();
                holder.position=(FlatTextView)convertView.findViewById(R.id.position);
                holder.catName=(FlatTextView)convertView.findViewById(R.id.catName);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Category ct = Category.get(position);
            holder.position.setText(""+(position+1));
            holder.catName.setText("" + ct.getCat_name());

            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_category, menu);
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
