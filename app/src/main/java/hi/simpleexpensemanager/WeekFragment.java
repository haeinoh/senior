package hi.simpleexpensemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
//import com.google.api.services.calendar.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Calendar;
import java.util.*;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {

    public WeekFragment() {
        // Required empty public constructor
    }

    private HorizontalCalendar horizontalCalendar;

    public LineChart lineChart;
    public TextView textView;

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("SUN");
        labels.add("MON");
        labels.add("TUE");
        labels.add("WED");
        labels.add("THU");
        labels.add("FRI");
        labels.add("SAT");

        return labels;
    }

    public class MyValueFormatter implements IValueFormatter{
        private DecimalFormat mFormat;

        public MyValueFormatter(){
            mFormat = new DecimalFormat("###,###,###0.0"); //use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler){

            return "$"+mFormat.format(value);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_week, container, false);
        //currentExpenseAmount = v.findViewById(R.id.currentExpenseAmount);

        //Calendar
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        //set up horizontalcalendar in fragment through its builder
            horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.weekcalendar)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        textView = v.findViewById(R.id.testText);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                if(position >=19 && position <= 25){
                    new WeekListP1().execute();
                }
                if(position >=26 && position <= 32 )
                {
                    new WeekList().execute();
                }
                if(position >= 33 && position <= 39)
                {
                    new WeekList2().execute();
                }
                if(position >= 40 && position <= 46)
                {
                    new WeekList3().execute();
                }
            }
        });

        //Line Chart
        //Data set
        lineChart = (LineChart) v.findViewById(R.id.chart);

        new WeekList2().execute();

        return v;
    }

    class WeekList extends AsyncTask<Void,Void, String>
    {
        String weekTarget;

        @Override
        protected void onPreExecute() {
            weekTarget = "http://greenohi.cafe24.com/WeekExpense.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(weekTarget);
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
            ArrayList<Entry> entries = new ArrayList<Entry>();
            double sumExpense = 0.0;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    WeekExpense weekExpense = new WeekExpense(expenseAmount);
                    f= Float.parseFloat(expenseAmount);

                    entries.add(new Entry(count, f));
                    sumExpense = sumExpense + Double.parseDouble(expenseAmount);

                    count++;
                }
                //x-axis label

                LineDataSet lineDataSet = new LineDataSet(entries, "$");

                LineData lineData = new LineData(lineDataSet);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.setData(lineData); //set the data and list of labels into chart

               lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));

                //set circle
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));

                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setValueTextSize(10);
                lineDataSet.setValueFormatter(new MyValueFormatter());

                //set y-axis
                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);
                //only left
                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                lineChart.getDescription().setEnabled(false);

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            textView.setText(" Week Expense : $" + String.valueOf(df.format(sumExpense)));
        }
    }

    class WeekList2 extends AsyncTask<Void,Void, String>
    {
        String weekTarget;

        @Override
        protected void onPreExecute() {
            weekTarget = "http://greenohi.cafe24.com/WeekExpense2.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(weekTarget);
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
            ArrayList<Entry> entries = new ArrayList<Entry>();
            double sumExpense = 0.0;

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    WeekExpense2 weekExpense2 = new WeekExpense2(expenseAmount);
                    f= Float.parseFloat(expenseAmount);

                        entries.add(new Entry(count, f));
                    sumExpense = sumExpense + Double.parseDouble(expenseAmount);

                    count++;
                }
                //x-axis label


                LineDataSet lineDataSet = new LineDataSet(entries, "$");

                LineData lineData = new LineData(lineDataSet);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.setData(lineData); //set the data and list of labels into chart

                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));

                //set circle
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));

                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setValueTextSize(10);
                lineDataSet.setValueFormatter(new MyValueFormatter());

                //set y-axis
                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);
                //only left
                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                lineChart.getDescription().setEnabled(false);

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            textView.setText(" Week Expense : $" + String.valueOf(df.format(sumExpense)));
        }
    }

    class WeekList3 extends AsyncTask<Void,Void, String>
    {
        String weekTarget;

        @Override
        protected void onPreExecute() {
            weekTarget = "http://greenohi.cafe24.com/WeekExpense3.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(weekTarget);
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
            ArrayList<Entry> entries = new ArrayList<Entry>();
            double sumExpense = 0.00;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    WeekExpense weekExpense = new WeekExpense(expenseAmount);
                    f= Float.parseFloat(expenseAmount);

                    entries.add(new Entry(count, f));
                    sumExpense = sumExpense + Double.parseDouble(expenseAmount);

                    count++;
                }
                //x-axis label


                LineDataSet lineDataSet = new LineDataSet(entries, "$");

                LineData lineData = new LineData(lineDataSet);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.setData(lineData); //set the data and list of labels into chart

                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));

                //set circle
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));

                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setValueTextSize(10);
                lineDataSet.setValueFormatter(new MyValueFormatter());

                //set y-axis
                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);
                //only left
                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                lineChart.getDescription().setEnabled(false);

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            textView.setText(" Week Expense : $" + String.valueOf(df.format(sumExpense)));
        }
    }

    class WeekListP1 extends AsyncTask<Void,Void, String>
    {
        String weekTarget;

        @Override
        protected void onPreExecute() {
            weekTarget = "http://greenohi.cafe24.com/WeekExpenseP1.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(weekTarget);
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
            ArrayList<Entry> entries = new ArrayList<Entry>();
            double sumExpense = 0.00;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    WeekExpense2 weekExpense2 = new WeekExpense2(expenseAmount);
                    f= Float.parseFloat(expenseAmount);
                    entries.add(new Entry(count, f));

                    sumExpense = sumExpense + Double.parseDouble(expenseAmount);

                    count++;
                }
                //x-axis label


                LineDataSet lineDataSet = new LineDataSet(entries, "$");

                LineData lineData = new LineData(lineDataSet);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.setData(lineData); //set the data and list of labels into chart

                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));

                //set circle
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));

                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setValueTextSize(10);
                lineDataSet.setValueFormatter(new MyValueFormatter());

                //set y-axis
                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);
                //only left
                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                lineChart.getDescription().setEnabled(false);

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            textView.setText(" Week Expense : $" + String.valueOf(df.format(sumExpense)));
        }
    }
}
