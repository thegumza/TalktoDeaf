package project.se.game.wordgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.astuetz.PagerSlidingTabStrip;
import com.cengalabs.flatui.views.FlatButton;

import project.se.game.wordgame.Detail.GameNo1;
import project.se.game.wordgame.Detail.GameNo2;
import project.se.game.wordgame.Detail.GameNo3;
import project.se.game.wordgame.Detail.GameNo4;
import project.se.game.wordgame.Detail.GameNo5;
import project.se.talktodeaf.R;

public class WordGame extends FragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private FlatButton btnPrev,btnNext,btnAns;
    //List<String> answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        btnPrev = (FlatButton) findViewById(R.id.btn_prev);
        btnNext = (FlatButton) findViewById(R.id.btn_next);
        btnAns = (FlatButton) findViewById(R.id.btn_answer);
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        Typeface font = Typeface.createFromAsset(WordGame.this.getAssets(), "fonts/ThaiSansNeue_regular.ttf");
        tabs.setViewPager(pager);
        tabs.setTypeface(font, 20);
        tabs.setTextSize(50);
        //btnPrev.setVisibility(View.INVISIBLE);
        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pager.setCurrentItem(getItem(-1), true); //getItem(-1) for previous
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous
            }
        });
        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //answer.add(GameNo1.getAnswer());
                SharedPreferences sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                Toast.makeText(WordGame.this,""+(sp.getString("Answer1","No Select")),Toast.LENGTH_SHORT).show();
            }
        });


        /*btnAns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MaterialDialog dialog = new MaterialDialog.Builder(WordGame.this)
                        .title("เฉลยคำตอบ")
                        .adapter(new ButtonItemAdapter(this, R.array.socialNetworks))
                        .build();

                ListView listView = dialog.getListView();
                if (listView != null) {
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(WordGame.this, "Clicked item " + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                dialog.show();
            }
        });*/
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(pager.getCurrentItem() == 0)
                {
                    btnAns.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                if(pager.getCurrentItem() == 1)
                {
                    btnAns.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                if(pager.getCurrentItem() == 2)
                {
                    btnAns.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                if(pager.getCurrentItem() == 3)
                {
                    btnAns.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                if(pager.getCurrentItem() == 4)
                {
                    btnAns.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private int getItem(int i) {

        return pager.getCurrentItem() + i;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "ข้อ 1","ข้อ 2", "ข้อ 3", "ข้อ 4", "ข้อ 5"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(final int position) {
            switch(position) {

                case 0:
                    return GameNo1.newInstance(position);

                case 1:
                    return GameNo2.newInstance(position);
                case 2:
                    return GameNo3.newInstance(position);
                case 3:
                    return GameNo4.newInstance(position);
                case 4:
                    return GameNo5.newInstance(position);
                default:
                    return GameNo1.newInstance(position);
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        AlertDialogWrapper.Builder dialogBuilder = new AlertDialogWrapper.Builder(WordGame.this);
        dialogBuilder.setMessage("คุณต้องการออกจากเกมใช่หรือไม่");
        dialogBuilder.setTitle("ออกจากเกม");
        dialogBuilder.setIcon(R.drawable.ic_warning_amber_36dp);
        dialogBuilder.setPositiveButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        dialogBuilder.create().show();
    }
}
