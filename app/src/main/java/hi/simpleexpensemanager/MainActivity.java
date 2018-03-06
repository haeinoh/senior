package hi.simpleexpensemanager;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent weekIntent = new Intent(MainActivity.this, WeekActivity.class);
                MainActivity.this.startActivity(weekIntent);
            }
        });

    }

    /*
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
    }*/

}
