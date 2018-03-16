package hi.simpleexpensemanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class TodayFragment extends Fragment implements BudgetDialog.OnInputSelected {

    public TodayFragment() {
        // Required empty public constructor
    }

    private ListView incomeListView;
    private IncomeListAdapter adapter;
    private List<Income> incomeList;

    private static final String TAG = "TodayFragment";


    public void sendInput(String input){
        Log.d(TAG, "sendInput: from budgetsettng: " + input);
        mBudgetValue.setText(input);
    }
    public TextView mBudgetValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

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

       /* incomeListView = (ListView) getView().findViewById(R.id.incomeListView);
        incomeList = new ArrayList<Income>();
        adapter = new IncomeListAdapter(getContext().getApplicationContext(), incomeList);
        incomeListView.setAdapter(adapter);
        */

        //budgetSetting
        mBudgetValue = v.findViewById(R.id.budgetValue);
        Button budgetSetting = (Button) v.findViewById(R.id.budgetSetting);
        budgetSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: opening dialog");

                BudgetDialog dialog = new BudgetDialog();
                dialog.setTargetFragment(TodayFragment.this, 1);
                dialog.show(getFragmentManager(), "BudgetDialog");
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
        return v;
    }
/*
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
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //read one by one and store into temp
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
                    count++;
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
