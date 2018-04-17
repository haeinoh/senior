package hi.simpleexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
    //private View rootView;

    LineChart lineChart;

    //public TextView currentExpenseAmount;

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
                /*
                    .configure()    // starts configuration.
                    .formatTopText(String dateFormat)       // default to "MMM".
                    .formatMiddleText(String dateFormat)    // default to "dd".
                    .formatBottomText(String dateFormat)    // default to "EEE".
                    .showTopText(boolean show)              // show or hide TopText (default to true).
                        .showBottomText(boolean show)           // show or hide BottomText (default to true).
                        .textColor(int normalColor, int selectedColor)    // default to (Color.LTGRAY, Color.WHITE).
                        .selectedDateBackground(Drawable background)      // set selected date cell background.
                    .selectorColor(int color)               // set selection indicator bar's color (default to colorAccent).
                    .end()          // ends configuration.
                    */
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        //Line Chart
        //Data set
        lineChart = (LineChart) v.findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,37));
        entries.add(new Entry(1,58));
        entries.add(new Entry(2,150));
        entries.add(new Entry(3,45));
        entries.add(new Entry(4,10));
        entries.add(new Entry(5,99));
        entries.add(new Entry(6,78));

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
       /*
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(24, 24, 0);
 */

        lineChart.getDescription().setEnabled(false);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


        return v;
    }
}
