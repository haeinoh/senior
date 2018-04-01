package hi.simpleexpensemanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Haein on 3/13/2018.
 */

public class BudgetDialog extends DialogFragment {
    private Fragment fragment;
    private static final String TAG="BudgetDialog";
    private EditText mBudgetInput;
    private Button mBudgetSaveButton, mBudgetCloseButton;

    public BudgetDialog(){
    }

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog, container, false);
        mBudgetSaveButton = view.findViewById(R.id.budgetSaveButton);
        mBudgetCloseButton = view.findViewById(R.id.budgetCloseButton);
        mBudgetInput = view.findViewById(R.id.budgetInput);

        mBudgetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        mBudgetSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: input");

                String input = mBudgetInput.getText().toString();
                if(!input.equals(""))
                {
                    //TodayFragment fragment = (TodayFragment) getActivity().getSupportFragmentManager().findFragmentByTag("TodayFragment");
                    //fragment.mBudgetValue.setText(input);
                    mOnInputSelected.sendInput(input);
                    final String budgetValue = mBudgetInput.getText().toString();


                }
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch(Exception e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
