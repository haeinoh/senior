package hi.simpleexpensemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExpenseCategory extends AppCompatActivity {


    List<String> category;
    ArrayAdapter<String> adapter;
    ListView expenseCategoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_category);

/*
        //From arrays.xml, get called "income" and store into String[] array
        String[] expense = getResources().getStringArray(R.array.expense);

        //Set into ArrayAdapter
        //simple_list_item_1 is stored textview (xml)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, expense);

        //From resource, get Listview and store it into adapter
        ListView expenseCategoryView = (ListView) findViewById(R.id.expenseCategoryView);
        expenseCategoryView.setAdapter(adapter);
    */

        category = new ArrayList<String>();
        category.add("General Expense");
        category.add("Food");
        category.add("Clothing");
        category.add("Necessaries");
        category.add("Transportation");
        category.add("Education");
        category.add("Home");
        category.add("Utility Bills");
        category.add("Enteretainment");
        category.add("Insurance");
        category.add("Phone");
        category.add("Electronics");
        category.add("Beauty");
        category.add("Savings");
        category.add("Carried Over");
        category.add("Other");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, category);
        expenseCategoryView = (ListView) findViewById(R.id.expenseCategoryView);
        expenseCategoryView.setAdapter(adapter);
        expenseCategoryView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        expenseCategoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast - print the data
                String item = category.get(i);

                Toast.makeText(ExpenseCategory.this, item, Toast.LENGTH_LONG).show();
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

                int position = expenseCategoryView.getCheckedItemPosition();

                if(position >= 0 && position < category.size()){
                    category.remove(position);
                    expenseCategoryView.clearChoices();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
