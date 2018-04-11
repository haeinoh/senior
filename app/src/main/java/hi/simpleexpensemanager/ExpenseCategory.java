package hi.simpleexpensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExpenseCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_category);


        //From arrays.xml, get called "income" and store into String[] array
        String[] expense = getResources().getStringArray(R.array.expense);

        //Set into ArrayAdapter
        //simple_list_item_1 is stored textview (xml)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, expense);

        //From resource, get Listview and store it into adapter
        ListView expenseCategoryView = (ListView) findViewById(R.id.expenseCategoryView);
        expenseCategoryView.setAdapter(adapter);
    }
}
