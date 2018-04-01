package hi.simpleexpensemanager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.ArrayList;


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
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_month, container, false);

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
