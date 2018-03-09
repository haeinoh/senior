package hi.simpleexpensemanager;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends FragmentActivity {

    private TodayFragment todayFragment;
    private WeekFragment weekFragment;
    private MonthFragment monthFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayFragment = new TodayFragment();
        weekFragment = new WeekFragment();
        monthFragment = new MonthFragment();
        settingFragment = new SettingFragment();

        initFragment();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(tabId == R.id.menu_home){
                    transaction.replace(R.id.contentContainer, todayFragment).commit();
                }
                else if(tabId == R.id.menu_week){
                    transaction.replace(R.id.contentContainer, weekFragment).commit();
                }
                else if(tabId == R.id.menu_month){
                    transaction.replace(R.id.contentContainer, monthFragment).commit();
                }
                else if(tabId == R.id.menu_setting){
                    transaction.replace(R.id.contentContainer, settingFragment).commit();
                }
            }
        });
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, todayFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private long lastTimeBackPressed;

    @Override
    public void onBackPressed()
    {   //clicke once, and click again within 1.5s
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this,"if you push the back button again, it will terminated", Toast.LENGTH_SHORT);
        lastTimeBackPressed = System.currentTimeMillis();
    }

}
