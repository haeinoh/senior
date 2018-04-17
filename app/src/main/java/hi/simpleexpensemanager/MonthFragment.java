package hi.simpleexpensemanager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.security.KeyStore;
import java.text.DateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
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

    PieChart pieChart;

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

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        //Pie chart

        pieChart = (PieChart) v.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(0);

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

            yvalues.add(new PieEntry(6f, "Utility"));
            yvalues.add(new PieEntry(15f, "Education"));
            yvalues.add(new PieEntry(12f, "Clothing"));
            yvalues.add(new PieEntry(7f,  "Savings"));
            yvalues.add(new PieEntry(27f, "Food"));
            yvalues.add(new PieEntry(13f, "Home"));
            yvalues.add(new PieEntry(7f, "Entertainment"));

        PieDataSet dataSet = new PieDataSet(yvalues,"");

        pieChart.getDescription().setEnabled(false);

        pieChart.getLegend().setWordWrapEnabled(true);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //setting color
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);

        pieChart.setData(data);
        data.setValueFormatter(new PercentFormatter());

        return v;
    }
}
