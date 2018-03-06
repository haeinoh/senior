package hi.simpleexpensemanager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button todayButton = (Button) findViewById(R.id.todayButton);
        final Button weekButton = (Button) findViewById(R.id.weekButton);
        final Button monthButton = (Button) findViewById(R.id.monthButton);
        final LinearLayout transaction = (LinearLayout) findViewById(R.id.transaction);

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.setVisibility(View.GONE);
                todayButton.setBackgroundColor(getResources().getColor(R.color.colorMenuDark));
                weekButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                monthButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new TodayFragment());
                fragmentTransaction.commit();
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.setVisibility(View.GONE);
                todayButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                weekButton.setBackgroundColor(getResources().getColor(R.color.colorMenuDark));
                monthButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new WeekFragment());
                fragmentTransaction.commit();
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.setVisibility(View.GONE);
                todayButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                weekButton.setBackgroundColor(getResources().getColor(R.color.colorMenuFragment));
                monthButton.setBackgroundColor(getResources().getColor(R.color.colorMenuDark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new MonthFragment());
                fragmentTransaction.commit();
            }
        });
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
