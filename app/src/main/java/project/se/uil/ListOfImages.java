//package project.se.uil;
//
///**
// * Created by BKK on 18/2/2558.
// */
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
//public class ListOfImages extends Activity {
//    protected AbsListView list;
//    protected ImageLoader loader = ImageLoader.getInstance();
//
//    final Context context=this;
//    DisplayImageOptions op;
//    String [] images={"http://t2.gstatic.com/images?q=tbn:ANd9GcSZrajzoEXNlRWjMGE9L3kqI1EsFN9P5HCNhMo4xaqLkWuhAixo","http://t0.gstatic.com/images?q=tbn:ANd9GcQH7hisM_szjOKlVdQvq6m_J4lETkWxQOlAk3SMWs051TFFnmWMCA","http://3.bp.blogspot.com/-kAhN0HX-MBk/T_5bApfhbJI/AAAAAAAAAuI/lUww8xT9yV8/s1600/smileys_001_01.png"};
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.image_listview_layout);
//
//        op = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.drawable.ic_empty)
////                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory()
//                .cacheOnDisc()
//                .displayer(new RoundedBitmapDisplayer(20))
//                .build();
//
//        list = (ListView) findViewById(android.R.id.list);
//        ((ListView) list).setAdapter(new ItemAdapter());
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //tartImagePagerActivity(position);
//
//                Intent i=new Intent(context,SeperateView.class);
//                i.putExtra("pos",position+"");
//                startActivity(i);
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    class ItemAdapter extends BaseAdapter {
//
//        private class ViewHolder {
//            public TextView text;
//            public ImageView image;
//        }
//
//        @Override
//        public int getCount() {
//            return images.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View v = convertView;
//            final ViewHolder holder;
//            if (convertView == null) {
//                v = getLayoutInflater().inflate(R.layout.image_list_layout, parent, false);
//                holder = new ViewHolder();
//                holder.text = (TextView) v.findViewById(R.id.text);
//                holder.image = (ImageView) v.findViewById(R.id.image);
//                v.setTag(holder);
//            } else {
//                holder = (ViewHolder) v.getTag();
//            }
//
//            holder.text.setText("Item " + (position + 1));
//            loader.displayImage(images[position], holder.image, op, null);
//
//            return v;
//        }
//    }
//}
