package hi.simpleexpensemanager;

import android.graphics.Color;
import android.os.AsyncTask;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
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
                   // IncomeActivity.adapter.add(item.trim());
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

       // new IncomeCategorySync().execute();
    }
/*
    class IncomeCategorySync extends AsyncTask<Void,Void, String>
    {
        String incomeTarget;

        @Override
        protected void onPreExecute() {
            incomeTarget = "http://greenohi.cafe24.com/IncomeArray.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(incomeTarget);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //save result
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //read buffer one by one and store into temp (string)
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> categoryList = new ArrayList<String>();
            double sumExpense = 0.0;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String categoryName;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    categoryName = object.getString("categoryName");
                    CategoryName categoryList = new CategoryName(categoryName);

                    category.add(new String(categoryName));

                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
*/
}
