package project.se.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cengalabs.flatui.views.FlatTextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import project.se.model.Vocabulary;
import project.se.talktodeaf.R;

/**
 * Created by wiwat on 3/16/2015.
 */
public class VocabularyListAdapter extends BaseAdapter {
    Context context;
    List<project.se.model.Vocabulary> Vocabulary;
    //final File actiondirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "action");
    public VocabularyListAdapter(Context context,List<Vocabulary> sd) {
        Vocabulary = sd;
        this.context = context;
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
        ImageView downloadView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.activity_action_vocabulary_column, parent,false);
            holder = new ViewHolder();
            holder.position=(FlatTextView)convertView.findViewById(R.id.position);
            holder.imageview=(ImageView)convertView.findViewById(R.id.imageView);
            holder.vocName=(FlatTextView)convertView.findViewById(R.id.vocName);
            holder.downloadView=(ImageView)convertView.findViewById(R.id.downloadView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        Vocabulary bk = Vocabulary.get(position);
        String FirstVoc = bk.getVoc_name().substring(0, 1);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/ThaiSansNeue_regular.ttf");
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(type)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound("" + FirstVoc, Color.DKGRAY);
        NumberFormat f = new DecimalFormat("00");
        String vidname = bk.getVid_name();
        //String video = ("http://talktodeafphp-talktodeaf.rhcloud.com/video/" + vidname+".mp4");

        /*File vid1 = new File(actiondirectory+"/"+vidname+".mp4");
        if(!vid1.exists()) {*/
            holder.position.setText(""+f.format(position + 1));
            holder.vocName.setText("" + bk.getVoc_name());
            holder.imageview.setImageDrawable(drawable);/*
            holder.downloadView.setImageResource(R.drawable.ic_download_grey);
        }
        else{
            holder.position.setText(""+f.format(position + 1));
            holder.vocName.setText("" + bk.getVoc_name());
            holder.imageview.setImageDrawable(drawable);
            holder.downloadView.setImageResource(R.drawable.ic_downloaded_green);
        }*/
        return convertView;
    }
}