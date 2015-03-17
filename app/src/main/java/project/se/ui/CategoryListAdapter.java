package project.se.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cengalabs.flatui.views.FlatTextView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import project.se.model.Category;
import project.se.talktodeaf.R;

/**
 * Created by wiwat on 3/16/2015.
 */
public class CategoryListAdapter extends BaseAdapter {
    Context context;
    List<project.se.model.Category> Category;
    final File actiondirectory = new File(Environment.getExternalStorageDirectory() +File.separator+ "action");
    public CategoryListAdapter(Context context,List<Category> ct) {
        Category = ct;
        this.context = context;
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
        ImageView imageview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.activity_action_category_column, parent,false);
            holder = new ViewHolder();
            holder.position=(FlatTextView)convertView.findViewById(R.id.position);
            holder.imageview=(ImageView)convertView.findViewById(R.id.imageView);
            holder.catName=(FlatTextView)convertView.findViewById(R.id.catName);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        Category ct = Category.get(position);
        String FirstCat = ct.getCat_name().substring(0, 1);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/ThaiSansNeue_regular.ttf");
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(type)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound("" + FirstCat, Color.DKGRAY);
        NumberFormat f = new DecimalFormat("00");
        holder.position.setText(""+f.format(position + 1));
        holder.catName.setText("" + ct.getCat_name());
        holder.imageview.setImageDrawable(drawable);
        return convertView;
    }
}
