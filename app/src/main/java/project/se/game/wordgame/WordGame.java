package project.se.game.wordgame;

import android.content.Context;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.cengalabs.flatui.views.FlatButton;

import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import project.se.game.wordgame.Detail.GameNo1;
import project.se.game.wordgame.Detail.GameNo2;
import project.se.game.wordgame.Detail.GameNo3;
import project.se.game.wordgame.Detail.GameNo4;
import project.se.talktodeaf.R;

public class WordGame extends FragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private FlatButton btnPrev,btnNext,btnAns;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int correctanswer = 0,wronganswer = 0;
    ArrayList<String> correctList,wrongList;
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
        pager.setOffscreenPageLimit(4);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        Typeface font = Typeface.createFromAsset(WordGame.this.getAssets(), "fonts/ThaiSansNeue_regular.ttf");
        tabs.setViewPager(pager);
        tabs.setTypeface(font, 20);
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

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

                correctList = new ArrayList<String>();
                wrongList = new ArrayList<String>();

                correctList.add(sp.getString("Answer1",""));
                correctList.add(sp.getString("Answer2", ""));
                correctList.add(sp.getString("Answer3", ""));
                correctList.add(sp.getString("Answer4", ""));
                //correctList.add(sp.getString("Answer5", ""));


                correctList.removeAll(Arrays.asList("", null));

                String[] arrCorrect = correctList.toArray(new String[correctList.size()]);
                String[] arrWrong = wrongList.toArray(new String[wrongList.size()]);

                if(correctList.isEmpty()) {
                    MaterialDialog dialog = new MaterialDialog.Builder(WordGame.this)
                            .title("คุณตอบถูก " + 0 + " ข้อ")
                            .iconRes(R.drawable.ic_question_answer_black_36dp)
                            .build();
                            dialog.show();
                    correctList.clear();
                }
                else {
                    MaterialDialog dialog = new MaterialDialog.Builder(WordGame.this)
                            .title("คุณตอบถูก " + correctList.size() + " ข้อ")
                            .iconRes(R.drawable.ic_question_answer_black_36dp)
                            .items(arrCorrect)
                            .build();
                    dialog.show();
                    correctList.clear();
                }

                /*Log.d("Check Answer",""+GameNo1.getCorrect()+", "+GameNo2.getCorrect()+", "+GameNo3.getCorrect()+", "+GameNo4.getCorrect()+", "+GameNo5.getCorrect());
                answerList = new ArrayList<String>();

                answerList.add(GameNo1.getCorrect());
                answerList.add(GameNo2.getCorrect());
                answerList.add(GameNo3.getCorrect());
                answerList.add(GameNo4.getCorrect());
                answerList.add(GameNo5.getCorrect());

                answerList.remove(sp.getString("Answer1",""));
                answerList.remove(sp.getString("Answer2",""));
                answerList.remove(sp.getString("Answer3",""));
                answerList.remove(sp.getString("Answer4",""));
                answerList.remove(sp.getString("Answer5",""));

                Toast.makeText(WordGame.this, "คุณตอบถูก " + answerList.size()+" ข้อ\nคุณตอบผิด "+wronganswer+" ข้อ", Toast.LENGTH_SHORT).show();*/
            }
        });


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
                    btnAns.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);

                    /*btnAns.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);*/
                }
                /*if(pager.getCurrentItem() == 4)
                {
                    btnAns.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                }*/
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

        private final String[] TITLES = { "ข้อ 1","ข้อ 2", "ข้อ 3", "ข้อ 4"/*,"ข้อ 5"*/};

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
                /*case 4:
                    return GameNo5.newInstance(position);*/
                default:
                    return GameNo1.newInstance(position);
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("ออกจากเกมส์")
                .setContentText("คุณต้องการออกจากเกมส์ใช่หรือไม่?")
                .setConfirmText("ใช่")
                .setCancelText("ไม่")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .show();
    }


}
