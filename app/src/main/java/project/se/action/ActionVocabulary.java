package project.se.action;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import project.se.model.Vocabulary;
import project.se.rest.ApiService;
import project.se.talktodeaf.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class ActionVocabulary extends ActionBarActivity {
    private ListView listView;
    public static String voc_name;
    public static String cat_name=ActionCategory.cat_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_vocabulary);
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
        GsonBuilder builder = new GsonBuilder();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://talktodeafphp-talktodeaf.rhcloud.com")
                .setConverter(new GsonConverter(builder.create()))
                .build();
        ApiService retrofit = restAdapter.create(ApiService.class);
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
                Toast.makeText(ActionVocabulary.this,
                        "Connect Failure Please Try Again",
                        Toast.LENGTH_LONG).show();
            }
        });
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
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder  holder;
            LayoutInflater inflater = getLayoutInflater();

            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_action_vocabulary_column, parent,false);
                holder = new ViewHolder();
                holder.position=(FlatTextView)convertView.findViewById(R.id.position);
                holder.vocName=(FlatTextView)convertView.findViewById(R.id.vocName);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            Vocabulary bk = Vocabulary.get(position);
            holder.position.setText(""+(position+1));
            holder.vocName.setText("" + bk.getVoc_name());

            return convertView;
        }
    }
}
