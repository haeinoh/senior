package hi.simpleexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
