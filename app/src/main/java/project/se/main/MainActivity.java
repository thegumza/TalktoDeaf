package project.se.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.mikepenz.aboutlibraries.Libs;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;

import project.se.action.ActionCategory;
import project.se.download.CategoryDownload;
import project.se.game.Game;
import project.se.information.Information;
import project.se.speak.SpeakCategory;
import project.se.talktodeaf.R;


public class MainActivity extends ActionBarActivity implements OnMenuItemClickListener,View.OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    FlatButton btnAction,btnSpeak,btnGame,btnInfo;
    private FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;
    ShowcaseView showcaseView;
    SharedPreferences prefs = null;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("project.se.talktodeaf", MODE_PRIVATE);

        ArrayList<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(new MenuObject(R.drawable.ic_highlight_remove_black_36dp,"ปิดหน้าต่าง"));
        menuObjects.add(new MenuObject(R.drawable.ic_settings_black_36dp, "ตั้งค่า"));
        menuObjects.add(new MenuObject(R.drawable.ic_file_download_black_24dp, "ดาว์นโหลด"));
        menuObjects.add(new MenuObject(R.drawable.ic_info_outline_black_36dp, "เกี่ยวกับ"));

        fragmentManager = getSupportFragmentManager();
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), menuObjects);
        //addFragment(new MainFragment(), true, R.id.container);
        btnAction = (FlatButton)findViewById(R.id.btn_action);
        btnSpeak = (FlatButton)findViewById(R.id.btn_speak);
        btnGame = (FlatButton)findViewById(R.id.btn_game);
        btnInfo = (FlatButton)findViewById(R.id.btn_info);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent action = new Intent(MainActivity.this, ActionCategory.class);
                startActivity(action);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speak = new Intent(MainActivity.this, SpeakCategory.class);
                startActivity(speak);
            }
        });
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainActivity.this, Game.class);
                startActivity(game);
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info = new Intent(MainActivity.this, Information.class);
                startActivity(info);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(new ViewTarget(findViewById(R.id.btn_action)))
                    .setContentTitle("ท่าทาง")
                    .setContentText("คุณสามารถฝึกท่าทางภาษามือจากปุ่มนี้")
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setOnClickListener(this)
                            //.setShowcaseEventListener(this)
                    .build();
            showcaseView.setButtonText("ต่อไป");

            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "กดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                mMenuDialogFragment.show(fragmentManager, "DropDownMenuFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View view, int i) {
        switch(i) {
            case 1:
                //Setting Here!!!
                break;
            case 2:
                Intent download = new Intent(MainActivity.this, CategoryDownload.class);
                startActivity(download);
                break;
            case 3:
                new Libs.Builder()
                        .withFields(R.string.class.getFields())
                        .withActivityTitle("About")
                        .withAboutAppName("พูดผ่านภาษามือ")
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withLicenseShown(true)
                        .withAboutVersionShownCode(true)
                        .withAboutDescription("แอพพลิเคชั่น TalktoDeaf พูดผ่านภาษามือ <br />พัฒนาโดยนักศึกษาวิศวกรรมซอฟต์แวร์<br /><b>มหาวิทยาลัยสงขลานครินทร์ วิทยาเขตภูเก็ต</b>")
                        .start(this);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (counter) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(btnSpeak), true);
                showcaseView.setContentTitle("การพูด");
                showcaseView.setContentText("คุณสามารถฝึกการพูดจากปุ่มนี้");
                showcaseView.setStyle(R.style.CustomShowcaseTheme2);
                break;

            case 1:
                showcaseView.setShowcase(new ViewTarget(btnInfo), true);
                showcaseView.setContentTitle("ข้อมูล");
                showcaseView.setContentText("ข้อมูลหนังสือเกี่ยวกับภาษามือและสถานที่");
                showcaseView.setStyle(R.style.CustomShowcaseTheme2);
                break;
            case 2:
                showcaseView.setShowcase(new ViewTarget(btnGame), true);
                showcaseView.setContentTitle("เกม");
                showcaseView.setContentText("มีเกมทายปริศนารอคนอยู่");
                showcaseView.setStyle(R.style.CustomShowcaseTheme2);
                break;
            case 3:
                showcaseView.setTarget(Target.NONE);
                showcaseView.setContentTitle("ขอบคุณ");
                showcaseView.setContentText("คุณสามารถเริ่มใช้งานแอพพลเคชั่นได้แล้ว");
                showcaseView.setButtonText("ปิด");
                setAlpha(0.4f, btnAction, btnSpeak, btnInfo,btnGame);
                break;

            case 4:
                showcaseView.hide();
                setAlpha(1.0f, btnAction, btnSpeak, btnInfo,btnGame);
                break;
        }
        counter++;
    }
    private void setAlpha(float alpha, View... views) {
         {
            for (View view : views) {
                view.setAlpha(alpha);
            }
        }
    }
}

