package hi.simpleexpensemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment /*implements BudgetDialog.OnInputSelected */{

    public TodayFragment() {
        // Required empty public constructor
    }

    private ListView expenseListView;
    private ExpenseListAdapter adapter2;
    private List<Expense> expenseList;
    private AlertDialog dialog;

    //private String budgetAmount;

    private static final String TAG = "TodayFragment";

    public void sendInput(String input){
        Log.d(TAG, "sendInput: from budgetsettng: " + input);
        //mBudgetValue.setText(input);
       // mBudgetValue.setText(input);
       // final String mbudgetvalue = mBudgetValue.getText().toString();
    }
    //public TextView mBudgetValue;
   //public TextView budgetValue;
    //public TextView budgetPrint;
    public TextView budgetTest;
    public TextView currentExpenseAmount;
    public TextView currentIncomeAmount;
    public TextView currentBalance;
    public TextView currentPercent;
   // public TextView result;
   // public EditText insertValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

        currentExpenseAmount = v.findViewById(R.id.currentExpenseAmount);
        currentIncomeAmount = v.findViewById(R.id.currentIncomeAmount);
        currentBalance = v.findViewById(R.id.currentBalance);
        currentPercent = v.findViewById(R.id.currentPercent);
        //budgetValue = v.findViewById(R.id.budgetValue);
        //budgetPrint = v.findViewById(R.id.budgetPrint);
        budgetTest = v.findViewById(R.id.budgetTest);

        //Calendar - do not remove it
        TextView yearLabel = (TextView)v.findViewById(R.id.yearLabel);
        TextView monthLabel = (TextView)v.findViewById(R.id.monthLabel);
        TextView dayLabel = (TextView)v.findViewById(R.id.dayLabel);
        TextView weekLabel = (TextView)v.findViewById(R.id.weekLabel);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/dd/E", Locale.US);
        String strDate = sdf.format(cal.getTime());

        String[] values = strDate.split("/",0);
        /*for(int i = 0; i < values.length; i++)
        {
            Log.v("CHECK_DATE", values[i]);
        }*/
        yearLabel.setText(values[0]);
        monthLabel.setText(values[1]);
        dayLabel.setText(values[2]);
        weekLabel.setText(values[3]);
        // the end of Calendar

    //expense list view
        expenseListView = (ListView) v.findViewById(R.id.expenseListView);
        expenseList = new ArrayList<Expense>();

        adapter2 = new ExpenseListAdapter(getContext().getApplicationContext(), expenseList);
        expenseListView.setAdapter(adapter2);

        //final TextView result = (TextView) v.findViewById(R.id.budgetValue);
        //final EditText insertValue = (EditText) v.findViewById(R.id.insertValue);

        //budgetSetting
        //mBudgetValue = v.findViewById(R.id.budgetValue);

        Button budgetSetting = (Button) v.findViewById(R.id.budgetSetting);
        budgetSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(result.getText().toString().equals("")){
                    String input = insertValue.getText().toString();
                    result.setText(input);
                }
                else{
                    result.setText("");
                }*/
               Log.d(TAG, "onClick: opening dialog");

                //final String budgetValue = mBudgetValue.getText().toString();

                BudgetDialog dialog = new BudgetDialog();
                dialog.setTargetFragment(TodayFragment.this, 1);
                dialog.show(getFragmentManager(), "BudgetDialog");

                //String budgetValue = mBudgetValue.getText().toString();
            }
        });

        Button incomeDetailsButton = (Button) v.findViewById(R.id.incomeDetailsButton);
        incomeDetailsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent incomeDetails = new Intent(getActivity(), IncomeDetails.class);
                startActivity(incomeDetails);
            }
        });

        Button addIncomeButton = (Button) v.findViewById(R.id.addIncomeButton);
        addIncomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent incomeIntent = new Intent(getActivity(), IncomeActivity.class);
                startActivity(incomeIntent);
            }
        });

        Button addExpenseButton = (Button) v.findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent expenseIntent = new Intent(getActivity(), ExpenseActivity.class);
                startActivity(expenseIntent);
            }
        });
        //access database
        new ExampleTask().execute();
        new IncomeTask().execute();
        new BackgroundTask().execute();
        return v;
    }

    class ExampleTask extends AsyncTask<Void,Void, String>
    {
        String budgetTarget;

        @Override
        protected void onPreExecute() {
            budgetTarget = "http://greenohi.cafe24.com/CurrentBudget.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(budgetTarget);
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
            double budgetPrint = 0.0;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String budgetAmount;
                if(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    budgetAmount = object.getString("budgetAmount");
                    Budget budget = new Budget(budgetAmount);
                    budgetPrint = Double.parseDouble(budgetAmount);
                    //count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            budgetTest.setText(String.valueOf(df.format(budgetPrint)));
        }
    }

    class IncomeTask extends AsyncTask<Void,Void, String>
    {
        String incomeTarget;

        @Override
        protected void onPreExecute() {
            incomeTarget = "http://greenohi.cafe24.com/MonthIncome.php";
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
            double monthPrint = 0.0;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String monthIncome;
                if(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    monthIncome = object.getString("SumMonth");
                    MonthIncome monthSum = new MonthIncome(monthIncome);
                    monthPrint = Double.parseDouble(monthIncome);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            currentIncomeAmount.setText(String.valueOf(df.format(monthPrint)));
        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String expenseTarget;
        //String budgetTarget;
        //String resultValue;

        @Override
        protected void onPreExecute()
        {
            expenseTarget = "http://greenohi.cafe24.com/ExpenseList.php";
            //budgetTarget = "http://greenohi.cafe24.com/CurrentBudget.php";
            //resultValue = result.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(expenseTarget);
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
            double sumExpense = 0.00;
            double totalBalance = 0.00;
            double calCurrentPercent = 0.0;

            try{
                expenseList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseName, expenseAmount, expenseCategory, expenseDate;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseName = object.getString("expenseName");
                    expenseAmount = object.getString("expenseAmount");
                    expenseCategory = object.getString("expenseCategory");
                    expenseDate = object.getString("expenseDate");
                    Expense expense = new Expense(expenseName, expenseAmount, expenseCategory, expenseDate);
                    expenseList.add(expense);

                    sumExpense = sumExpense + Double.parseDouble(expenseAmount);

                    count++;
                }
                adapter2.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //display total Income
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            currentExpenseAmount.setText(String.valueOf(df.format(sumExpense)));
            //display total Balance (sum of sumExpense and sumIncome)
            sumIncome = 23027.99;
            currentIncomeAmount.setText(String.valueOf(df.format(sumIncome)));
            totalBalance = sumIncome - sumExpense;
            currentBalance.setText(String.valueOf(df.format(totalBalance)));

            //Calculate percent of budget
            DecimalFormat df2 = new DecimalFormat();
            df2.setMaximumFractionDigits(1);
            double userBudget = Double.parseDouble(budgetTest.getText().toString());
            //double userBudget = 2000;
            calCurrentPercent = (sumExpense/userBudget) * 100;
            currentPercent.setText(String.valueOf(df2.format(calCurrentPercent)));

            if(calCurrentPercent > 100.00)
            {
                currentPercent.setTextColor(Color.parseColor("#e60000"));
            }
            else if(calCurrentPercent <= 100.00 && calCurrentPercent > 75.00)
            {
                currentPercent.setTextColor(Color.parseColor("#e6005c"));
            }
            else if(calCurrentPercent <= 75.00 && calCurrentPercent > 50.00)
            {
                currentPercent.setTextColor(Color.parseColor("#ff3300"));
            }
            else if(calCurrentPercent <= 50.00 && calCurrentPercent > 25.00)
            {
                currentPercent.setTextColor(Color.parseColor("#006600"));
            }
            else if(calCurrentPercent <= 25.00 && calCurrentPercent > 10.00)
            {
                currentPercent.setTextColor(Color.parseColor("#005c99"));
            }
            else
            {
                currentPercent.setTextColor(Color.BLACK);
            }
        }
    }

}
