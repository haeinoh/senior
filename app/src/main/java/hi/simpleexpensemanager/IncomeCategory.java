package hi.simpleexpensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class IncomeCategory extends AppCompatActivity {

    List<String> category;
    ArrayAdapter<String> adapter;
    ListView incomeCategoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_category);
/*
        //From arrays.xml, get called "income" and store into String[] array
        final String[] income = getResources().getStringArray(R.array.income);

        //Set into ArrayAdapter
        //simple_list_item_1 is stored textview (xml)
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, income);
        final ArrayList<String> items = new ArrayList<String>();

        //From resource, get Listview and store it into adapter
        ListView incomeCategoryView = (ListView) findViewById(R.id.incomeCategoryView);
        incomeCategoryView.setAdapter(adapter);
        //choice mode single
        incomeCategoryView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
*/
        category = new ArrayList<String>();
        category.add("General Income");
        category.add("Salary");
        category.add("Investment");
        category.add("Business");
        category.add("Asset Withdrawal");
        category.add("Carried Over");
        category.add("Other");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, category);
        incomeCategoryView = (ListView) findViewById(R.id.incomeCategoryView);
        incomeCategoryView.setAdapter(adapter);
        incomeCategoryView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        incomeCategoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast - print the data
                String item = category.get(i);

                Toast.makeText(IncomeCategory.this, item, Toast.LENGTH_LONG).show();
            }
        });

        //add Catgeory
        final Button addCategoryButton = (Button) findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                EditText newCategory = (EditText) findViewById(R.id.newCategory);

                String item = newCategory.getText().toString();
                if(item != null || item.trim().length() > 0){
                    category.add(item.trim());
                    adapter.notifyDataSetChanged();
                    newCategory.setText("");
                }
            }
        });

        //Delete Category
        Button deleteCategoryButton = (Button) findViewById(R.id.deleteCategoryButton);
        deleteCategoryButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {

                int position = incomeCategoryView.getCheckedItemPosition();

                if(position >= 0 && position < category.size()){
                    category.remove(position);
                    incomeCategoryView.clearChoices();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
