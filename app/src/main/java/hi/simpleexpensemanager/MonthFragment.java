package hi.simpleexpensemanager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.text.DateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {

    public MonthFragment() {
        // Required empty public constructor
    }
    private HorizontalCalendar horizontalCalendar;

    public PieChart pieChart;
   // public TextView textView;
   // public TextView textView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_month, container, false);

        //Calendar
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.DAY_OF_MONTH);
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        //set up horizontalcalendar in fragment through its builder
        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.monthCalendar)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.MONTHS)
                .configure()
                    .formatBottomText("yyyy")
                    .formatMiddleText("MMM")
                    .showTopText(false)
                    .showBottomText(true)
                .end()
                //.defaultSelectedDate();
                .build();

        pieChart = (PieChart) v.findViewById(R.id.piechart);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                @Override
         public void onDateSelected(Calendar date, int position) {
                if(position == 0)
                {
                    new MonthList().execute();
                }
                if(position == 1)
                {
                    new MonthList2().execute();
                }
                else if(position == 2)
                {
                    new MonthList3().execute();
                }
                //textView.setText("Selected Position: " + position);
         }
        });

        //Pie chart
        pieChart = (PieChart) v.findViewById(R.id.piechart);
        new MonthList2().execute();

        return v;
    }


    class MonthList extends AsyncTask<Void,Void, String>
    {
        String monthTarget;

        @Override
        protected void onPreExecute() {
            monthTarget = "http://greenohi.cafe24.com/MonthExpense.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(monthTarget);
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
            //ArrayList<PieEntry> xvalues = new ArrayList<>();
            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                String expenseCategory;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    expenseCategory = object.getString("expenseCategory");
                    MonthExpense monthExpense = new MonthExpense(expenseAmount, expenseCategory);
                    f= Float.parseFloat(expenseAmount);

                    yvalues.add(new PieEntry(f, expenseCategory));

                    count++;
                }

                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);

                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.TRANSPARENT);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(0);


                PieDataSet dataSet = new PieDataSet(yvalues,"");

                pieChart.getDescription().setEnabled(false);

                pieChart.getLegend().setWordWrapEnabled(true);

                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                //setting color
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.MATERIAL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);

                pieChart.setData(data);
                data.setValueFormatter(new PercentFormatter());
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MonthList2 extends AsyncTask<Void,Void, String>
    {
        String monthTarget;

        @Override
        protected void onPreExecute() {
            monthTarget = "http://greenohi.cafe24.com/MonthExpense2.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(monthTarget);
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
            //ArrayList<PieEntry> xvalues = new ArrayList<>();
            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                String expenseCategory;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    expenseCategory = object.getString("expenseCategory");
                    MonthExpense2 monthExpense2 = new MonthExpense2(expenseAmount, expenseCategory);
                    f= Float.parseFloat(expenseAmount);

                    yvalues.add(new PieEntry(f, expenseCategory));

                    count++;
                }

                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);

                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.TRANSPARENT);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(0);


                PieDataSet dataSet = new PieDataSet(yvalues,"");

                pieChart.getDescription().setEnabled(false);

                pieChart.getLegend().setWordWrapEnabled(true);


                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                //setting color
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.MATERIAL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);


                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);

                pieChart.setData(data);
                data.setValueFormatter(new PercentFormatter());
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MonthList3 extends AsyncTask<Void,Void, String>
    {
        String monthTarget;

        @Override
        protected void onPreExecute() {
            monthTarget = "http://greenohi.cafe24.com/MonthExpense3.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(monthTarget);
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
            //ArrayList<PieEntry> xvalues = new ArrayList<>();
            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String expenseAmount;
                String expenseCategory;
                float f;
                while(count < jsonArray.length())
                {   //current array element
                    JSONObject object = jsonArray.getJSONObject(count);
                    expenseAmount = object.getString("expenseAmount");
                    expenseCategory = object.getString("expenseCategory");
                    MonthExpense2 monthExpense2 = new MonthExpense2(expenseAmount, expenseCategory);
                    f= Float.parseFloat(expenseAmount);

                    yvalues.add(new PieEntry(f, expenseCategory));

                    count++;
                }

                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);

                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.TRANSPARENT);
                pieChart.setHoleRadius(30f);
                pieChart.setTransparentCircleRadius(0);


                PieDataSet dataSet = new PieDataSet(yvalues,"");

                pieChart.getDescription().setEnabled(false);

                pieChart.getLegend().setWordWrapEnabled(true);


                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                //setting color
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.MATERIAL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);
                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);


                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setValueTextSize(10f);

                pieChart.setData(data);
                data.setValueFormatter(new PercentFormatter());
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
