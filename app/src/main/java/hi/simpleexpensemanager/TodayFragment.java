package hi.simpleexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);

        TextView yearLabel = (TextView)v.findViewById(R.id.yearLabel);
        TextView monthLabel = (TextView)v.findViewById(R.id.monthLabel);
        TextView dayLabel = (TextView)v.findViewById(R.id.dayLabel);
        TextView weekLabel = (TextView)v.findViewById(R.id.weekLabel);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/d/E", Locale.US);
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

}
