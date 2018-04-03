package hi.simpleexpensemanager;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

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

public class IncomeDetails extends AppCompatActivity {


    private ListView incomeListView;
    private IncomeListAdapter adapter;
    private List<Income> incomeList;
    private static final String TAG_TOINCOME = "incomeAmount";
    TextView totalIncomeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_details);

        incomeListView = (ListView) findViewById(R.id.incomeListView);
        incomeList = new ArrayList<Income>();

        adapter = new IncomeListAdapter(getApplicationContext(), incomeList);
        incomeListView.setAdapter(adapter);

        totalIncomeValue = (TextView) findViewById(R.id.totalIncomeValue);

        //access database
        new BackgroundTask().execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String incomeTarget;

        @Override
        protected void onPreExecute()
        {
            incomeTarget = "http://greenohi.cafe24.com/IncomeList.php";
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

        @Override //result
        public void onPostExecute(String result){
            double sumIncome = 0.00;
            try{
                incomeList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String incomeName, incomeAmount, incomeCategory, incomeDate;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    incomeName = object.getString("incomeName");
                    incomeAmount = object.getString("incomeAmount");
                    incomeCategory = object.getString("incomeCategory");
                    incomeDate = object.getString("incomeDate");
                    Income income = new Income(incomeName, incomeAmount, incomeCategory, incomeDate);
                    incomeList.add(income);

                    sumIncome = sumIncome + Double.parseDouble(incomeAmount);
                    count++;

                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //display total Income
            //DecimalFormat df = new DecimalFormat("#.##");
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            totalIncomeValue.setText(String.valueOf(df.format(sumIncome)));
        }
    }

}
