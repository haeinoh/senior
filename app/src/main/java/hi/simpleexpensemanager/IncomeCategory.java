package hi.simpleexpensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class IncomeCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_category);

        //From arrays.xml, get called "income" and store into String[] array
        String[] income = getResources().getStringArray(R.array.income);

        //Set into ArrayAdapter
        //simple_list_item_1 is stored textview (xml)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, income);

        //From resource, get Listview and store it into adapter
        ListView incomeCategoryView = (ListView) findViewById(R.id.incomeCategoryView);
        incomeCategoryView.setAdapter(adapter);
    }
}
